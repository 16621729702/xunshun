package com.wink.livemall.sys.withdraw.dao;


import com.wink.livemall.sys.withdraw.dto.Bank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BankDao extends tk.mybatis.mapper.common.Mapper<Bank>{


    /**
     * 查询该用户所有记录
     * @param user_id
     * @return
     */
    @Select("SELECT b.*,a.bank_name,a.logo FROM bank  b inner join all_bank_info a on b.bank_code = a.code WHERE b.user_id = #{user_id}  ORDER BY b.create_time desc ")
    List<Map<String,Object>> findListByUserId(@Param("user_id")int user_id);

    @Select("SELECT * FROM bank WHERE id = #{id} ")
    Bank findById(Integer id);
}
