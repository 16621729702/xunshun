package com.wink.livemall.goods.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
@Table(name = "lm_goods")
public class Good   {
    //一口价
    public static int typeone = 0;
    //拍卖
    public static int typetwo = 1;
    //下架
    public static int stateone = 0;
    //上架
    public static int statetwo = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private String category_id;
    private String keyword;
    private int mer_id;
    private int mer_use;
    private int buyway;
    private String title;
    private String subtitle;
    private String goodssn;
    private String unit;
    private int type;
    private BigDecimal productprice;
    private BigDecimal marketprice;
    private BigDecimal cost_price;
    private int buy_limit;
    private int  state;
    private BigDecimal expressprice;
    private int sort;
    private int browse_num;
    private int sale_num;
    private int ficti_num;
    private String description;
    private int stock;
    private String video;
    private String thumb;//缩略图1:1
    private String thumbs;//详情图4:3
    private int hasoption;
    private int is_free_freight;
    private Date create_at;
    private Date update_at;
    private Date delete_at;
    private BigDecimal startprice;//起始价格
    private BigDecimal stepprice;//加价价格
    private Date auction_start_time;//拍卖开始时间
    private Date auction_end_time;//结束时间
    private String label;//商品标签
    private int bidsnum;//出价次数
    private String material;//商品材料
    private int isrecommend;//是否被推荐 1推荐/0不推荐
    private int freeshipping;//是否包邮 0否 1是
    private String warehouse; //商品库
    private String place;//产地
    private String spec;//规格参数
    private String sn;//编号
    private int delaytime;//延时时间
    private String weight;//重量
    private int auction_status;//拍卖成交状态 1截拍 2流拍
    private int ischipped; //是否合买 0否1是
    private int chipped_num;//合买份数0否1是
    private BigDecimal chipped_price;//合买单价0否1是
    private int isdelete; //是否删除 1删除

    public int getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(int isdelete) {
        this.isdelete = isdelete;
    }

    public int getIschipped() {
		return ischipped;
	}

	public void setIschipped(int ischipped) {
		this.ischipped = ischipped;
	}

	public int getChipped_num() {
		return chipped_num;
	}

	public void setChipped_num(int chipped_num) {
		this.chipped_num = chipped_num;
	}

	public BigDecimal getChipped_price() {
		return chipped_price;
	}

	public void setChipped_price(BigDecimal chipped_price) {
		this.chipped_price = chipped_price;
	}

	public int getAuction_status() {
		return auction_status;
	}

	public void setAuction_status(int auction_status) {
		this.auction_status = auction_status;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public int getDelaytime() {
		return delaytime;
	}

	public void setDelaytime(int delaytime) {
		this.delaytime = delaytime;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public Good() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getBidsnum() {
        return bidsnum;
    }

    public void setBidsnum(int bidsnum) {
        this.bidsnum = bidsnum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getMer_id() {
        return mer_id;
    }

    public void setMer_id(int mer_id) {
        this.mer_id = mer_id;
    }

    public int getMer_use() {
        return mer_use;
    }

    public void setMer_use(int mer_use) {
        this.mer_use = mer_use;
    }

    public int getBuyway() {
        return buyway;
    }

    public void setBuyway(int buyway) {
        this.buyway = buyway;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getGoodssn() {
        return goodssn;
    }

    public void setGoodssn(String goodssn) {
        this.goodssn = goodssn;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getProductprice() {
        return productprice;
    }

    public void setProductprice(BigDecimal productprice) {
        this.productprice = productprice;
    }

    public BigDecimal getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(BigDecimal marketprice) {
        this.marketprice = marketprice;
    }

    public BigDecimal getCost_price() {
        return cost_price;
    }

    public void setCost_price(BigDecimal cost_price) {
        this.cost_price = cost_price;
    }

    public int getBuy_limit() {
        return buy_limit;
    }

    public void setBuy_limit(int buy_limit) {
        this.buy_limit = buy_limit;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public BigDecimal getExpressprice() {
        return expressprice;
    }

    public void setExpressprice(BigDecimal expressprice) {
        this.expressprice = expressprice;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getBrowse_num() {
        return browse_num;
    }

    public void setBrowse_num(int browse_num) {
        this.browse_num = browse_num;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public int getFicti_num() {
        return ficti_num;
    }

    public void setFicti_num(int ficti_num) {
        this.ficti_num = ficti_num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumbs() {
        return thumbs;
    }

    public void setThumbs(String thumbs) {
        this.thumbs = thumbs;
    }

    public int getHasoption() {
        return hasoption;
    }

    public void setHasoption(int hasoption) {
        this.hasoption = hasoption;
    }

    public int getIs_free_freight() {
        return is_free_freight;
    }

    public void setIs_free_freight(int is_free_freight) {
        this.is_free_freight = is_free_freight;
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

    public Date getDelete_at() {
        return delete_at;
    }

    public void setDelete_at(Date delete_at) {
        this.delete_at = delete_at;
    }

    public BigDecimal getStartprice() {
        return startprice;
    }

    public void setStartprice(BigDecimal startprice) {
        this.startprice = startprice;
    }

    public BigDecimal getStepprice() {
        return stepprice;
    }

    public void setStepprice(BigDecimal stepprice) {
        this.stepprice = stepprice;
    }

    public Date getAuction_start_time() {
        return auction_start_time;
    }

    public void setAuction_start_time(Date auction_start_time) {
        this.auction_start_time = auction_start_time;
    }

    public Date getAuction_end_time() {
        return auction_end_time;
    }

    public void setAuction_end_time(Date auction_end_time) {
        this.auction_end_time = auction_end_time;
    }

    public int getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(int isrecommend) {
        this.isrecommend = isrecommend;
    }

    public int getFreeshipping() {
        return freeshipping;
    }

    public void setFreeshipping(int freeshipping) {
        this.freeshipping = freeshipping;
    }


}
