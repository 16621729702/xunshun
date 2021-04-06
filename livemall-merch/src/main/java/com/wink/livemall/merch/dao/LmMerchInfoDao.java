package com.wink.livemall.merch.dao;

import com.mysql.fabric.xmlrpc.base.Params;
import com.wink.livemall.merch.dto.LmMerchApplyInfo;
import com.wink.livemall.merch.dto.LmMerchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mapper
public interface LmMerchInfoDao extends tk.mybatis.mapper.common.Mapper<LmMerchInfo>{

	
	@Select("select count(*) from lm_merch_info where id=#{id} and state=1")
	int checkMerchEnable(String id);

    @SelectProvider(type = LmMerchInfoDaoprovider.class, method = "findListByCondient")
    List<LmMerchInfo> findByCondient(Map<String, String> condient);
    @SelectProvider(type = LmMerchInfoDaoprovider.class, method = "findListByCondient2")
    List<Map<String, Object>> findByCondient2(Map<String, String> condient);

    @SelectProvider(type = LmMerchInfoDaoprovider.class, method = "findMerchInfoByCategoryByApi")
    List<Map<String, Object>> findMerchInfoByCategoryByApi(@Param("categoryid")int categoryid );

    @Select("SELECT store_name as name,avatar as img,description as description FROM lm_merch_info WHERE id = #{id}")
    Map<String, String> findMerchinfoByMerchidByApi(@Param("id")int merchid);

    @SelectProvider(type = LmMerchInfoDaoprovider.class, method = "updateByFields")
    List<LmMerchInfo> updateByFields(List<Map<String, String>> params,String id);

    @Select("SELECT  " +
            " lm.id as id," +
            " l.nickname as nickname," +
            " l.id as imid," +
            " lm.avatar as avatar," +
            " lm.store_name as store_name, " +
            " lm.isauction as isauction ," +
            " lm.isdirect as isdirect ," +
            " lm.isquality as isquality ," +
            " lm.postage as postage ," +
            " lm.refund as refund ," +
             " lm.bg_image as bg_image ," +
            " lm.categoryid as categoryid ," +
            " lm.isoem as isoem ," +
            " lm.margin as margin ," +
            " lm.focusnum as focusnum ," +
            " lm.description as description FROM lm_merch_info lm left join lm_member l on lm.member_id= l.id WHERE lm.id = #{id}")
    Map<String, Object> findInfoByIdByApd(@Param("id")int id);

    @Select("SELECT * FROM lm_merch_info WHERE member_id = #{memberid}")
    List<LmMerchInfo>  findByMemberid(@Param("memberid")int memberid);

    @Select("SELECT * FROM lm_merch_info WHERE isdirect = 1 limit 0,1")
    LmMerchInfo findDirectMerch();

    @SelectProvider(type = LmMerchInfoDaoprovider.class, method = "findMerchInfoByNameByApi")
    List<Map<String, Object>> findMerchInfoByNameByApi(String name);


    @Select("SELECT * FROM lm_merch_info WHERE state = 1 order by id desc")
    List<LmMerchInfo> findActiveMerch();

    @Select("SELECT id FROM lm_merch_info order by id desc limit 0,1 ")
    int findmaxno();

    @SelectProvider(type = LmMerchInfoDaoprovider.class, method = "isRepeat")
    List<Map<String, Object>> isRepeat(String store_name);

    class LmMerchInfoDaoprovider{
        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_merch_info where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("store_name"))){
                sql += " and store_name like '%"+condient.get("store_name")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("realname"))){
                sql += " and realname like '%"+condient.get("realname")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("state"))){
                sql += " and state =" +condient.get("state");
            }
            sql +=" order by id desc ";
            return sql;
        }

        public String findListByCondient2(Map<String, String> condient) {
            String sql = "SELECT " +
                    " lmc.name as categoryname, " +
                    " lb.name as businessname, " +
                    " ls.name as sellname, " +
                    " lm.* " +
                    " from lm_merch_info lm left join lm_merch_category lmc on  lm.categoryid = lmc.id " +
                    " left join lm_business_entity lb on lm.businessid = lb.id " +
                    " left join lm_sell_cate ls on lm.sellid = ls.id where lm.step = 2 ";
            if(!StringUtils.isEmpty(condient.get("realname"))){
                sql += " and lm.realname like '%"+condient.get("realname")+"%'";
            }
            sql += " order by lm.id desc ";

            return sql;
        }

        public String findMerchInfoByCategoryByApi(@Param("categoryid")int categoryid) {
           String sql = " SELECT " +
                   " lm.id as id," +
                   " lm.label as label," +
                   " lm.avatar as avatar," +
                   " lm.categoryid as categoryid," +
                   " lm.store_name as store_name," +
                   " lm.isauction as isauction ," +
                   " lm.isdirect as isdirect ," +
                   " lm.isquality as isquality ," +
                   " lm.postage as postage ," +
                   " lm.refund as refund ," +
                   " lm.isoem as isoem " +
                   " FROM lm_merch_info lm " +
                   " WHERE  lm.state = 1 ";
            if(categoryid==0){
                sql += " and lm.isrecommend = 1";
            }else if(categoryid==9){
                sql += " and lm.categoryid =2";
            }else{
                sql += " and lm.categoryid =#{categoryid}";
            }
            sql += "  order by lm.create_at desc ";
            return sql;
        }
        public String findMerchInfoByNameByApi(@Param("name")String name) {
            String sql = " SELECT " +
                    " lm.id as id," +
                    " lm.label as label," +
                    " lm.avatar as avatar," +
                    " lm.store_name as store_name," +
                    " lm.isauction as isauction ," +
                    " lm.isdirect as isdirect ," +
                    " lm.isquality as isquality ," +
                    " lm.postage as postage ," +
                    " lm.refund as refund ," +
                    " lm.isoem as isoem " +
                    " FROM lm_merch_info lm" +
                    " WHERE  lm.state = 1";
            if(!StringUtils.isEmpty(name)){
                sql += " and lm.store_name like '%"+name+"%'";
            }
            return sql;
        }

        public String updateByFields(List<Map<String, String>> params,String id) {
        	StringBuilder builder=new StringBuilder();
        	builder.append("update lm_merch_info set ");
        	for (int i = 0; i < params.size(); i++) {
				Map<String, String> m=params.get(i);
				String field=m.get("field");
				String value=m.get("value");
				if(i==params.size()-1) {
					builder.append(""+field+"='"+value+"'");
					
				}else {
					builder.append(""+field+"='"+value+"',");
					
				}
				
			}
        	
        	builder.append(" where id="+id);
        	System.out.println(builder.toString());
        	return builder.toString();
        }

        public String isRepeat(@Param("store_name")String store_name) {
            String sql = " SELECT " +
                    " lm.store_name as store_name" +
                    " FROM lm_merch_info lm"+
                    " WHERE lm.state = 1 ";
            if(!StringUtils.isEmpty(store_name)){
                sql += " and store_name like '"+store_name+"'";
            }
            return sql;
        }
        
    }
}
