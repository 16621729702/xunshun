package com.wink.livemall.goods.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wink.livemall.goods.dto.User;
import com.wink.livemall.goods.dto.WareHouse;

import org.springframework.stereotype.Repository;

@Mapper
public interface MerchWareHouseDao {
   
    /**
     * 查询所有用可用库
     */
    @Select("SELECT * FROM lm_warehouse where status=1")
    List<WareHouse> findList();

   
}