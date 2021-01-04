package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.Good;
import org.apache.ibatis.annotations.*;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface GoodDao extends tk.mybatis.mapper.common.Mapper<Good> {

    /**
     * 根据 id 删除商品信息
     */
    @Delete("DELETE from lm_goods WHERE id = #{id}")
    void deleteUser(@Param("id") int id);

    /**
     * 根据条件查询商品
     * @param condient
     * @return
     */
    @SelectProvider(type = GoodDaoprovider.class, method = "findListByCondient")
    List<Map> findListByCondient(Map<String, String> condient);

    @Select("SELECT * FROM lm_goods where id = #{id}")
    Good findById(@Param("id") int id);


    @Select("SELECT lg.id as goodid,lg.bidsnum as bidsnum , lg.title as name , lg.thumb as goodimg," +
            " lm.isauction as isauction ," +
            " lm.isdirect as isdirect ," +
            " lm.isquality as isquality ," +
            " lm.postage as postage ," +
            " lm.refund as refund ," +
            " lm.isoem as isoem ," +
            " lg.stepprice as stepprice ," +
            " lg.productprice as goodprice FROM lm_goods lg,lm_merch_info lm  where lg.mer_id = lm.id and lg.isrecommend = 1 and lg.state = 1 ")
    List<Map> findRecommendList();

    //order by rand()
    @Select("SELECT lg.id as goodid,lg.bidsnum as bidsnum ,lg.title as name , lg.thumb as goodimg," +
            " lg.thumbs as goodimgs ," +
            " lm.isauction as isauction ," +
            " lm.isdirect as isdirect ," +
            " lm.isquality as isquality ," +
            " lm.postage as postage ," +
            " lm.refund as refund ," +
            " lm.isoem as isoem ," +
            " lg.stepprice as stepprice ," +
            " lg.marketprice as marketprice ," +
            " lg.type as type ," +
            " lg.productprice as goodprice FROM lm_goods lg,lm_merch_info lm  where lg.mer_id = lm.id and lg.state = 1 ")
    List<Map> findHotList();


    @Select("SELECT lg.type as type,lg.id as goodid,lg.title as name ,lg.thumb as goodimg,lg.thumbs as goodimgs," +
            " lg.productprice as goodprice," +
            " lg.stepprice as stepprice," +
            " lg.bidsnum as bidsnum " +
            "FROM lm_goods lg  where lg.mer_id = #{merchid} and lg.state = 1 order by sale_num ")
    List<Map<String, Object>> findByMerchIdByApi(@Param("merchid")int merchid);

    @Select("SELECT lg.id as goodid," +
            " lm.isauction as isauction ," +
            " lm.isdirect as isdirect ," +
            " lm.isquality as isquality ," +
            " lm.postage as postage ," +
            " lm.refund as refund ," +
            " lm.isoem as isoem ," +
            " lg.stepprice as stepprice," +
            " lg.type as type ," +
            " lg.thumbs as goodimgs ," +
            "lg.title as name ,lg.bidsnum as bidsnum, lg.thumb as goodimg, lg.productprice as goodprice FROM lm_goods lg,lm_merch_info lm  where lg.mer_id = lm.id and lg.mer_id = #{merchid} and lg.type = #{type} and lg.state = 1 order by sale_num ")
    List<Map<String, Object>> findByMerchIdAndTypeByApi(@Param("merchid")int merchid, @Param("type")int type);

    @SelectProvider(type = GoodDaoprovider.class, method = "findInfoByApi")
    List<Map> findInfoByApi(String categoryid, String goodname, String topprice, String lowprice , String isauction, String isstores, String isgoodshop, String ispackage, String isback, String isstudio, String material, String sorttype, String sortway,String type,String pid);


    @Select("SELECT lg.id as goodid,lg.title as name , lg.thumb as goodimg, lg.productprice as goodprice," +
            " lm.isauction as isauction ," +
            " lm.isdirect as isdirect ," +
            " lm.isquality as isquality ," +
            " lm.postage as postage ," +
            " lm.refund as refund ," +
            " lm.isoem as isoem ," +
            " lg.stepprice as stepprice," +
            " lg.thumbs as goodimgs," +
            " lg.type as type," +
            "lg.bidsnum as bidsnum FROM lm_goods lg,lm_merch_info lm  where lg.mer_id = lm.id and lg.mer_id = #{merchid} and lg.state = 1 order by sale_num ")
    List<Map<String, Object>> findhotByMerchIdByApi(@Param("merchid")int merchid);


    @Select("SELECT * FROM lm_goods lg  where lg.type = 1 and lg.auction_status = 0 and lg.auction_end_time < #{nowdate} ")
    List<Good> findwaitGoodInfo(@Param("nowdate")Date nowdate);

    /**
     * 开始寻找拍卖前15分钟订单
     * @return
     */
    @Select("SELECT * FROM lm_goods lg  where lg.type = 1 and lg.auction_status = 0 and #{nowdate} < lg.auction_end_time and lg.auction_end_time < #{olddate} ")
    List<Good> findwaitGoodInfo15(@Param("nowdate")Date nowdate,@Param("olddate")Date olddate);


    @Select("SELECT * FROM lm_goods lg  where lg.type = 1  and lg.auction_end_time < #{nowdate} ")
    List<Good> findAllwaitGoodInfo(@Param("nowdate")Date nowdate);


    @SelectProvider(type = GoodDaoprovider.class, method = "findInfoByName")
    List<Map> findInfoByName(String name);


    class GoodDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_goods where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("title"))){
                sql += " and title like '%"+condient.get("title")+"%'";
            }

            if(!StringUtils.isEmpty(condient.get("state"))){
                sql += " and state = '"+condient.get("state")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("categoryid"))){
                sql += " and category_id = '"+condient.get("categoryid")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("merchid"))){
                sql += " and mer_id = "+condient.get("merchid");
            }
            if(!StringUtils.isEmpty(condient.get("startdate"))){
                sql += " and auction_start_time >= '"+condient.get("startdate")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("enddate"))){
                sql += " and auction_end_time <= '"+condient.get("enddate")+"'";
            }
            sql += " order by id desc ";
            return sql;
        }

        public String findInfoByApi(String categoryid, String goodname, String topprice, String lowprice,
                                    String isauction, String isstores,
                                    String isgoodshop, String ispackage,
                                    String isback, String isstudio,
                                    String material, String sorttype, String sortway,String type,String pid) {
            String sql = "SELECT " +
                    " lg.thumb as goodimg, " +
                    " lg.id as goodid, " +
                    " lg.productprice as price," +
                    " lg.title as name , " +
                    " lm.isauction as isauction ," +
                    " lm.isdirect as isdirect ," +
                    " lm.isquality as isquality ," +
                    " lm.postage as postage ," +
                    " lm.refund as refund ," +
                    " lm.isoem as isoem ," +
                    " lg.stepprice as stepprice," +
                    " lg.type as type," +
                    " lg.bidsnum as bidsnum" +
                    " from lm_goods lg left join lm_merch_info lm on lg.mer_id = lm.id " +
                    " left join lm_goods_categories lgc on lg.category_id = lgc.id  where lg.state = 1  ";
            if(!StringUtils.isEmpty(categoryid)){
                if(!"0".equals(categoryid)){
                    if(categoryid.contains(",")){
                        String [] ids = categoryid.split(",");
                        sql+=" and ";
                        for(String id:ids){
                            sql += " lg.category_id = "+id+" or";
                        }
                        sql = sql.substring(0,sql.length()-2);
                    }else{
                        sql += " and lg.category_id = "+categoryid;
                    }
                }else{
                    sql += " and lg.category_id = "+pid;
                }
            }
            if(!StringUtils.isEmpty(type)){
                sql += " and lg.type = '"+type+"'";
            }
            if(!StringUtils.isEmpty(isauction)){
                sql += " and lm.isauction = '"+isauction+"'";
            }
            if(!StringUtils.isEmpty(isstores)){
                sql += " and lm.isdirect = '"+isstores+"'";
            }
            if(!StringUtils.isEmpty(isgoodshop)){
                sql += " and lm.isquality = '"+isgoodshop+"'";
            }
            if(!StringUtils.isEmpty(ispackage)){
                sql += " and lm.postage = '"+ispackage+"'";
            }
            if(!StringUtils.isEmpty(isback)){
                sql += " and lm.refund = '"+isback+"'";
            }
            if(!StringUtils.isEmpty(isstudio)){
                sql += " and lm.isoem = '"+isstudio+"'";
            }
            if(!StringUtils.isEmpty(goodname)){
                sql += " and lg.title like '%"+goodname+"%'";
            }
            if(!StringUtils.isEmpty(material)){
                if(material.contains(",")){
                    String [] ids = material.split(",");
                    sql+=" and ";
                    for(String id:ids){
                        sql += " lg.material = '"+id+"' or";
                    }
                    sql = sql.substring(0,sql.length()-2);
                }else{
                    sql += " and lg.material = '"+material+"'";
                }
            }
            if(!StringUtils.isEmpty(lowprice)){
                sql += " and lg.productprice >= "+new BigDecimal(lowprice);
            }
            if(!StringUtils.isEmpty(topprice)){
                sql += " and lg.productprice <= "+new BigDecimal(topprice);
            }
            if("1".equals(sorttype)){
                if("up".equals(sortway)){
                    sql += " order by lg.productprice  ";
                }else{
                    sql += " order by lg.productprice  desc";
                }
            }else if("2".equals(sorttype)){
                if("up".equals(sortway)){
                    sql += " order by lg.create_at  ";
                }else{
                    sql += " order by lg.create_at  desc";
                }
            }else{
                sql += " order by lg.sort  desc";
            }
            System.out.println(sql);
            return sql;
        }

        public String findInfoByName(String name) {
            String sql = "SELECT " +
                    " lg.thumb as goodimg, " +
                    " lg.thumbs as goodimgs, " +
                    " lg.id as goodid, " +
                    " lg.productprice as price," +
                    " lg.title as name , " +
                    " lg.type as type , " +
                    " lm.isauction as isauction ," +
                    " lm.isdirect as isdirect ," +
                    " lm.isquality as isquality ," +
                    " lm.postage as postage ," +
                    " lm.refund as refund ," +
                    " lm.isoem as isoem ," +
                    " lg.stepprice as stepprice ," +
                    " lg.bidsnum as bidsnum" +
                    " from lm_goods lg left join lm_merch_info lm on lg.mer_id = lm.id " +
                    " left join lm_goods_categories lgc on lg.category_id = lgc.id  where lg.state = 1 ";
            if(!StringUtils.isEmpty(name)){
                sql += " and lg.title like '%"+name+"%'";
            }
            return sql;
        }


    }
}