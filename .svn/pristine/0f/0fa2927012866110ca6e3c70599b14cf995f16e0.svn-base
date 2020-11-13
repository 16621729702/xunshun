package com.wink.livemall.goods.service.impl;


import com.wink.livemall.goods.dao.GoodSpecDao;
import com.wink.livemall.goods.dao.GoodSpecItemDao;
import com.wink.livemall.goods.dao.GoodSpecOptionDao;
import com.wink.livemall.goods.dto.GoodSpec;
import com.wink.livemall.goods.dto.GoodSpecItem;
import com.wink.livemall.goods.dto.GoodSpecOption;
import com.wink.livemall.goods.service.GoodSpecService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class GoodSpecServiceImpl implements GoodSpecService {

    @Resource
    private GoodSpecDao specDao;
    @Resource
    private GoodSpecItemDao goodSpecItemDao;
    @Resource
    private GoodSpecOptionDao goodSpecOptionDao;

    @Override
    public List<GoodSpec> findbyGoodId(String id) {
        return specDao.findbyGoodId(Integer.parseInt(id));
    }

    /**
     * 根据id删除商品规格 和 规格项 和选项
     * @param id
     */
    @Override
    @Transactional
    public void delete(String id) {
        List<GoodSpecItem> itemList = goodSpecItemDao.findBySpecid(Integer.parseInt(id));
        List<GoodSpecOption> list = goodSpecOptionDao.selectAll();
        for(GoodSpecItem goodSpecItem:itemList){
            for(GoodSpecOption goodSpecOption:list){
                if(goodSpecOption.getSpec_item_ids().contains(goodSpecItem.getId()+"")){
                    goodSpecOptionDao.delete(goodSpecOption);
                }
            }
        }
        specDao.deleteByPrimaryKey(Integer.parseInt(id));
        goodSpecItemDao.deleteBySpecid(Integer.parseInt(id));
    }

    /**
     * 添加
     * @param goodSpec
     */
    @Override
    public void insertGoodspec(GoodSpec goodSpec) {
        specDao.insert(goodSpec);
    }

    /**
     * 根据主键查询
     * @param spec_id
     * @return
     */
    @Override
    public GoodSpec findByid(String spec_id) {
        return specDao.selectByPrimaryKey(Integer.parseInt(spec_id));
    }

    /**
     * 更新信息
     * @param goodSpec
     */
    @Override
    public void updateGoodspec(GoodSpec goodSpec) {
        specDao.updateByPrimaryKey(goodSpec);
    }
}
