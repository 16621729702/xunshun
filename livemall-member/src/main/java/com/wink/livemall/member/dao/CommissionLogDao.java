package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.CommissionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CommissionLogDao extends tk.mybatis.mapper.common.Mapper<CommissionLog>{

    /**
     * 查询
     * @param gainer
     * @return
     */
    @Select("SELECT * FROM commission_log WHERE gainer = #{gainer} order by create_time desc")
    List<CommissionLog> findListByGainer(@Param("gainer")int gainer);


}
