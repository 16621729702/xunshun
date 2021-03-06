package com.wink.livemall.goods.service;

import com.wink.livemall.goods.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface GoodService {

    /**
     * 根据条件查询商品
     * @param condient
     * @return
     */
    List<Map> findListByCondient(Map<String, String> condient);

    /**
     * 根据商品id删除商品信息
     * @param id
     */
    void delete(String id);

    /**
     * 根据id查询
     * @return
     */
    Good findById(int id);

    void updateGoods(Good good);

    List<Good> findAll();

    /**
     * 获取状态为开启
     * @return
     */
    List<Good> findGoodList();
    /**
     * 获取推荐商品列表
     * @return
     */
    List<Map> findRecommendList();

    /**
     * app获取最热商品
     * @return
     */
    List<Map> findHotList();


    List<Map<String,Object>> findByMerchIdByApi(Integer merchid);

    List<Map<String, Object>> findByMerchIdAndTypeByApi(int merchid, int type);

    /**
     * app获取商品材质
     * @return
     */
    List<LmGoodMaterial> getGoodMaterialByApi();

    List<Map> findInfoByApi(String categoryid, String goodname, String topprice, String lowprice, String isauction, String isstores, String isgoodshop, String ispackage, String isback, String isstudio, String material, String sorttype, String sortway,String type,String pid);

    List<Map<String, Object>> findhotByMerchIdByApi(int merchid);

    List<Map<String,Object>> findAuctionlistByGoodid(int id,int type);

    void updateAuctionService(LmGoodAuction lmGoodAuction);

    void insertAuctionService(LmGoodAuction lmGoodAuction);

    /**
     * 开始寻找拍卖结束未生成订单的拍品
     * @return
     */
    List<Good> findwaitGoodInfo(Date nowdate);


    /**
     * 开始寻找拍卖前15分钟订单
     * @return
     */
    List<Good> findwaitGoodInfo15(Date nowdate,Date olddate);

    List<LmGoodAuction> findAuctionlistByGoodid2(int goodid,int type);

    List<Map<String, Object>> findAuctionlistByUseridAndType(int userid, int type);

    List<Map<String, Object>> findAuctionlistByUseridAndType2(int userid, int type);

    void insertService(Good good);

    List<Map> findInfoByName(String name);

    List<LmGoodMaterial> findMaterlist();

    LmGoodMaterial findMaterById(int id);

    void insertMater(LmGoodMaterial material);

    void deletemater(int id);

    void updatemater(LmGoodMaterial material);

    List<Good> findAllwaitGoodInfo(Date nowdate);

    void insertShare(LmShareGood lmShareGood);

    LmShareGood findshareById(int goodid);


    void insertLivedGood(LivedGood livedGood);

    LivedGood findLivedGood(int goodid);

    void updateLivedGood(LivedGood good);

    void updateShareGood(LmShareGood good);

    List<LmShareGood> findShareGood(Date date);

    List<LivedGood>  findLivedGoodByLiveid(int liveid);

    List<LivedGood> findLiveGood(Date date);

	List<LmShareGood> findshareGoodByLiveid(String liveid);

    List<LivedGood> movementImLive(int liveid);

}