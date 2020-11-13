package com.wink.livemall.order.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_orders")
public class LmOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private String orderid;
	private String status;
	private String paystatus;
	private String paynickname;
	private BigDecimal totalprice;
	private BigDecimal payexpressprice;
	private BigDecimal realexpressprice;
	private BigDecimal realpayprice;
	private java.util.Date createtime;
	private java.util.Date paytime;
	private java.util.Date deliverytime;
	private java.util.Date finishtime;
	private String chargename;
	private String chargephone;
	private String chargeaddress;
	private String promotename;
	private String usercomment;
	private String ordercomment;
	private int merchid;
	private int backstatus;
	private BigDecimal backprice;
	private int memberid;
	private BigDecimal prepay_money;
	private int deposit_type;
	private BigDecimal remain_money;
	private Date prepay_time;
	private Date remian_time;
	private int type;
	private int porderid;
	private int expressid;
	private String expresssn;
	private String express;
	private String expressname;
	private int islive; //是否直播间订单 0否1 是
	private int refundid;//退款信息id
	private int lots_log_id;//抽签记录id
	private int lots_log_no;//抽签编号
	private int lots_info_id;//开料结果id
	private int lots_status;//分料状态 0未分 1已分
	private Date chippedtime;//合买凑单成功时间
	private int commentstatus;//评价状态
	private int isprepay;//是否全款
	private int islivegood;//是否直播商品
	private String neworderid;

	public Date getChippedtime() {
		return chippedtime;
	}

	public void setChippedtime(Date chippedtime) {
		this.chippedtime = chippedtime;
	}

	public int getLots_status() {
		return lots_status;
	}

	public void setLots_status(int lots_status) {
		this.lots_status = lots_status;
	}

	public int getLots_log_id() {
		return lots_log_id;
	}

	public void setLots_log_id(int lots_log_id) {
		this.lots_log_id = lots_log_id;
	}

	public int getLots_log_no() {
		return lots_log_no;
	}

	public void setLots_log_no(int lots_log_no) {
		this.lots_log_no = lots_log_no;
	}

	public int getLots_info_id() {
		return lots_info_id;
	}

	public void setLots_info_id(int lots_info_id) {
		this.lots_info_id = lots_info_id;
	}

	public int getRefundid() {
		return refundid;
	}

	public void setRefundid(int refundid) {
		this.refundid = refundid;
	}

	public int getIslive() {
		return islive;
	}

	public void setIslive(int islive) {
		this.islive = islive;
	}

	public int getExpressid() {
		return expressid;
	}

	public void setExpressid(int expressid) {
		this.expressid = expressid;
	}

	public String getExpresssn() {
		return expresssn;
	}

	public void setExpresssn(String expresssn) {
		this.expresssn = expresssn;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getExpressname() {
		return expressname;
	}

	public void setExpressname(String expressname) {
		this.expressname = expressname;
	}

	public BigDecimal getPrepay_money() {
		return prepay_money;
	}

	public void setPrepay_money(BigDecimal prepay_money) {
		this.prepay_money = prepay_money;
	}

	public int getDeposit_type() {
		return deposit_type;
	}

	public void setDeposit_type(int deposit_type) {
		this.deposit_type = deposit_type;
	}

	public BigDecimal getRemain_money() {
		return remain_money;
	}

	public void setRemain_money(BigDecimal remain_money) {
		this.remain_money = remain_money;
	}

	public Date getPrepay_time() {
		return prepay_time;
	}

	public void setPrepay_time(Date prepay_time) {
		this.prepay_time = prepay_time;
	}

	public Date getRemian_time() {
		return remian_time;
	}

	public void setRemian_time(Date remian_time) {
		this.remian_time = remian_time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPorderid() {
		return porderid;
	}

	public String getNeworderid() {
		return neworderid;
	}

	public void setNeworderid(String neworderid) {
		this.neworderid = neworderid;
	}

	public void setPorderid(int porderid) {
		this.porderid = porderid;
	}

	public static String backstatuschangetochinese(Object paystatus) {
		if ("1".equals(paystatus)) {
			return "退款中";
		}
		if ("2".equals(paystatus)) {
			return "退款完成";
		}
		return "";
	}

	public static String paystatuschangetochinese(Object paystatus) {
		if ("0".equals(paystatus)) {
			return "支付宝支付";
		}
		if ("1".equals(paystatus)) {
			return "微信支付";
		}
		if ("2".equals(paystatus)) {
			return "云支付";
		}
		return "";
	}

	public static String statuschangetochinese(Object status) {
		if ("0".equals(status)) {
			return "未支付";
		}
		if ("1".equals(status)) {
			return "待发货";
		}
		if ("2".equals(status)) {
			return "待收货";
		}
		if ("3".equals(status)) {
			return "待评价";
		}
		if ("4".equals(status)) {
			return "已完成";
		}
		if ("5".equals(status)) {
			return "已关闭";
		}
		if ("-1".equals(status)) {
			return "订单失效";
		}
		return "";
	}

	public BigDecimal getBackprice() {
		return backprice;
	}

	public void setBackprice(BigDecimal backprice) {
		this.backprice = backprice;
	}

	public int getBackstatus() {
		return backstatus;
	}

	public void setBackstatus(int backstatus) {
		this.backstatus = backstatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}

	public String getPaynickname() {
		return paynickname;
	}

	public void setPaynickname(String paynickname) {
		this.paynickname = paynickname;
	}

	public BigDecimal getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(BigDecimal totalprice) {
		this.totalprice = totalprice;
	}

	public BigDecimal getPayexpressprice() {
		return payexpressprice;
	}

	public void setPayexpressprice(BigDecimal payexpressprice) {
		this.payexpressprice = payexpressprice;
	}

	public BigDecimal getRealpayprice() {
		return realpayprice;
	}

	public void setRealpayprice(BigDecimal realpayprice) {
		this.realpayprice = realpayprice;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public Date getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(Date deliverytime) {
		this.deliverytime = deliverytime;
	}

	public Date getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}

	public String getChargename() {
		return chargename;
	}

	public void setChargename(String chargename) {
		this.chargename = chargename;
	}

	public String getChargephone() {
		return chargephone;
	}

	public void setChargephone(String chargephone) {
		this.chargephone = chargephone;
	}

	public String getChargeaddress() {
		return chargeaddress;
	}

	public void setChargeaddress(String chargeaddress) {
		this.chargeaddress = chargeaddress;
	}

	public String getPromotename() {
		return promotename;
	}

	public void setPromotename(String promotename) {
		this.promotename = promotename;
	}

	public String getUsercomment() {
		return usercomment;
	}

	public void setUsercomment(String usercomment) {
		this.usercomment = usercomment;
	}

	public String getOrdercomment() {
		return ordercomment;
	}

	public void setOrdercomment(String ordercomment) {
		this.ordercomment = ordercomment;
	}

	public int getMerchid() {
		return merchid;
	}

	public BigDecimal getRealexpressprice() {
		return realexpressprice;
	}

	public void setRealexpressprice(BigDecimal realexpressprice) {
		this.realexpressprice = realexpressprice;
	}

	public void setMerchid(int merchid) {
		this.merchid = merchid;
	}

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public int getCommentstatus() {
		return commentstatus;
	}

	public void setCommentstatus(int commentstatus) {
		this.commentstatus = commentstatus;
	}

	public int getIsprepay() {
		return isprepay;
	}

	public void setIsprepay(int isprepay) {
		this.isprepay = isprepay;
	}

	public int getIslivegood() {
		return islivegood;
	}

	public void setIslivegood(int islivegood) {
		this.islivegood = islivegood;
	}
}
