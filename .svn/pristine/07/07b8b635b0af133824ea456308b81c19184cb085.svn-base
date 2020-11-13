package com.wink.livemall.goods.service;

import com.wink.livemall.goods.dto.GoodCategory;
import com.wink.livemall.goods.dto.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


public interface GoodCategoryService {
	
	
	 
	    /**
	     * 根据名字查找商品类型
	     */
	    GoodCategory selectGoodCategoryByName(String name) ;

	    /**
	     * 查找所有用户
	     */
	    List<GoodCategory> selectAll() ;

	    /**
	     * 根据id 删除用户
	     */
	    void deleteService(int id) ;

	/**
	 * 根据参数动态查询信息
	 * @param condient
	 * @return
	 */
	List<Map>  findGoodCategoryByCondient(Map<String, String> condient);

	/**
	 * 新增商品类型信息
	 */
	void addgoodcategory(GoodCategory goodCategory);

	/**
	 * 查询所有顶级分类
	 * @return
	 */
	List<GoodCategory> findtopgoodscategory();

	/**
	 * 根据id查询分类
	 * @param id
	 * @return
	 */
	GoodCategory findgoodscategoryById(String id);

	/**
	 * 修改分类信息
	 */
	void editgoodcategory(GoodCategory goodCategory);

	/**
	 * 查询所有启用的分类
	 * @return
	 */
    List<GoodCategory> findActiveList();


	List<GoodCategory> findByPid(String pid);

	/**
	 * 查询推荐子分类
	 * @return
	 */
	List<GoodCategory> findrecommend();

	/**
	 * 查询所有顶级分类
	 * @return
	 */
	List<Map<String,String>> findtopgoodscategorybyapi();

	List<Map<String, String>> findrecommendByapi();

	List<Map<String, String>> findByPidByapi(String pid);
}