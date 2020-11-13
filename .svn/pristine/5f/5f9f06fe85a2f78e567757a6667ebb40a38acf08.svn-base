package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.LmGoodComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmGoodCommentDao extends tk.mybatis.mapper.common.Mapper<LmGoodComment>{


    @SelectProvider(type = LmGoodCommentDaoprovider.class, method = "findListByCondient")
    List<Map<String,Object>> findByCondient(Map<String, String> condient);

    class LmGoodCommentDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT " +
                    " lc.id as id, " +
                    " lc.content as content, " +
                    " lc.isreplay as isreplay, " +
                    " lc.goodquality as goodquality, " +
                    " lc.serviceattitude as serviceattitude, " +
                    " lc.replaycontent as replaycontent, " +
                    " lc.img as img, " +
                    " lc.adminreplay as adminreplay, " +
                    " lc.replaydate as replaydate, " +
                    " lg.title as title, " +
                    " lm.nickname as nickname, " +
                    " lm.realname as realname, " +
                    " lm.mobile as mobile " +
                    " from lm_good_comment lc left join lm_goods lg on lc.goodid = lg.id " +
                    " left join lm_member lm on lc.userid = lm.id where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("goodname"))){
                sql += " and lg.title like '%"+condient.get("goodname")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("content"))){
                sql += " lc.content like '%"+condient.get("content")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("goodquality"))){
                sql += " lc.goodquality = "+condient.get("goodquality");
            }
            if(!StringUtils.isEmpty(condient.get("serviceattitude"))){
                sql += " lc.serviceattitude = "+condient.get("serviceattitude");
            }
            if(!StringUtils.isEmpty(condient.get("isreplay"))){
                sql += " lc.isreplay = "+condient.get("isreplay");
            }
            sql +=" order by lc.replaydate desc ";
            return sql;
        }

    }
}
