package com.wink.livemall.order.dao;

import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderLog;
import com.wink.livemall.order.dto.LmOrderRefundLog;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.omg.CORBA.Request;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

@Mapper
public interface LmMerchOrderDao extends tk.mybatis.mapper.common.Mapper<LmOrder> {
	
	@Select("SELECT count(*) FROM lm_orders WHERE merchid = #{merchid} and status=1")
    int countPay(@Param("merchid")String merchid);
	
	@Select("SELECT count(*) FROM lm_orders WHERE merchid = #{merchid} and backstatus=1")
    int countRefund(@Param("merchid")String merchid);

	
	@SelectProvider(type = sqlprovider.class, method = "findChildrenOrder")
	List<Map<String, Object>> findChildrenOrder(Map<String, String> params);

	@SelectProvider(type = sqlprovider.class, method = "findInfoById")
	Map<String, Object> findByOrderid(String orderid);

	@SelectProvider(type = sqlprovider.class, method = "findListByCondition")
	List<Map<String, Object>> findListByCondition(Map<String, String> params);

	@SelectProvider(type = sqlprovider.class, method = "getpagesql")
	List<Map<String, Object>> getpage(Map<String, String> params);

	@SelectProvider(type = sqlprovider.class, method = "updateByFields")
	void updateByFields(List<Map<String, String>> params, String id);

	@SelectProvider(type = sqlprovider.class, method = "staticAll")
	Map<String, Object> staticAll(String merchid);
	
	@SelectProvider(type = sqlprovider.class, method = "staticOrderOk")
	Map<String, Object> staticOrderOk(String merchid);

	@SelectProvider(type = sqlprovider.class, method = "staticOrderPay")
	Map<String, Object> staticOrderPay(String merchid);

	@SelectProvider(type = sqlprovider.class, method = "staticOrderRefund")
	Map<String, Object> staticOrderRefund(String merchid,int backStatus);

	@SelectProvider(type = sqlprovider.class, method = "staticOrderEarn")
	Map<String, Object> staticOrderEarn(String merchid,int status);

	@SelectProvider(type = sqlprovider.class, method = "orderOkList")
	List<Map<String, Object>> orderOkList(Map<String, String> params);

	@SelectProvider(type = sqlprovider.class, method = "orderPayList")
	List<Map<String, Object>> orderPayList(Map<String, String> params);

	@SelectProvider(type = sqlprovider.class, method = "orderRefundList")
	List<Map<String, Object>> orderRefundList(Map<String, String> params);

	@SelectProvider(type = sqlprovider.class, method = "orderEarnList")
	List<Map<String, Object>> orderEarnList(Map<String, String> params);

	@SelectProvider(type = sqlprovider.class, method = "staticLive")
	Map<String, Object> staticLive(String merchid);

	@SelectProvider(type = sqlprovider.class, method = "staticLiveNum")
	int staticLiveNum(String merchid);

	@SelectProvider(type = sqlprovider.class, method = "staticLiveList")
	Map<String, Object> staticLiveList(String merId, String startTime, String entTime);

	@SelectProvider(type = sqlprovider.class, method = "orderLiveList")
	List<Map<String, Object>> orderLiveList(Map<String, String> params);

	@SelectProvider(type = sqlprovider.class, method = "findChippedByGoodsId")
	Map<String, Object> findChippedByGoodsId(String goodsid);
	
	@SelectProvider(type = sqlprovider.class, method = "findChippedOrder")
	List<Map<String, Object>> findChippedOrder(String porderid,String order);
	
	@SelectProvider(type = sqlprovider.class, method = "staticOrderMonth")
	Map<String, Object> staticOrderMonth(String merchid,String type );


	@Select("SELECT * FROM lm_orders WHERE porderid = #{porderid}")
	List<LmOrder> findChippedOrder2(@Param("porderid") int porderid);

	@SelectProvider(type = sqlprovider.class, method = "orderLogList")
	Map<String, Object> orderLogList(String merId, Integer type, String startTime, String entTime);

    class sqlprovider {

		public String orderLogList(String merId, Integer type, String startTime, String entTime) {
			StringBuilder sql = new StringBuilder();
			sql.append("select IFNULL(sum(realpayprice),0) price,count(id) num from lm_orders  where merchid=" + merId);
			if(type==1) {
				//成拍
				sql.append(" and type=2 and status!=-1 ");
			}else if(type==2) {
				//已付款
				sql.append(" and status>0 and (backstatus=0 or backstatus=3) ");
			}else if(type==3) {
				//已退款
				sql.append(" and backstatus=2 ");
			}else if(type==4) {
				//已收款
				sql.append(" and status>2 and status<5 and  (backstatus=0 or backstatus=3) ");
			}
			sql.append(" and createtime between '" + startTime + "'" + " and " + "'" + entTime + "'");
			return sql.toString();
		}

