package com.wink.livemall.order.dao;

import com.wink.livemall.order.dto.LmOrderComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmOrderCommentDao extends tk.mybatis.mapper.common.Mapper<LmOrderComment>{

    @SelectProvider(type = sqlprovider.class, method = "findByMerchId")
    List<Map<String, Object>> findByMerchId(@Param("merchid") int merchid);

    class sqlprovider {
        public String findByMerchId(@Param("merchid")int merchid) {
            String sql= "select " +
                    " lom.img as img," +
                    " lom.comment as comment, " +
                    " lom.score as score, " +
                    " lo.finishtime as finishtime," +
                    " lm.avatar as avatar, " +
                    " lm.nickname as nickname " +
                    " from lm_order_comment lom , lm_orders lo, lm_member lm  where lom.order_id = lo.id and lo.memberid = lm.id and lom.merchid = #{merchid}";

            return sql;
        }
    }
}
