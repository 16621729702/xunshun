package com.wink.livemall.order.dao;

import com.wink.livemall.order.dto.LmExpress;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmExpressDao extends tk.mybatis.mapper.common.Mapper<LmExpress> {

	@Select("select * from lm_express where status=1 ")
	List<LmExpress> getAll();

	@SelectProvider(type = LmExpressDao.sqlprovider.class, method = "findByCondient")
	List<LmExpress> findByCondient(Map<String, String> condient);


	class sqlprovider {

		public String findByCondient(Map<String, String> condient) {
			String sql = "select * from lm_express where 1=1 ";
			if(!StringUtils.isEmpty(condient.get("name"))){
				sql += " and name like '%"+condient.get("name")+"%'";
			}
			return sql;
		}
	}
}
