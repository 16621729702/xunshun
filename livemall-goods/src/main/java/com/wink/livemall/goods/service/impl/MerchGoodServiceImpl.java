package com.wink.livemall.goods.service.impl;


import com.wink.livemall.goods.dao.GoodCategoryDao;
import com.wink.livemall.goods.dao.GoodDao;
import com.wink.livemall.goods.dao.LmGoodAuctionDao;
import com.wink.livemall.goods.dao.MerchGoodDao;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.GoodCategory;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodCategoryService;
import com.wink.livemall.goods.service.MerchGoodService;

import com.wink.livemall.goods.utils.Errors;
import com.wink.livemall.goods.utils.HttpJsonResult;
import com.wink.livemall.merch.dao.LmMerchInfoDao;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.springframework.util.StringUtils;


import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class MerchGoodServiceImpl implements MerchGoodService {

    @Resource
    private MerchGoodDao MerchGoodDao;
	@Resource
	private GoodDao goodDao;
    @Resource
    private LmGoodAuctionDao lmGoodAuctionDao;
	@Resource
	private LmMerchInfoDao lmMerchInfoDao;
	@Resource
	private GoodCategoryDao goodCategoryDao;


	@Override
	public void addGoods(Good good) {
		MerchGoodDao.insertSelective(good);
	}
    
	@Override
	public HttpJsonResult changeGood(Good good,Integer types) {
		HttpJsonResult jsonResult=new HttpJsonResult<>();
		int isExist = lmMerchInfoDao.checkMerchEnable(String.valueOf(good.getMer_id()));
		if (isExist == 0) {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("商户不存在或者被禁用");
			return jsonResult;
		}
		if(!StringUtils.isEmpty(good.getCategory_id())){
			good.setCategory_id(good.getCategory_id());
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("商品类目"+"值为null");
			return jsonResult;
		}
		if(!StringUtils.isEmpty(good.getMer_id())){
			good.setMer_id(good.getMer_id());
		}
		if(!StringUtils.isEmpty(good.getBuyway())){
			good.setBuyway(good.getBuyway());
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("违约金开关"+"值为null");
			return jsonResult;
		}
		if(!StringUtils.isEmpty(good.getDescription())){
			good.setDescription(good.getDescription());
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("商品描述"+"值为null");
			return jsonResult;
		}
		String materials="";
		GoodCategory goodCategory = goodCategoryDao.findgoodscategoryById(Integer.parseInt(good.getCategory_id()));
		if(!StringUtils.isEmpty(goodCategory.getName())){
			for (int j = 0; j < 2; j++) {
				char word = goodCategory.getName().charAt(j);
				String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
				if (pinyinArray != null) {
					materials += Character.toUpperCase(pinyinArray[0].charAt(0));
				} else {
					materials += Character.toUpperCase(word);
				}
			}
		}
		good.setBidsnum(0);
		good.setCreate_at(new Date());
		if(types==1){
			DateFormat format1 = new SimpleDateFormat("MMdd");//日期格式
			String sn = materials + String.format("%04d", good.getMer_id()) + format1.format(new Date()) + (int) (Math.random() * (9999 - 1000) + 1000);
			good.setSn(sn);
			if(good.getState()==0){
				good.setWarehouse("1");
			}else {
				good.setWarehouse("2");
			}
			MerchGoodDao.insertSelective(good);
		}else {
			if(good.getType()==1){
					List<LmGoodAuction> list = lmGoodAuctionDao.findAllByGoodid(good.getId(), 0);
					if(list!=null&&list.size()>0){
						Good goodDaoById = goodDao.findById(good.getId());
						good.setId(0);
						Good goodDaoByIds=good;
						DateFormat format1 = new SimpleDateFormat("MMdd");//日期格式
						String sn = materials + String.format("%04d", good.getMer_id()) + format1.format(new Date()) + (int) (Math.random() * (9999 - 1000) + 1000);
						goodDaoByIds.setSn(sn);
						goodDaoByIds.setWarehouse("1");
						goodDaoByIds.setBidsnum(0);
						goodDaoByIds.setIsdelete(0);
						MerchGoodDao.insertSelective(goodDaoByIds);
						goodDaoById.setState(0);
						goodDaoById.setWarehouse("1");
						goodDaoById.setAuction_status(2);
						goodDaoById.setIsdelete(1);
						MerchGoodDao.updateByPrimaryKeySelective(goodDaoById);
					}else {
						good.setWarehouse("1");
						good.setBidsnum(0);
						good.setUpdate_at(new Date());
						MerchGoodDao.updateByPrimaryKeySelective(good);
				    }
			}else {
				List<LmGoodAuction> list = lmGoodAuctionDao.findAllByGoodid(good.getId(), 0);
				if(list!=null&&list.size()>0){
					jsonResult.setMsg("该商品已有出价记录,不可修改成一口价商品");
					jsonResult.setCode(Errors.ERROR.getCode());
					return jsonResult;
				}
				if(good.getState()==0){
					good.setWarehouse("1");
				}else {
					good.setWarehouse("2");
				}
					good.setUpdate_at(new Date());
					MerchGoodDao.updateByPrimaryKeySelective(good);
			}
		}
		jsonResult.setCode(Errors.SUCCESS.getCode());
		jsonResult.setMsg(Errors.SUCCESS.getMsg());
		return  jsonResult;
	}


	@Override
	public void updateByFields(List<Map<String, String>> params, String id) {
		// TODO Auto-generated method stub
		MerchGoodDao.updateByFields(params, id);
	}

	@Override
	public void addByFields(List<Map<String, String>> params) {
		// TODO Auto-generated method stub
		MerchGoodDao.addByFields(params);
	}


	@Override
	public List<Map<String, Object>> getPage(Map<String, String> params) {
		// TODO Auto-generated method stub
		return MerchGoodDao.getpage(params);
	}


	@Override
	public void delGoods(int id) {
		// TODO Auto-generated method stub
		MerchGoodDao.deleteByPrimaryKey(id);
	}


	@Override
	public int countGoodsNum(int merchid) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countGoodsNum(merchid);
	}

	@Override
	public int newCountGoodsNum(int merchid,int type,int state) {
		// TODO Auto-generated method stub
		return MerchGoodDao.newCountGoodsNum(merchid,type,state);
	}


	@Override
	public int countOrderNum(int merchid) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countOrderNum(merchid);
	}


	@Override
	public Integer countOrderPrice(int merchid) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countOrderPrice(merchid);
	}


	@Override
	public void updateEntity(Good good) {
		// TODO Auto-generated method stub
		MerchGoodDao.updateByPrimaryKeySelective(good);
	}


	@Override
	public LmGoodAuction findLastAuction(int goodid) {
		// TODO Auto-generated method stub
		
		return MerchGoodDao.findLast(goodid);
	}


	@Override
	public int countAuctionGoods(int merchid) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countAuctionGoods(merchid, new Date());
	}


	@Override
	public List<Map<String, Object>> findListByCondition(Map<String, String> params) {
		// TODO Auto-generated method stub
		return MerchGoodDao.findListByCondition(params);
	}


	@Override
	public int countByStatus(Map<String, String> params) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countByStatus(params);
	}


	@Override
	public List<Map<String, Object>> findWarehouses(String ids) {
		// TODO Auto-generated method stub
		return MerchGoodDao.findWarehouses(ids);
	}


	@Override
	public Map<String, Object> findGoodByid(String id) {
		// TODO Auto-generated method stub
		return MerchGoodDao.findByid(id);
	}

  


}
