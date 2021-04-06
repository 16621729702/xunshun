package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.ForwardUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ForwardUserDao extends tk.mybatis.mapper.common.Mapper<ForwardUser>{

    /**
     * 查询
     * @param userId
     * @return
     */
    @Select("SELECT * FROM forward_user WHERE user_id = #{userId} order by create_time desc ")
    ForwardUser findListByUserId(@Param("userId")int userId);

    /**
     * 查询
     * @param forwardId
     * @return
     */
    @Select("SELECT * FROM forward_user WHERE forward_id = #{forwardId} limit 1 ")
    List<ForwardUser> findListByForwardId(@Param("forwardId")int forwardId);

}
