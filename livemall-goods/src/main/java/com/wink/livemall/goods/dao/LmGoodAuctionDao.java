package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.LmGoodAuction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmGoodAuctionDao extends tk.mybatis.mapper.common.Mapper<LmGoodAuction> {

    @Select("select lga.goodid as goodid,lga.price as price,lga.status as status,lm.id as userid," +
            " lga.createtime as createtime ,lm.avatar as avatar,lm.nickname as nickname from lm_good_auction lga,lm_member lm  where lga.memberid = lm.id and lga.goodid = #{id} and lga.type = #{type} order by lga.price desc")
    List<Map<String,Object>> findAuctionlistByGoodid(@Param("id") int id,@Param("type") int type);

    @Select("select * from lm_good_auction  where goodid = #{goodid} and type = #{type}  order by price desc")
    List<LmGoodAuction> findAuctionlistByGoodid2(@Param("goodid")int goodid,@Param("type")int type);

    @Select("select * from lm_good_auction  where goodid = #{goodid} and type = #{type} order by price desc limit 0,1")
    LmGoodAuction findnowPriceByGoodidByApi(@Param("goodid")int goodid,@Param("type")int type);

    @Select("select * from lm_good_auction  where goodid = #{goodid} and type = #{type} ")
    List<LmGoodAuction> findAllByGoodid(@Param("goodid")int id,@Param("type")int type);

    @Select("select lga.price as price ,lga.createtime as createtime, lg.thumb as thumb, lg.id as id, lga.id as lgaid," +
            " lg.title as goodname,lm.avatar as avatar,lm.store_name as store_name from lm_good_auction lga,lm_goods lg,lm_merch_info lm  where lga.goodid = lg.id and lm.id = lg.mer_id and lga.memberid = #{userid} and lga.status = #{type} order by lga.createtime desc")
    List<Map<String, Object>> findAuctionlistByUseridAndType(@Param("userid")int userid, @Param("type")int type);

    @Select("select lga.price as price ,lga.createtime as createtime, lg.img as thumb, lg.id as id, lga.id as lgaid," +
            " lg.name as goodname,lm.avatar as avatar,lm.store_name as store_name from lm_good_auction lga,lm_livegood lg,lm_merch_info lm,lm_lives ll  where  lg.liveid = ll.id and lga.goodid = lg.id and lm.id = ll.merch_id and lga.memberid = #{userid} and lga.status = #{type} order by lga.createtime desc")
    List<Map<String, Object>> findAuctionlistByUseridAndType2(@Param("userid")int userid, @Param("type")int type);

    /**
     * @param memberid
     * @return
     */
    @Select("select * from lm_good_auction  where memberid = #{memberid}  order by price desc limit 0,1")
    LmGoodAuction isHavingAuction(@Param("memberid")int memberid);

}
