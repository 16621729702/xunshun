package com.wink.livemall.sys.code.dao;

import com.wink.livemall.sys.code.dto.LmSmsVcode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LmSmsVcodeDao extends tk.mybatis.mapper.common.Mapper<LmSmsVcode>{

    @Select("SELECT * FROM lm_sms_vcode WHERE mobile = #{mobile} order by creatdate desc")
    List<LmSmsVcode> findByMobile(@Param("mobile") String mobile);

}
