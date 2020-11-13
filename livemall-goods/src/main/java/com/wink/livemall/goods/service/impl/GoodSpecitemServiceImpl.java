package com.wink.livemall.goods.service.impl;

import com.wink.livemall.goods.dao.GoodDao;
import com.wink.livemall.goods.dao.GoodSpecDao;
import com.wink.livemall.goods.dao.GoodSpecItemDao;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.GoodSpecItem;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.GoodSpecitemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class GoodSpecitemServiceImpl implements GoodSpecitemService {

@Resource
private GoodSpecItemDao goodSpecItemDao;

    /**
     * 根据goodid查询
     * @param id
     * @return
     */
    @Override
    public List<GoodSpecItem> findByGoodId(String id) {
        return goodSpecItemDao.findByGoodId(Integer.parseInt(id));
    }

    /**
     * 删除
     * @param id
     */
    @Override
    @Transactional
    public void delete(String id) {
        goodSpecItemDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 添加
     * @param goodSpecItem
     */
    @Override
    public void insertGoodSpecItem(GoodSpecItem goodSpecItem) {
        goodSpecItemDao.insert(goodSpecItem);
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @Override
    public GoodSpecItem findById(String id) {
        return goodSpecItemDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 更新信息
     * @param goodSpecItem
     */
    @Override
    public void updateGoodSpecItem(GoodSpecItem goodSpecItem) {
        goodSpecItemDao.updateByPrimaryKey(goodSpecItem);
    }

    @Override
    public List<GoodSpecItem> findBySpecid(int id) {
        return goodSpecItemDao.findBySpecid(id);
    }

    @Override
    public List<String> findtitleBySpecid(int id) {
        return goodSpecItemDao.findtitleBySpecid(id);
    }
}
