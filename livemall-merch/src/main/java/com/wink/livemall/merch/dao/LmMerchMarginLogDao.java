package com.wink.livemall.merch.dao;


import com.wink.livemall.merch.dto.LmMerchMarginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LmMerchMarginLogDao extends tk.mybatis.mapper.common.Mapper<LmMerchMarginLog>{



    @Select("SELECT *  FROM lm_merch_margin_log  where  mer_id = #{merId} and type = #{type}  and state = 1 order by create_time desc  ")
   List<LmMerchMarginLog>  findByMerId(@Param("merId")int merId, @Param("type")int type);

    @Select("SELECT *  FROM lm_merch_margin_log  where  mer_id = #{merId}  and state = 1 order by create_time desc  ")
    List<LmMerchMarginLog>  findByMerIdAll(@Param("merId")int merId);

    @Select("SELECT *  FROM lm_merch_margin_log  where  margin_sn = #{marginSn}  order by create_time desc limit 1 ")
    LmMerchMarginLog findByMarginSn(@Param("marginSn")String marginSn);

    @Select("SELECT *  FROM lm_merch_margin_log  where  id = #{id}  ")
    LmMerchMarginLog findByID(@Param("id")int id);


    @Select("SELECT *  FROM lm_merch_margin_log  where  violate = 1  and state = 1 order by create_time desc  ")
    List<LmMerchMarginLog>  findViolateAll();
}
