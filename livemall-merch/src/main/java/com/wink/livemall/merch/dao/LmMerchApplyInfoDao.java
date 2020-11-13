package com.wink.livemall.merch.dao;

import com.wink.livemall.merch.dto.LmMerchApplyInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmMerchApplyInfoDao extends tk.mybatis.mapper.common.Mapper<LmMerchApplyInfo>{

    @Delete("DELETE from lm_merch_apply_info WHERE merch_id = #{id}")
    void deleteByMerchid(@Param("id") int merchid);


    @SelectProvider(type = LmMerchApplyInfoDaoprovider.class, method = "findListByCondient")
    List<LmMerchApplyInfo> findByCondient(Map<String, String> condient);

    class LmMerchApplyInfoDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_merch_apply_info where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("name"))){
                sql += " and true_name like '%"+condient.get("true_name")+"%'";
            }
            return sql;
        }

    }
}