		//type 1当月 2上月
		public String staticOrderMonth(String merchid,String type) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.MONTH, -1);
			c1.set(Calendar.DAY_OF_MONTH, 1);
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			String last_month_start=sf.format(c1.getTime());
			Calendar c2 = Calendar.getInstance();
			c2.set(Calendar.DAY_OF_MONTH, 0);
			c2.set(Calendar.HOUR_OF_DAY, 23);
			c2.set(Calendar.MINUTE, 59);
			c2.set(Calendar.SECOND, 59);
			String last_month_end=sf.format(c2.getTime());
			Calendar c3 = Calendar.getInstance();
			c3.set(Calendar.DAY_OF_MONTH, 1);
			c3.set(Calendar.HOUR_OF_DAY, 0);
			c3.set(Calendar.MINUTE, 0);
			c3.set(Calendar.SECOND, 0);
			String month_start=sf.format(c3.getTime());
			Calendar c4 = Calendar.getInstance();
			c4.set(Calendar.DAY_OF_MONTH, c4.getActualMaximum(Calendar.DAY_OF_MONTH));
			c4.set(Calendar.HOUR_OF_DAY, 23);
			c4.set(Calendar.MINUTE, 59);
			c4.set(Calendar.SECOND, 59);
			String month_end=sf.format(c4.getTime());
			
			StringBuilder sql = new StringBuilder();
			sql.append("select IFNULL(sum(realpayprice),0) price,count(id) num from lm_orders where merchid=" + merchid
					+ " and status=4 ");
			if(type.equals("1")) {
				sql.append(" and createtime between '" + month_start + "'" + " and " + "'" + month_end + "'");
			}else if(type.equals("2")) {
				sql.append(" and createtime between '" + last_month_start + "'" + " and " + "'" + last_month_end + "'");
			}
			
