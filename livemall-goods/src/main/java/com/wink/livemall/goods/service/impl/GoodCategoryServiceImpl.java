package com.wink.livemall.goods.service.impl;


import com.wink.livemall.goods.dao.GoodCategoryDao;
import com.wink.livemall.goods.dto.GoodCategory;
import com.wink.livemall.goods.dto.Menu;
import com.wink.livemall.goods.service.GoodCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.awt.*;
import java.util.*;
import java.util.List;


@Service
public class GoodCategoryServiceImpl implements GoodCategoryService {

	@Resource
	private GoodCategoryDao goodCategoryDao;

	/**
	 * 根据name查询
	 * @param name
	 * @return
	 */
	@Override
	public GoodCategory selectGoodCategoryByName(String name) {
		return goodCategoryDao.findGoodCategoryByName(name);
	}

	/**
	 * 查询所有
	 * @return
	 */
	@Override
	public List<GoodCategory> selectAll() {
		return goodCategoryDao.selectAll();
	}

	/**
	 * 根据id删除
	 * @param id
	 */
	@Override
	public void deleteService(int id) {
		goodCategoryDao.deleteByPrimaryKey(id);
	}

	/**
	 * 查询商品类型 + 分页
	 * @param condient
	 * @return
	 */
	@Override
	public List<Map>  findGoodCategoryByCondient(Map<String, String> condient) {
		List<GoodCategory> list = goodCategoryDao.findGoodCategoryByCondient(condient);
		List<GoodCategory> toplist = goodCategoryDao.findtopgoodscategory();

		List<Map> returnlist = new ArrayList<>();
		for(GoodCategory category:list){
			Map<String,String> map = new HashMap<>();
			map.put("id",category.getId()+"");
			map.put("pid",category.getParent_id()+"");
			map.put("name",category.getName());
			map.put("pic",category.getPic());
			if(category.getParent_id()==0){
				map.put("pname","顶级菜单");
				map.put("merchshow",category.getMerchshow()+"");
			}else{
				for(GoodCategory pcategory:toplist){
					if(pcategory.getId() == category.getParent_id()){
						map.put("pname",pcategory.getName());
					}
				}
				map.put("merchshow",category.getMerchshow()+"");
				map.put("isrecommend",category.getIsrecommend()+"");
			}
			map.put("sort",category.getSort()+"");
			map.put("isshow",category.getIsshow()+"");
			returnlist.add(map);
		}
		return returnlist;
	}

	/**
	 */
	@Override
	public void addgoodcategory(GoodCategory goodCategory) {
		goodCategoryDao.insert(goodCategory);
	}

	/**
	 * 查询所有顶级分类
	 * @return
	 */
	@Override
	public List<GoodCategory> findtopgoodscategory() {
		return goodCategoryDao.findtopgoodscategory();
	}

	@Override
	public GoodCategory findgoodscategoryById(String id) {
		if(StringUtils.isEmpty(id)){
			return null;
		}else{
			return goodCategoryDao.findgoodscategoryById(Integer.parseInt(id));
		}

	}

	@Override
	public void editgoodcategory(GoodCategory goodCategory) {
		goodCategoryDao.updateByPrimaryKey(goodCategory);
	}

	@Override
	public List<GoodCategory> findActiveList() {
		return goodCategoryDao.findActiveList();
	}

	@Override
	public List<Menu> returnMenuTree(List<GoodCategory> list, List<com.wink.livemall.goods.dto.Menu> menulist) {

		if (list.size() > 0) {
			int len = list.size();
			for (int i = 0; i < len; i++) {
				GoodCategory cate = list.get(i);
				if (cate.getParent_id() == 0) {
					List<Menu> child = getChild(list, cate.getId());
					Menu menu = new Menu();
					menu.setId(list.get(i).getId());
					menu.setHome_pic(list.get(i).getHome_pic());
					menu.setName(list.get(i).getName());
					menu.setPic(list.get(i).getPic());
					menu.setChildren(child);
					menulist.add(menu);
				}
			}

		}
		return menulist;
	}

	@Override
	public List<Menu> getChild(List<GoodCategory> list, int parentid) {
		List<Menu> child = new ArrayList<Menu>();
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getParent_id() == parentid) {
				Menu menu = new Menu();
				menu.setId(list.get(i).getId());
				menu.setHome_pic(list.get(i).getHome_pic());
				menu.setName(list.get(i).getName());
				menu.setPic(list.get(i).getPic());
				menu.setParent_id(parentid);
				child.add(menu);
			}
		}

		return child;
	}


	@Override
	public List<GoodCategory> findByPid(String pid) {
		return goodCategoryDao.findByPid(Integer.parseInt(pid));
	}

	@Override
	public List<GoodCategory> findrecommend() {
		return goodCategoryDao.findrecommend();
	}


	/*-----------------------------app查询接口--------------------------------------------------------*/
	@Override
	public List<Map<String, String>> findtopgoodscategorybyapi() {
		return goodCategoryDao.findtopgoodscategorybyapi();
	}

	@Override
	public List<Map<String, String>> findrecommendByapi() {
		return goodCategoryDao.findrecommendByapi();
	}

	@Override
	public List<Map<String, String>> findByPidByapi(String pid) {
		return goodCategoryDao.findByPidByapi(Integer.parseInt(pid));
	}


}
