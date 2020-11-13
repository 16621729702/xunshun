package com.wink.livemall.merch.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

import java.util.Date;

@Table(name = "lm_merch_info")
public class LmMerchInfo {
	// 正常
	public static int active = 1;
	// 封号
	public static int inactive = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private String avatar;
	private String store_name;
	private String description;
	private String weixin;
	private int merchno;
	private String mobile;
	private String bg_image;
	private java.util.Date create_at;
	private java.util.Date update_at;
	private int member_id;
	private int state;
	private int isrecommend;
	private String label;
	private int categoryid;
	private BigDecimal margin;
	private int focusnum;
	private int step;
	private int businessid;
	private int sellid;
	private String realname;
	private String idcard;
	private String business_license;
	private String bank_account;
	private String idcard_front;
	private String idcard_back;
	private String idcard_hold;
	private Date limit_time;
	private Double score;
	private int levelid;
	private int defaults_open;
	private int defaults_num;
	private int refund_open;
	private int refund_num;
	private int level_open;
	private String link;
	private int isstep;
	private BigDecimal deposit;
	private int autodeduct;
	private int isauction;// 滴雨轩拍卖行
	private int isdirect;// 直营店
	private int isquality;// 优选好店
	private int postage;// 包邮
	private int refund;// 包退
	private int isoem;// 代工工作室
	private int isopen;// 是否开启商品库 0否 1是
	private int type;// 类型 1店铺2优店3直播店4合买定制店
	private int islive;// 能否直播 0否 1是
	private int ischipped;// 能否合买 0否 1是
	private String refund_address;//退货地址
	private String refund_mobile; //退货电话
	private String refund_link;//退货联系人
	private Double goodper;//好评率
	private Double successper;//成交率
	private Double backper;//退款率
	private BigDecimal credit;//商户月

	public String getRefund_mobile() {
		return refund_mobile;
	}

	public void setRefund_mobile(String refund_mobile) {
		this.refund_mobile = refund_mobile;
	}

	public String getRefund_link() {
		return refund_link;
	}

	public void setRefund_link(String refund_link) {
		this.refund_link = refund_link;
	}

	public String getRefund_address() {
		return refund_address;
	}

	public void setRefund_address(String refund_address) {
		this.refund_address = refund_address;
	}

	public int getIslive() {
		return islive;
	}

	public void setIslive(int islive) {
		this.islive = islive;
	}

	public int getIschipped() {
		return ischipped;
	}

	public void setIschipped(int ischipped) {
		this.ischipped = ischipped;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsopen() {
		return isopen;
	}

	public void setIsopen(int isopen) {
		this.isopen = isopen;
	}

	public int getIsauction() {
		return isauction;
	}

	public void setIsauction(int isauction) {
		this.isauction = isauction;
	}

	public int getIsdirect() {
		return isdirect;
	}

	public void setIsdirect(int isdirect) {
		this.isdirect = isdirect;
	}

	public int getIsquality() {
		return isquality;
	}

	public void setIsquality(int isquality) {
		this.isquality = isquality;
	}

	public int getPostage() {
		return postage;
	}

	public void setPostage(int postage) {
		this.postage = postage;
	}

	public int getRefund() {
		return refund;
	}

	public void setRefund(int refund) {
		this.refund = refund;
	}

	public int getIsoem() {
		return isoem;
	}

	public void setIsoem(int isoem) {
		this.isoem = isoem;
	}

	public int getAutodeduct() {
		return autodeduct;
	}

	public void setAutodeduct(int autodeduct) {
		this.autodeduct = autodeduct;
	}

	public int getIsstep() {
		return isstep;
	}

	public void setIsstep(int isstep) {
		this.isstep = isstep;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(Date limit_time) {
		this.limit_time = limit_time;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public int getLevelid() {
		return levelid;
	}

	public void setLevelid(int levelid) {
		this.levelid = levelid;
	}

	public int getDefaults_open() {
		return defaults_open;
	}

	public void setDefaults_open(int defaults_open) {
		this.defaults_open = defaults_open;
	}

	public int getDefaults_num() {
		return defaults_num;
	}

	public void setDefaults_num(int defaults_num) {
		this.defaults_num = defaults_num;
	}

	public int getRefund_open() {
		return refund_open;
	}

	public void setRefund_open(int refund_open) {
		this.refund_open = refund_open;
	}

	public int getRefund_num() {
		return refund_num;
	}

	public void setRefund_num(int refund_num) {
		this.refund_num = refund_num;
	}

	public int getLevel_open() {
		return level_open;
	}

	public void setLevel_open(int level_open) {
		this.level_open = level_open;
	}

	public int getBusinessid() {
		return businessid;
	}

	public void setBusinessid(int businessid) {
		this.businessid = businessid;
	}

	public int getSellid() {
		return sellid;
	}

	public void setSellid(int sellid) {
		this.sellid = sellid;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getBusiness_license() {
		return business_license;
	}

	public void setBusiness_license(String business_license) {
		this.business_license = business_license;
	}

	public String getBank_account() {
		return bank_account;
	}

	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public LmMerchInfo() {

	}

	public static int getActive() {
		return active;
	}

	public static void setActive(int active) {
		LmMerchInfo.active = active;
	}

	public static int getInactive() {
		return inactive;
	}

	public static void setInactive(int inactive) {
		LmMerchInfo.inactive = inactive;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBg_image() {
		return bg_image;
	}

	public void setBg_image(String bg_image) {
		this.bg_image = bg_image;
	}

	public Date getCreate_at() {
		return create_at;
	}

	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}

	public Date getUpdate_at() {
		return update_at;
	}

	public void setUpdate_at(Date update_at) {
		this.update_at = update_at;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getIsrecommend() {
		return isrecommend;
	}

	public void setIsrecommend(int isrecommend) {
		this.isrecommend = isrecommend;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	public BigDecimal getMargin() {
		return margin;
	}

	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}

	public int getFocusnum() {
		return focusnum;
	}

	public void setFocusnum(int focusnum) {
		this.focusnum = focusnum;
	}

	public static String parsetochinesetype(int type){
		if(type==1){
			return "普通店铺";
		}
		if(type==2){
			return "优选店铺";
		}
		if(type==3){
			return "直播店铺";
		}
		if(type==4){
			return "合买店铺";
		}
		return "";
	}

	public Double getGoodper() {
		return goodper;
	}

	public void setGoodper(Double goodper) {
		this.goodper = goodper;
	}

	public Double getSuccessper() {
		return successper;
	}

	public void setSuccessper(Double successper) {
		this.successper = successper;
	}

	public Double getBackper() {
		return backper;
	}

	public void setBackper(Double backper) {
		this.backper = backper;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public String getIdcard_front() {
		return idcard_front;
	}

	public void setIdcard_front(String idcard_front) {
		this.idcard_front = idcard_front;
	}

	public String getIdcard_back() {
		return idcard_back;
	}

	public void setIdcard_back(String idcard_back) {
		this.idcard_back = idcard_back;
	}

	public String getIdcard_hold() {
		return idcard_hold;
	}

	public void setIdcard_hold(String idcard_hold) {
		this.idcard_hold = idcard_hold;
	}

	public int getMerchno() {
		return merchno;
	}

	public void setMerchno(int merchno) {
		this.merchno = merchno;
	}
}
