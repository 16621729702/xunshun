package com.wink.livemall.goods.service.impl;

import com.wink.livemall.goods.dao.*;
import com.wink.livemall.goods.dto.*;
import com.wink.livemall.goods.service.GoodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class GoodServiceImpl implements GoodService {
    @Resource
    private LmGoodMaterialDao lmGoodMaterialDao;
    @Resource
    private GoodDao goodDao;
    @Resource
    private LmGoodAuctionDao lmGoodAuctionDao;
    @Resource
    private LmShareGoodDao lmShareGoodDao;
    @Resource
    private LiveGoodDao liveGoodDao;

    /**
     * 根据条件查询商品
     * @param condient
     * @return
     */
    @Override
    public List<Map> findListByCondient(Map<String, String> condient) {
        return goodDao.findListByCondient(condient);
    }

    /**
     * 根据id删除商品信息
     * @param id
     */
    @Override
    @Transactional
    public void delete(String id) {
        //删除商品信息
        goodDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 根据id查询实体
     * @return
     */
    @Override
    public Good findById(int id) {
        return goodDao.findById(id);
    }


    @Override
    public void updateGoods(Good good) {
        goodDao.updateByPrimaryKey(good);
    }

    @Override
    public List<Good> findAll() {
        return goodDao.selectAll();
    }



    @Override
    public List<Map> findRecommendList() {
        return goodDao.findRecommendList();
    }

    @Override
    public List<Map> findHotList() {
        return goodDao.findHotList();
    }

    @Override
    public List<Good> findGoodList() {
        return goodDao.findGoodList();
    }

    @Override
    public List<Map<String, Object>> findByMerchIdByApi(Integer merchid) {
        return goodDao.findByMerchIdByApi(merchid);
    }

    @Override
    public List<Map<String, Object>> findByMerchIdAndTypeByApi(int merchid, int type) {
        return goodDao.findByMerchIdAndTypeByApi(merchid,type);
    }

    @Override
    public List<LmGoodMaterial> getGoodMaterialByApi() {
        return lmGoodMaterialDao.selectAll();
    }

    @Override
    public List<Map> findInfoByApi(String categoryid, String goodname, String topprice, String lowprice, String isauction, String isstores, String isgoodshop, String ispackage, String isback, String isstudio, String material, String sorttype, String sortway, String type,String pid) {
        List<Map> list =goodDao.findInfoByApi(categoryid,goodname,topprice,lowprice,isauction,isstores,isgoodshop,ispackage,isback,isstudio,material,sorttype,sortway,type,pid);
        if(list!=null){
            return list;
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> findhotByMerchIdByApi(int merchid) {
        return goodDao.findhotByMerchIdByApi(merchid);
    }

    @Override
    public List<Map<String,Object>> findAuctionlistByGoodid(int id,int type) {
        return lmGoodAuctionDao.findAuctionlistByGoodid(id,type);
    }

    @Override
    public void updateAuctionService(LmGoodAuction lmGoodAuction) {
        lmGoodAuctionDao.updateByPrimaryKey(lmGoodAuction);
    }

    @Override
    public void insertAuctionService(LmGoodAuction lmGoodAuction) {
        lmGoodAuctionDao.insert(lmGoodAuction);
    }

    @Override
    public List<Good> findwaitGoodInfo(Date nowdate) {
        return goodDao.findwaitGoodInfo(nowdate);
    }

    @Override
    public List<Good> findwaitGoodInfo15(Date nowdate,Date olddate) {
        return goodDao.findwaitGoodInfo15(nowdate,olddate);
    }

    @Override
    public List<Good> findAllwaitGoodInfo(Date nowdate) {
        return goodDao.findAllwaitGoodInfo(nowdate);
    }

    @Override
    public void insertShare(LmShareGood lmShareGood) {
        lmShareGoodDao.insert(lmShareGood);
    }

    @Override
    public LmShareGood findshareById(int goodid) {
        return lmShareGoodDao.selectByPrimaryKey(goodid);
    }

    @Override
    public void insertLivedGood(LivedGood livedGood) {
        liveGoodDao.insertSelective(livedGood);
    }

    @Override
    public LivedGood findLivedGood(int goodid) {
        return liveGoodDao.selectByPrimaryKey(goodid);
    }

    @Override
    public void updateLivedGood(LivedGood good) {
        liveGoodDao.updateByPrimaryKey(good);
    }

    @Override
    public void updateShareGood(LmShareGood good) {
        lmShareGoodDao.updateByPrimaryKey(good);
    }

    @Override
    public List<LmShareGood> findShareGood(Date date) {
        return lmShareGoodDao.findShareGood(date);
    }

    @Override
    public List<LivedGood>  findLivedGoodByLiveid(int liveid) {
        return liveGoodDao.findLivedGoodByLiveid(liveid);
    }

    @Override
    public List<LivedGood> findLiveGood(Date date) {
        return liveGoodDao.findShareGood(date);
    }

    @Override
    public List<LmGoodAuction> findAuctionlistByGoodid2(int goodid,int type) {
        return lmGoodAuctionDao.findAuctionlistByGoodid2(goodid,type);
    }

    @Override
    public List<Map<String, Object>> findAuctionlistByUseridAndType(int userid, int type) {
        return lmGoodAuctionDao.findAuctionlistByUseridAndType(userid,type);
    }

    @Override
    public List<Map<String, Object>> findAuctionlistByUseridAndType2(int userid, int type) {
        return lmGoodAuctionDao.findAuctionlistByUseridAndType2(userid,type);
    }

    @Override
    public void insertService(Good good) {
        goodDao.insert(good);
    }

    @Override
    public List<Map> findInfoByName(String name) {
        return goodDao.findInfoByName(name);
    }

    @Override
    public List<LmGoodMaterial> findMaterlist() {
        return lmGoodMaterialDao.findbyList();
    }

    @Override
    public LmGoodMaterial findMaterById(int id) {
        return lmGoodMaterialDao.selectByPrimaryKey(id);
    }

    @Override
    public void insertMater(LmGoodMaterial material) {
        lmGoodMaterialDao.insert(material);
    }

    @Override
    public void deletemater(int id) {
        lmGoodMaterialDao.deleteByPrimaryKey(id);
    }

    @Override
    public void updatemater(LmGoodMaterial material) {
        lmGoodMaterialDao.updateByPrimaryKey(material);
    }

	@Override
	public List<LmShareGood> findshareGoodByLiveid(String liveid) {
		// TODO Auto-generated method stub
		return lmShareGoodDao.findshareGoodByLiveid(Integer.parseInt(liveid));
	}

    @Override
    public List<LivedGood> movementImLive(int liveid) {
        return liveGoodDao.movementImLive(liveid);
    }

}
