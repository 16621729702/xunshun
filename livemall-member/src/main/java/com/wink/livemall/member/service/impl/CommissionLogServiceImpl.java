package com.wink.livemall.member.service.impl;

import com.wink.livemall.goods.dao.GoodDao;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.member.dao.CommissionLogDao;
import com.wink.livemall.member.dao.LmMemberDao;
import com.wink.livemall.member.dto.CommissionLog;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.service.CommissionLogService;
import com.wink.livemall.order.dao.LmOrderGoodsDao;
import com.wink.livemall.order.dao.LmShopOrderDao;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.sys.withdraw.dao.WithdrawLogDao;
import com.wink.livemall.sys.withdraw.dto.WithdrawLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CommissionLogServiceImpl implements CommissionLogService {
    @Resource
    private CommissionLogDao commissionLogDao;
    @Resource
    private WithdrawLogDao withdrawLogDao;
    @Resource
    private LmOrderGoodsDao lmOrderGoodsDao;
    @Resource
    private GoodDao goodDao;
    @Resource
    private LmShopOrderDao lmShopOrderDao;
    @Resource
    private LmMemberDao lmMemberDao;

    @Override
    public void insertCommissionLog(CommissionLog commissionLog) {
        commissionLog.setCreate_time(new Date());
        commissionLog.setUpdate_time(new Date());
        commissionLogDao.insertSelective(commissionLog);
    }

    @Override
    public void updateCommissionLog(CommissionLog commissionLog) {
        commissionLog.setUpdate_time(new Date());
        commissionLogDao.updateByPrimaryKeySelective(commissionLog);
    }

    @Override
    public List<CommissionLog> findListByGainer(int gainer) {
        return commissionLogDao.findListByGainer(gainer);
    }


    @Override
    public List<Map<String, Object>> findListCredit(Integer userId) {
        List<WithdrawLog> listByUserId = withdrawLogDao.findListByUserId(userId);
        List<Map<String, Object>> listCredit=new LinkedList<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(listByUserId.size()>0){
            for(WithdrawLog withdrawLog:listByUserId){
                Map<String,Object> map=new HashMap<>();
                BigDecimal realPrice = withdrawLog.getAmount();
                map.put("price",realPrice);
                map.put("time",dateFormat.format(withdrawLog.getCreate_time()));
                map.put("type",withdrawLog.getState());
                map.put("description",withdrawLog.getDesc());
                listCredit.add(map);
            }
        }
        return listCredit;
    }

    @Override
    public List<Map<String, Object>> userCommissionLog(Integer userId) {
        List<Map<String, Object>> listCredit=new LinkedList<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<CommissionLog> listByGainer = commissionLogDao.findListByGainer(userId);
        if(listByGainer.size()>0){
            for(CommissionLog commissionLog:listByGainer){
                Map<String,Object> map=new HashMap<>();
                BigDecimal realPrice = commissionLog.getCommission();
                map.put("price",realPrice);
                map.put("time",dateFormat.format(commissionLog.getCreate_time()));
                map.put("type",0);
                LmOrderGoods byOrderId = lmOrderGoodsDao.findByOrderid(commissionLog.getOrder_id());
                LmOrder lmOrder = lmShopOrderDao.findOrderById(commissionLog.getOrder_id());
                LmMember lmMember = lmMemberDao.selectByPrimaryKey(lmOrder.getMemberid());
                map.put("nickname",lmMember.getNickname());
                map.put("avatar",lmMember.getAvatar());
                map.put("orderId",lmOrder.getOrderid());
                Good byId = goodDao.findById(byOrderId.getGoodid());
                map.put("description","因"+byId.getTitle()+"分享卖出获得的佣金");
                listCredit.add(map);
            }
        }
        return listCredit;
    }
}
