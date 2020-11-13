package com.wink.livemall.help.dao;

import com.wink.livemall.help.dto.LmFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
@Mapper
public interface LmFeedbackDao extends tk.mybatis.mapper.common.Mapper<LmFeedback>{

    @SelectProvider(type = LmFeedbackDaoprovider.class, method = "findListByCondient")
    List<LmFeedback> findByCondient(Map<String, String> condient);


    class LmFeedbackDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_feedback where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("code"))){
                sql += " and type = '"+condient.get("type")+"'";
            }
            sql += " order by create_at desc ";
            return sql;
        }
    }
}
