package com.wink.livemall.order.dao;

import com.wink.livemall.order.dto.LmOrderComment;
import com.wink.livemall.order.dto.LmOrderGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LmOrderGoodsDao extends tk.mybatis.mapper.common.Mapper<LmOrderGoods>{

    @Select("select * from lm_order_goods where orderid=#{id} limit 0,1 ")
    LmOrderGoods findByOrderid(@Param("id") int id);


    //普通拍卖
    @Select("select * from lm_order_goods where goodid=#{id} and goodstype =0  limit 1 ")
    LmOrderGoods findByGoodsid0(@Param("id") int id);


    //直播拍卖
    @Select("select * from lm_order_goods where goodid=#{id} and goodstype =1 limit 1 ")
    LmOrderGoods findByGoodsid1(@Param("id") int id);

    //直播拍卖
    @Select("select * from lm_order_goods where goodid=#{goodid} and goodstype =1  ")
   List<LmOrderGoods> findByGoodRepeat(@Param("goodid") int goodid);

    @Select("select log.* from lm_order_goods log left join lm_orders  lo on log.orderid = lo.id  where lo.merchid=#{merchid} ")
    List<LmOrderComment> findByMerchid(@Param("merchid")int merchid);
}
