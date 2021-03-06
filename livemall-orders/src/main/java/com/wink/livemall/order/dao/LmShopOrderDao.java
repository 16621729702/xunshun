package com.wink.livemall.order.dao;

import com.wink.livemall.order.dto.LmOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface LmShopOrderDao extends tk.mybatis.mapper.common.Mapper<LmOrder>{

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "findListByCondient")
    List<Map<String, Object>> findByCondient(Map<String, String> condient);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "findInfoById")
    Map<String, Object> findInfoById(int id);

    @Select("SELECT * FROM lm_orders WHERE orderid = #{orderid} ")
    LmOrder findByOrderId(@Param("orderid")String orderid);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "findListByTypeByApi")
    List<Map<String, Object>> findListByTypeByApi(@Param("status")String status,@Param("userid")int userid);

    @Select("SELECT paystatus as paystatus ,id as id ,status as status FROM lm_orders WHERE merchid = #{merchid}")
    List<Map<String, String>> findListByMerchidByApi(@Param("merchid")int merchid);

    @Select("SELECT lm.isgood as isgood ,lm.score as score FROM lm_order_comment lm,lm_orders lo WHERE lm.order_id = lo.id and lo.merchid = #{merchid}")
    List<Map<String, String>> findCommentinfiByMerchidByApi(@Param("merchid")int merchid);

    @Select("SELECT id FROM lm_orders order by id desc limit 0,1")
    Integer findMaxId();

    @Select("SELECT * FROM lm_orders where id = #{id} ")
    LmOrder findOrderById(@Param("id")int id);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "ordersize")
    Integer ordersize(@Param("status") int status, @Param("userid") int userid);

    @Select("SELECT lo.* FROM lm_orders lo,lm_order_goods log where lo.id = log.orderid and lo.merchid = #{merchid} and log.goodid = #{goodid} and lo.porderid = 0 and lo.type = 3 order by id desc limit 0,1")
    LmOrder findTopOrder(@Param("goodid")int goodid,@Param("merchid") int merchid);

    @Select("SELECT * FROM lm_orders where porderid = #{id} and prepay_time is not null ")
    List<LmOrder> findOrderListByPid(@Param("id")int id);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "findMyshare")
    List<Map<String, Object>> findMyshare(@Param("status") int status, @Param("userid") int userid);


    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "findSharelist")
    List<Map<String, Object>> findSharelist(@Param("status") int status, @Param("liveid") int liveid, @Param("userid") int userid);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "findMyshare2")
    List<Map<String, Object>> findMyshare2(@Param("status")int status, @Param("merchid")int merchid);

    @Select("SELECT * FROM lm_orders where status = 2 and deliverytime < #{olddate}")
    List<LmOrder> findListByDate(@Param("olddate")Date olddate);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "findMerchshareList")
    List<Map<String, Object>> findMerchshareList(@Param("merchid")int merchid,@Param("type") String type);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "findOrderList")
    List<Map<String, Object>> findOrderList(@Param("status")String status,@Param("userid")int userid);

    @Select("SELECT * FROM lm_orders where merchid = #{merchid} and type = 3 and porderid = 0 and status >= 0")
    List<LmOrder> findShareOrderListByMerchid(@Param("merchid")int merchid);

    @Select("SELECT * FROM lm_orders where  type = 3 and porderid = 0 and lots_status=0 ")
    List<LmOrder> findTopOrderList();

    @Select("SELECT * FROM lm_orders where porderid = #{id} and status >= 1 ")
    List<LmOrder> findChildOrder(@Param("id")int id);

    @Select("SELECT * FROM lm_orders where porderid = #{id} and status >= 0 ")
    List<LmOrder> findOrderListByPid2(@Param("id")int id);

    @Select("SELECT * FROM lm_orders where  status = #{status} ")
    List<LmOrder> findOrderListByStatus(@Param("status")int status);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "findListByCondient2")
    List<Map<String, Object>> findByCondient2(Map<String, String> condient);

    @Select("SELECT * FROM lm_orders where  neworderid = #{neworderid} ")
    LmOrder findByNewOrderid(@Param("neworderid")String out_trade_no);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "violateOrderOne")
    List<LmOrder> violateOrderOne(@Param("type")int type);

    @SelectProvider(type = LmShopOrderDaoprovider.class, method = "violateOrderTwo")
    List<LmOrder> violateOrderTwo(@Param("type")int type);

    @Select("SELECT * FROM lm_orders where  merchid = #{merId} and  memberid = #{memberId}  and status >= 0 ")
    List<LmOrder> isBuy(@Param("merId")int merId,@Param("memberId")int memberId);

    @Select("SELECT * FROM lm_orders where  merchid = #{merId}  and (backstatus =0  or backstatus =3 )  and status > 2 and status < 5  order by createtime desc")
    List<LmOrder> merOrderPriceSum(@Param("merId")int merId);

    class LmShopOrderDaoprovider{

        public String violateOrderOne(int type) {
            StringBuilder sql = new StringBuilder();
            sql.append( "SELECT * FROM lm_orders  WHERE violate = 0 " );
            if(type==0){
                sql.append(" and  paystatus = 1 ");
            }else if(type==1){
                sql.append(" and  backstatus = 1");
            }
            return sql.toString();
        }


        public String violateOrderTwo(int type) {
            StringBuilder sql = new StringBuilder();
            sql.append( "SELECT * FROM lm_orders  WHERE violate = 2 " );
            if(type==0){
                sql.append(" and  paystatus = 1 ");
            }else if(type==1){
                sql.append(" and  backstatus = 1");
            }
            return sql.toString();
        }

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT " +
                    " lo.id as id, " +
                    " lo.orderid as orderid, " +
                    " lo.chargename as chargename, " +
                    " lo.chargephone as chargephone, " +
                    " lo.ordercomment as ordercomment, " +
                    " lg.title as goodname, " +
                    " log.goodprice as goodprice, " +
                    " log.goodnum as  goodnum, " +
                    " lg.thumb as  goodimg, " +
                    " lo.realpayprice as realpayprice, " +
                    " lo.paystatus as paystatus, " +
                    " lo.status as status, " +
                    " lo.paytime as paytime, " +
                    " lo.merchid as merchid, " +
                    " lo.createtime as createtime " +
                    " from lm_orders lo,lm_goods lg,lm_order_goods log where lo.id = log.orderid and lg.id = log.goodid and lo.type <> 3 ";
            if(!StringUtils.isEmpty(condient.get("orderid"))){
                sql += " and lo.orderid like '%"+condient.get("orderid")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("status"))){
                if(condient.get("status").equals("3")){
                    sql += " and lo.commentstatus = 0 and lo.status = 4";
                }else{
                    sql += " and lo.status = '"+condient.get("status")+"'";
                }
            }
            if(!StringUtils.isEmpty(condient.get("merchid"))){
                sql += " and lo.merchid = "+condient.get("merchid");
            }
            if(!StringUtils.isEmpty(condient.get("paystatus"))){
                sql += " and lo.paystatus = '"+condient.get("paystatus")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("backstatus"))){
                sql += " and lo.backstatus = '"+condient.get("backstatus")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("timetype"))){
                if("createtime".equals(condient.get("timetype"))){
                    if(!StringUtils.isEmpty(condient.get("startdate"))){
                        sql += " and lo.createtime >= '"+condient.get("startdate")+"'";
                    }
                    if(!StringUtils.isEmpty(condient.get("enddate"))){
                        sql += " and lo.createtime <= '"+condient.get("enddate")+"'";
                    }
                }
                if("paytime".equals(condient.get("timetype"))){
                    if(!StringUtils.isEmpty(condient.get("startdate"))){
                        sql += " and lo.paytime >= '"+condient.get("startdate")+"'";
                    }
                    if(!StringUtils.isEmpty(condient.get("enddate"))){
                        sql += " and lo.paytime <= '"+condient.get("enddate")+"'";
                    }
                }
                if("deliverytime".equals(condient.get("timetype"))){
                    if(!StringUtils.isEmpty(condient.get("startdate"))){
                        sql += " and lo.deliverytime >= '"+condient.get("startdate")+"'";
                    }
                    if(!StringUtils.isEmpty(condient.get("enddate"))){
                        sql += " and lo.deliverytime <= '"+condient.get("enddate")+"'";
                    }
                }
                if("finishtime".equals(condient.get("timetype"))){
                    if(!StringUtils.isEmpty(condient.get("startdate"))){
                        sql += " and lo.finishtime >= '"+condient.get("startdate")+"'";
                    }
                    if(!StringUtils.isEmpty(condient.get("enddate"))){
                        sql += " and lo.finishtime <= '"+condient.get("enddate")+"'";
                    }
                }
            }else {
                if(!StringUtils.isEmpty(condient.get("startdate"))){
                    sql += " and lo.createtime >= '"+condient.get("startdate")+"'";
                }
                if(!StringUtils.isEmpty(condient.get("enddate"))){
                    sql += " and lo.createtime <= '"+condient.get("enddate")+"'";
                }
            }
            sql+=" order by lo.createtime desc ";
            return sql;
        }
        //????????????
        public String findListByCondient2(Map<String, String> condient) {
            String sql = "SELECT " +
                    " lo.id as id, " +
                    " lo.orderid as orderid, " +
                    " lo.chargename as chargename, " +
                    " lo.chargephone as chargephone, " +
                    " lo.ordercomment as ordercomment, " +
                    " lg.name as goodname, " +
                    " log.goodprice as goodprice, " +
                    " log.goodnum as  goodnum, " +
                    " lg.img as  goodimg, " +
                    " lo.realpayprice as realpayprice, " +
                    " lo.paystatus as paystatus, " +
                    " lo.status as status, " +
                    " lo.paytime as paytime, " +
                    " lo.merchid as merchid, " +
                    " lo.createtime as createtime " +
                    " from lm_orders lo,lm_share_good lg,lm_order_goods log where lo.id = log.orderid and lg.id = log.goodid and lo.type = 3 and lo.porderid != 0 ";
            if(!StringUtils.isEmpty(condient.get("orderid"))){
                sql += " and lo.orderid like '%"+condient.get("orderid")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("status"))){
                if(condient.get("status").equals("3")){
                    sql += " and lo.commentstatus = 0 and lo.status = 4";
                }else{
                    sql += " and lo.status = '"+condient.get("status")+"'";
                }
            }
            if(!StringUtils.isEmpty(condient.get("merchid"))){
                sql += " and lo.merchid = "+condient.get("merchid");
            }
            if(!StringUtils.isEmpty(condient.get("paystatus"))){
                sql += " and lo.paystatus = '"+condient.get("paystatus")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("backstatus"))){
                sql += " and lo.backstatus = '"+condient.get("backstatus")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("timetype"))){
                if("createtime".equals(condient.get("timetype"))){
                    if(!StringUtils.isEmpty(condient.get("startdate"))){
                        sql += " and lo.createtime >= '"+condient.get("startdate")+"'";
                    }
                    if(!StringUtils.isEmpty(condient.get("enddate"))){
                        sql += " and lo.createtime <= '"+condient.get("enddate")+"'";
                    }
                }
                if("paytime".equals(condient.get("timetype"))){
                    if(!StringUtils.isEmpty(condient.get("startdate"))){
                        sql += " and lo.paytime >= '"+condient.get("startdate")+"'";
                    }
                    if(!StringUtils.isEmpty(condient.get("enddate"))){
                        sql += " and lo.paytime <= '"+condient.get("enddate")+"'";
                    }
                }
                if("deliverytime".equals(condient.get("timetype"))){
                    if(!StringUtils.isEmpty(condient.get("startdate"))){
                        sql += " and lo.deliverytime >= '"+condient.get("startdate")+"'";
                    }
                    if(!StringUtils.isEmpty(condient.get("enddate"))){
                        sql += " and lo.deliverytime <= '"+condient.get("enddate")+"'";
                    }
                }
                if("finishtime".equals(condient.get("timetype"))){
                    if(!StringUtils.isEmpty(condient.get("startdate"))){
                        sql += " and lo.finishtime >= '"+condient.get("startdate")+"'";
                    }
                    if(!StringUtils.isEmpty(condient.get("enddate"))){
                        sql += " and lo.finishtime <= '"+condient.get("enddate")+"'";
                    }
                }
            }
            sql+=" order by lo.createtime desc ";
            return sql;
        }

        public String findInfoById(Integer id) {
            String sql = "SELECT " +
                    " lo.id as id, " +
                    " lo.orderid as orderid, " +
                    " lo.chargename as chargename, " +
                    " lo.promotename as promotename, " +
                    " lo.chargephone as chargephone, " +
                    " lo.usercomment as usercomment, " +
                    " lo.ordercomment as ordercomment, " +
                    " lg.title as goodname, " +
                    " log.goodprice as goodprice, " +
                    " log.goodnum as  goodnum, " +
                    " lg.thumb as  goodimg, " +
                    " lo.realpayprice as realpayprice, " +
                    " lo.paystatus as paystatus, " +
                    " lo.realexpressprice as realexpressprice, " +
                    " lo.paynickname as paynickname, " +
                    " lo.totalprice as totalprice, " +
                    " lo.status as status, " +
                    " lo.chargeaddress as chargeaddress, " +
                    " lo.merchid as merchid, " +
                    " lo.deliverytime as deliverytime, " +
                    " lo.finishtime as finishtime, " +
                    " lo.paytime as paytime, " +
                    " lo.createtime as createtime " +
                    " from lm_orders lo,lm_goods lg,lm_order_goods log where lo.id = log.orderid and lg.id = log.goodid " +
                    " and lo.id = "+id;
            return sql;
        }



        public String findOrderList(@Param("status")String status,@Param("userid")int userid) {
            String sql = "SELECT " +
                    " lm.store_name as store_name," +
                    " lm.avatar as avatar," +
                    " lo.backprice as backprice," +
                    " lo.backstatus as backstatus," +
                    " lo.refundid as refundid," +
                    " lo.type as type," +
                    " lo.violate as violate," +
                    " lo.delay as delay," +
                    " lo.totalprice as totalprice," +
                    " lo.realpayprice as realpayprice," +
                    " lo.islivegood as islivegood," +
                    " lo.commentstatus as commentstatus," +
                    " lm.isauction as isauction ," +
                    " lm.isdirect as isdirect ," +
                    " lm.isquality as isquality ," +
                    " lm.postage as postage ," +
                    " lm.refund as refund ," +
                    " lm.isoem as isoem ," +
                    " lo.status as status," +
                    " lo.merchid as merchid," +
                    " lo.id as id," +
                    " lo.orderid as orderid," +
                    " log.goodid as goodid," +
                    " log.goodprice as goodprice," +
                    " log.goodnum as goodnum, " +
                    " lo.chargeaddress as chargeaddress," +
                    " lo.chargephone as chargephone" +
                    " FROM lm_orders lo,lm_order_goods log,lm_merch_info lm " +
                    " WHERE lo.merchid = lm.id " +
                    " AND lo.id = log.orderid " +
                    " AND lo.memberid=#{userid} ";
            if(!"-2".equals(status)&&!"6".equals(status)){
                if("3".equals(status)){
                    //?????????
                    sql+=" and lo.commentstatus= 0 and (lo.backstatus=0 or lo.backstatus=3)  and lo.status = 3 ";
                }else{
                    sql+=" and lo.status=#{status} and (lo.backstatus=0 or lo.backstatus=3) ";
                }
            }
            if("6".equals(status)){
                sql+=" and lo.backstatus > 0 and lo.backstatus < 3  ";
            }
            sql+=" order by lo.createtime desc";
            return sql;
        }
        public String ordersize(@Param("status")int status,@Param("userid")int userid) {
            String sql = "SELECT count(*)" +
                    " FROM lm_orders lo" +
                    " WHERE lo.memberid=#{userid} " ;
            if(-2!=status&&6!=status){
                if(3==status){
                    //?????????
                    sql+=" and lo.commentstatus= 0 and  (lo.backstatus=0 or lo.backstatus=3) and lo.status = 3 ";
                }else{
                    sql+=" and lo.status=#{status} and  (lo.backstatus=0 or lo.backstatus=3) ";
                }
            }
            if(6==status){
                sql+=" and lo.backstatus > 0  and lo.backstatus < 5 ";
            }
            return sql;
        }


        public String findListByTypeByApi(@Param("status")String status,@Param("userid")int userid) {
            String sql = "SELECT " +
                    " lm.store_name as store_name," +
                    " lm.avatar as avatar," +
                    " lg.title as goodname," +
                    " lg.thumb as thumb," +
                    " lo.backprice as backprice," +
                    " lo.backstatus as backstatus," +
                    " lo.refundid as refundid," +
                    " lg.spec as spec," +
                    " lo.commentstatus as commentstatus," +
                    " lm.isauction as isauction ," +
                    " lm.isdirect as isdirect ," +
                    " lm.isquality as isquality ," +
                    " lm.postage as postage ," +
                    " lm.refund as refund ," +
                    " lm.isoem as isoem ," +
                    " lo.status as status," +
                    " lo.id as id," +
                    " lo.orderid as orderid," +
                    " log.goodprice as goodprice," +
                    " log.goodnum as goodnum  " +
                    " FROM lm_orders lo,lm_order_goods log,lm_merch_info lm,lm_goods lg  " +
                    " WHERE lo.merchid = lm.id " +
                    " AND lo.id = log.orderid " +
                    " AND log.goodid = lg.id " +
                    " AND lo.memberid=#{userid} ";
            if(!"-2".equals(status)&&!"6".equals(status)){
                if("3".equals(status)){
                    //?????????
                    sql+=" and lo.commentstatus= 0 and (lo.backstatus=0 or lo.backstatus=3) and lo.status = 3 ";
                }else{
                    sql+=" and lo.status=#{status} and (lo.backstatus=0 or lo.backstatus=3) ";
                }

            }
            if("6".equals(status)){
                sql+=" and lo.backstatus > 0 and lo.backstatus <5 ";
            }
            sql+=" order by lo.createtime desc";
            return sql;
        }




        public String findMerchshareList(@Param("merchid")int merchid,@Param("type")String type) {
            String sql = "SELECT " +
                    " lm.store_name as store_name," +
                    " lg.name as goodname," +
                    " lg.img as thumb," +
                    " lg.id as goodid," +
                    " lg.price as price," +
                    " lg.material as material," +
                    " lm.id as merchid," +
                    " lo.status as status," +
                    " lo.lots_status as lots_status," +
                    " lo.id as id," +
                    " lo.chippedtime as chippedtime," +
                    " lo.orderid as orderid" +
                    " FROM lm_orders lo,lm_order_goods log,lm_merch_info lm,lm_share_good lg " +
                    " WHERE lo.merchid = lm.id " +
                    " AND lo.id = log.orderid " +
                    " AND log.goodid = lg.id " +
                    " AND lo.merchid=#{merchid} "+
                    " AND lo.type=3" +
                    " AND lo.porderid=0 ";
            if("2".equals(type)){
                sql+=" AND lo.status = '4' ";
            }else{
                sql+=" AND lo.status = '0' ";
            }
            sql += " order by lo.createtime desc ";
            return sql;
        }

        public String findMyshare(@Param("status")int status,@Param("userid")int userid) {
            String sql = "SELECT DISTINCT " +
                    " lm.store_name as store_name," +
                    " lg.name as goodname," +
                    " lg.img as thumb," +
                    " lg.material as material," +
                    " lm.id as merchid," +
                    " lm.member_id as merchimid," +
                    " lo.status as status," +
                    " lo.id as id," +
                    " lo.memberid as imid," +
                    " lo.chippedtime as chippedtime," +
                    " lo.orderid as orderid,";
                    if(status==2){
                        sql+=" lt.lot_no as lot_no,";
                    }
                    sql+=" log.goodprice as goodprice," +
                    " log.goodnum as goodnum  " +
                    " FROM lm_orders lo," +
                    "lm_order_goods log," +
                    "lm_merch_info lm," +
                    "lm_share_good lg";
                    if(status==2){
                        sql+=",lm_lots_log lt ";
                    }
                    sql+=" WHERE lo.merchid = lm.id " +
                    " AND lo.id = log.orderid ";
                    if(status==2){
                       sql+= " AND lt.goodsid = lg.id ";
                    }
                    sql+=" AND log.goodid = lg.id " +
                    " AND lo.memberid=#{userid} ";
                    if(status==2){
                        sql+=" AND lt.memberid=#{userid} ";
                    }
                    sql+=" AND lo.type=3 ";
                    if(status==2){
                        sql+=" AND lo.lots_log_no > 0 ";
                    }
                    if(status==1){
                        sql+=" AND lo.lots_log_no = 0 and lo.status >= 0 ";
                    }
                    sql+=" order by lo.id desc ";
            System.out.println(sql);
            return sql;
        }

        public String findMyshare2(@Param("status")int status,@Param("merchid")int merchid) {
            String sql = "SELECT " +
                    " lm.store_name as store_name," +
                    " lg.name as goodname," +
                    " lg.img as thumb," +
                    " lm.id as merchid," +
                    " lo.status as status," +
                    " lo.id as id," +
                    " lo.chippedtime as chippedtime," +
                    " lo.orderid as orderid," +
                    " lt.lot_no as lot_no," +
                    " log.goodprice as goodprice," +
                    " log.goodnum as goodnum  " +
                    " FROM lm_orders lo,lm_order_goods log,lm_merch_info lm,lm_share_good lg,lm_lots_log lt " +
                    " WHERE lo.merchid = lm.id " +
                    " AND lo.id = log.orderid " +
                    " AND lt.goodsid = lg.id " +
                    " AND log.goodid = lg.id " +
                    " AND lm.id=#{merchid} "+
                    " AND lo.type=3 ";
            if(status==2){
                sql+="AND lo.lots_log_id > 0";
            }
            return sql;
        }

        public String findSharelist(@Param("status")int status,@Param("liveid")int liveid,@Param("userid")int userid) {
            String sql = "SELECT " +
                    " lg.name as goodname," +
                    " lg.img as thumb," +
                    " lg.id as id," +
                    " lg.chipped_num as chipped_num," +
                    " lg.chipped_price as chipped_price " +
                    " FROM lm_share_good lg,lm_orders lo ,lm_order_goods log " +
                    " WHERE lg.id = log.goodid and lo.id = log.orderid and lg.liveid = #{liveid} and lg.status = 0 and lo.memberid = #{userid} ";
            //??????1?????????2?????????
            if(status==1){
                sql+=" and lo.status = 0";
            }
            if(status==2){
                sql+=" and lo.status > 0 and lo.lots_log_no = 0 ";
            }
            sql +=" order by lo.id desc ";
            return sql;
        }
    }
}
