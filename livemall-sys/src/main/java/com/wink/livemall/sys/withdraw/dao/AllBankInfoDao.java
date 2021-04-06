package com.wink.livemall.sys.withdraw.dao;


import com.wink.livemall.sys.withdraw.dto.AllBankInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AllBankInfoDao extends tk.mybatis.mapper.common.Mapper<AllBankInfo>{



    /**
     * 查询
     * @param code
     * @return
     */
    @Select("SELECT * FROM all_bank_info WHERE code = #{code} limit 1 ")
    AllBankInfo findListByCode(@Param("code")int code);

    /**
     * 查询所有
     * @return
     */
    @Select("SELECT * FROM all_bank_info ")
    List<AllBankInfo> findAll();
}