			return sql.toString();
		}
		
		
		public String findChippedOrder(String porderid,String order) {
			StringBuilder sql = new StringBuilder();
			sql.append(
					"select o.memberid,o.merchid,o.status,o.id orderid,m.nickname,o.lots_log_id,o.lots_log_no,o.lots_info_id,o.lots_status,m.mobile from  lm_orders o   left join lm_member m on o.memberid=m.id  where o.status>0 and o.porderid="
							+ porderid );
			if(order.equals("1")) {
				sql.append(" order by o.createtime desc ");
			}else if(order.equals("2")) {
				sql.append(" order by o.lots_log_no asc ");
			}
			
			System.out.println(sql.toString());
			return sql.toString();
		}
		
		public String findChippedByGoodsId(String goodsid) {
			StringBuilder sql = new StringBuilder();
			sql.append(
					"select o.chippedtime," +
							"g.chipped_num," +
							"o.memberid," +
							"o.merchid," +
							"o.status," +
							"o.id orderid," +
							"o.lots_info_id," +
							"o.realpayprice," +
							"g.img " +
							"from lm_order_goods og " +
							"left join lm_orders o on og.orderid=o.id " +
							"left join lm_share_good g on og.goodid=g.id  " +
							"where  og.goodid="
							+ goodsid+" and o.type = 3 and o.porderid = 0 limit 1" );
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String staticLive(String merchid) {
			StringBuilder sql = new StringBuilder();
			String date_start = calc_date(-1);
			String date_end = calc_date(0);
			sql.append("select IFNULL(sum(realpayprice),0) price,count(id) num from lm_orders where merchid=" + merchid
					+ " and islivegood=1 and status>0 ");
			sql.append(" and createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");
			return sql.toString();
		}

		public String staticLiveNum(String merchid) {
			StringBuilder sql = new StringBuilder();
			String date_start = calc_date(-1);
			String date_end = calc_date(0);
			sql.append("select count(id) from lm_live_log where merchid=" + merchid
					+ "  and status=2 and endtime!='NULL' ");
			sql.append(" and starttime between '" + date_start + "'" + " and " + "'" + date_end + "'");
			return sql.toString();
		}

		public String staticLiveList(String merId, String startTime, String entTime) {
			StringBuilder sql = new StringBuilder();
			sql.append("select IFNULL(sum(realpayprice),0) price,count(id) num from lm_orders where merchid=" + merId
					+ " and islivegood=1 and status>0 ");
			sql.append(" and createtime between '" + startTime + "'" + " and " + "'" + entTime + "'");
			return sql.toString();
		}

		public String staticAll(String merchid) {
			StringBuilder sql = new StringBuilder();
//			String date_start = calc_date(-1);
//			String date_end = calc_date(0);
			sql.append("select IFNULL(sum(realpayprice),0) price,count(id) num from lm_orders where merchid=" + merchid
					+ " and status>0 ");
//			sql.append(" and createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");

			return sql.toString();
		}

		public String orderLiveList(Map<String, String> params) {
			StringBuilder sql = new StringBuilder();
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");

			String date_start = calc_date(-1);
			String date_end = calc_date(0);
			sql.append(
					"select o.realpayprice,o.id,o.status,o.backstatus,m.nickname,m.avatar,g.thumb,og.goodprice,og.goodnum,g.spec from lm_orders o left join lm_member m on o.memberid=m.id ");
			sql.append(" left join lm_order_goods og on og.orderid=o.id  ");
			sql.append(" left join lm_goods g on og.goodid=g.id  ");
			sql.append(" where o.merchid=" + params.get("merchid"));
			sql.append(" and o.createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");
			sql.append(" and o.status>0 and islive=1 ");
			sql.append(" order by o.createtime desc ");

			sql.append(" limit " + (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize) + ","
					+ Integer.parseInt(pagesize));
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String staticOrderEarn(String merchid,int status) {
			StringBuilder sql = new StringBuilder();
			String date_start = calc_date(0);
			String date_end = calc_date(1);
			sql.append("select IFNULL(sum(lo.realpayprice),0) price,count(lo.id) num from lm_orders lo ");
					if(status==3){
						sql.append(" left join lm_order_refund_log log on log.orderid=lo.id ");
					}
					sql.append("where lo.merchid=" + merchid);
					if(status==1){
						//待收货
						sql.append(" and lo.status=1  and  (lo.backstatus=0 or lo.backstatus=3) ");
					}else if(status==2){
						//待发货
						sql.append(" and lo.status=2 and  (lo.backstatus=0 or lo.backstatus=3) ");
					}else if(status==3){
						//退货中
						sql.append(" and lo.backstatus=1 and log.type = 2 ");
					}else if(status==4){
						//已收款
						sql.append(" and lo.status>2 and lo.status< 5 and  (lo.backstatus=0 or lo.backstatus=3) ");
					}

			sql.append(" and lo.createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");

			return sql.toString();
		}

		public String orderEarnList(Map<String, String> params) {
			StringBuilder sql = new StringBuilder();
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");

			String date_start = calc_date(0);
			String date_end = calc_date(1);
			sql.append(
					"select o.realpayprice,o.id,o.status,o.backstatus,m.nickname,m.avatar,g.thumb,og.goodprice,og.goodnum,g.spec from lm_orders o left join lm_member m on o.memberid=m.id ");
			sql.append(" left join lm_order_goods og on og.orderid=o.id  ");
			sql.append(" left join lm_goods g on og.goodid=g.id  ");
			sql.append(" where o.merchid=" + params.get("merchid"));
			sql.append(" and o.createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");
			sql.append(" and o.status=4 ");
			sql.append(" order by o.createtime desc ");

			sql.append(" limit " + (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize) + ","
					+ Integer.parseInt(pagesize));
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String staticOrderRefund(String merchid,int backStatus) {
			StringBuilder sql = new StringBuilder();
			String date_start = calc_date(0);
			String date_end = calc_date(1);
			sql.append("select IFNULL(sum(realpayprice),0) price,count(id) num from lm_orders where merchid=" + merchid);
					if(backStatus==1){
						//退款中
						sql.append(" and backstatus=1 ");
					}else if(backStatus==2){
						//退款完成
						sql.append(" and backstatus=2 ");
					}else {
						//已收款
						sql.append(" and  (backstatus=0 or backstatus=3)  and status > 2 and status <5 ");
					}
			sql.append(" and createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");

			return sql.toString();
		}

		public String orderRefundList(Map<String, String> params) {
			StringBuilder sql = new StringBuilder();
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");

			String date_start = calc_date(0);
			String date_end = calc_date(1);
			sql.append(
					"select o.realpayprice,o.id,o.status,o.backstatus,m.nickname,m.avatar,g.thumb,og.goodprice,og.goodnum,g.spec from lm_orders o left join lm_member m on o.memberid=m.id ");
			sql.append(" left join lm_order_goods og on og.orderid=o.id  ");
			sql.append(" left join lm_goods g on og.goodid=g.id  ");
			sql.append(" where o.merchid=" + params.get("merchid"));
			sql.append(" and o.createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");
			sql.append(" and  (o.backstatus=0 or o.backstatus=3) ");
			sql.append(" order by o.createtime desc ");

			sql.append(" limit " + (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize) + ","
					+ Integer.parseInt(pagesize));
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String staticOrderPay(String merchid) {
			StringBuilder sql = new StringBuilder();
			String date_start = calc_date(0);
			String date_end = calc_date(1);
			sql.append("select IFNULL(sum(realpayprice),0) price,count(id) num from lm_orders where merchid=" + merchid
					+ " and status>0 and (backstatus=0 or backstatus=3)  ");
			sql.append(" and createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");

			return sql.toString();
		}

		public String orderPayList(Map<String, String> params) {
			StringBuilder sql = new StringBuilder();
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");

			String date_start = calc_date(0);
			String date_end = calc_date(1);
			sql.append(
					"select o.realpayprice,o.id,o.status,o.backstatus,m.nickname,m.avatar,g.thumb,og.goodprice,og.goodnum,g.spec from lm_orders o left join lm_member m on o.memberid=m.id ");
			sql.append(" left join lm_order_goods og on og.orderid=o.id  ");
			sql.append(" left join lm_goods g on og.goodid=g.id  ");
			sql.append(" where o.merchid=" + params.get("merchid"));
			sql.append(" and o.createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");
			sql.append(" and o.status>0 and (o.backstatus=0 or o.backstatus=3) ");
			sql.append(" order by o.createtime desc ");

			sql.append(" limit " + (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize) + ","
					+ Integer.parseInt(pagesize));
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String staticOrderOk(String merchid) {
			StringBuilder sql = new StringBuilder();
			String date_start = calc_date(0);
			String date_end = calc_date(1);
			sql.append("select IFNULL(sum(realpayprice),0) price,count(id) num from lm_orders where merchid=" + merchid
					+ " and type=2 and status!=-1");
			sql.append(" and createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");

			return sql.toString();
		}

		public String orderOkList(Map<String, String> params) {
			StringBuilder sql = new StringBuilder();
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");

			String date_start = calc_date(0);
			String date_end = calc_date(1);
			sql.append(
					"select o.realpayprice,o.id,o.status,o.backstatus,m.nickname,m.avatar,g.thumb,og.goodprice,og.goodnum,g.spec from lm_orders o left join lm_member m on o.memberid=m.id ");
			sql.append(" left join lm_order_goods og on og.orderid=o.id  ");
			sql.append(" left join lm_goods g on og.goodid=g.id  ");
			sql.append(" where o.merchid=" + params.get("merchid"));
			sql.append(" and o.createtime between '" + date_start + "'" + " and " + "'" + date_end + "'");
			sql.append(" and o.type=2 and o.status!=-1 ");
			sql.append(" order by o.createtime desc ");

			sql.append(" limit " + (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize) + ","
					+ Integer.parseInt(pagesize));
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String updateByFields(List<Map<String, String>> params, String id) {
			StringBuilder sql = new StringBuilder();
			sql.append("update lm_orders set ");
			for (int i = 0; i < params.size(); i++) {
				Map<String, String> m = params.get(i);
				String field = m.get("field");
				String value = m.get("value");
				if (i == params.size() - 1) {
					sql.append("" + field + "='" + value + "'");

				} else {
					sql.append("" + field + "='" + value + "',");

				}

			}
			System.out.println(sql.toString());
			sql.append(" where id=" + id);
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String getpagesql(Map<String, String> params) {
			StringBuilder sql = new StringBuilder();
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");

			sql.append(
					"select o.orderid," +
							"o.id," +
							"o.status," +
							"o.backstatus," +
							"o.createtime," +
							"o.type," +
							"o.delay," +
							"o.porderid," +
							"m.nickname," +
							"o.realpayprice," +
							"m.avatar," +
							"og.goodprice," +
							"og.goodnum," +
							"o.islivegood," +
							"o.violate," +
							"og.goodid" +
							" from lm_orders o left join lm_member m on o.memberid=m.id ");
			sql.append(" left join lm_order_goods og on og.orderid=o.id  ");
			sql.append(" where o.totalprice is not null and o.merchid=" + params.get("merchid"));
			if (!StringUtils.isEmpty(params.get("type"))) {
				if (params.get("type").equals("1")) {// 待付款
					sql.append(" and o.status=0 and (o.backstatus=0 or o.backstatus=3) ");
				} else if (params.get("type").equals("2")) {// 待发货
					sql.append(" and o.status=1 and (o.backstatus=0 or o.backstatus=3) ");
				} else if (params.get("type").equals("3")) {// 待收货
					sql.append(" and o.status=2 and (o.backstatus=0 or o.backstatus=3) ");
				} else if (params.get("type").equals("4")) {// 售后
					sql.append(" and o.backstatus >0 ");
				} else if (params.get("type").equals("5")) {// 交易失败
					sql.append(" and o.status = -1 ");
				} else if (params.get("type").equals("6")) {// 已完成
					sql.append(" and  o.status> 2  and  o.status <5  and o.backstatus=0  ");
				}
			}
			sql.append(" order by o.createtime desc ");
			sql.append(" limit " + (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize) + ","
					+ Integer.parseInt(pagesize));
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String findChildrenOrder(Map<String, String> params) {
			StringBuilder sql = new StringBuilder();
			String pageindex = StringUtils.isEmpty(params.get("pageindex")) ? "1" : params.get("pageindex");
			String pagesize = StringUtils.isEmpty(params.get("pagesize")) ? "5" : params.get("pagesize");

			sql.append(
					"select o.id,o.status,o.backstatus,o.createtime,o.type,o.porderid,m.nickname,m.avatar,g.thumb,og.goodprice,og.goodnum,g.spec from lm_orders o left join lm_member m on o.memberid=m.id ");
			sql.append(" left join lm_order_goods og on og.orderid=o.id  ");
			sql.append(" left join lm_goods g on og.goodid=g.id  ");
			sql.append(" where  o.porderid=" + params.get("porderid"));
			sql.append(" order by o.createtime desc ");
			sql.append(" limit " + (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize) + ","
					+ Integer.parseInt(pagesize));
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String findInfoById(String orderid) {
			String sql = "SELECT " + " e.id as id, " + " e.orderid as orderid, " + " e.sendphone as sendphone, "
					+ " e.sendname as sendname, " + " e.expressorderid as expressorderid, "
					+ " e.deliverytype as deliverytype, " + " e.expressid as expressid, " + " ex.name as expressname "
					+ " from lm_order_express e left join lm_setting_expresses ex on e.expressid = ex.id "
					+ " where e.orderid = '" + orderid + "'";
			return sql;
		}

		public String findListByCondition(Map<String, String> params) {

			StringBuilder sql = new StringBuilder();
			sql.append(
					"select o.*,m.nickname,m.avatar,g.title,g.thumb from lm_orders o left join lm_member m on o.memberid=m.id left join lm_order_goods og on og.orderid=o.id left join lm_goods g on g.id=og.goodid ");

			if (!StringUtils.isEmpty(params.get("status"))) {
				String status = params.get("status");
				if (status.equals("0")) {
					sql.append(" and o.status=0 and (o.backstatus=0 or o.backstatus=3)  ");
				}
				if (status.equals("1")) {
					sql.append(" and o.status=1 and (o.backstatus=0 or o.backstatus=3)  ");
				}
				if (status.equals("2")) {
					sql.append(" and o.status=2 and (o.backstatus=0 or o.backstatus=3)  ");
				}
				if (status.equals("4")) {
					sql.append(" and o.backstatus>0 and o.backstatus<3 ");
				}
			}
			sql.append(" where o.merchid=" + Integer.parseInt(params.get("merchid")));
			sql.append(" order by o.createtime desc");

			return sql.toString();
		}
		

		private String calc_date(int day) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.DAY_OF_MONTH, day);
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			return sf.format(c1.getTime());
		}
	}
	
	public static void main(String[] args) {
	/*	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.MONTH, -1);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		String last_month_start=sf.format(c1.getTime());
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.DAY_OF_MONTH, 0);
		c2.set(Calendar.HOUR_OF_DAY, 23);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.SECOND, 59);
		String last_month_end=sf.format(c2.getTime());
		Calendar c3 = Calendar.getInstance();
		c3.set(Calendar.DAY_OF_MONTH, 1);
		c3.set(Calendar.HOUR_OF_DAY, 0);
		c3.set(Calendar.MINUTE, 0);
		c3.set(Calendar.SECOND, 0);
		String month_start=sf.format(c3.getTime());
		Calendar c4 = Calendar.getInstance();
		c4.set(Calendar.DAY_OF_MONTH, c4.getActualMaximum(Calendar.DAY_OF_MONTH));
		c4.set(Calendar.HOUR_OF_DAY, 23);
		c4.set(Calendar.MINUTE, 59);
		c4.set(Calendar.SECOND, 59);
		String month_end=sf.format(c4.getTime());*/
		
	}
	
}
