package com.wink.livemall.admin.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.wink.livemall.goods.dto.Good;

import io.lettuce.core.dynamic.support.ReflectionUtils;

/**
 * @author Administrator
 * 验证必填字段
 */
public class VerifyFields {

	/**
	 * 验证字段是否有空值
	 * @param fields 验证字段
	 * @param request 请求传递参数
	 * @return error0 通过 1有空 -1 参数有误
	 */
	public Map<String, Object> verify(String[] fields,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String,Object>();
		System.out.println("----verifysingle");
		System.out.println(request);
		if(fields.length>0) {
			for (int i = 0; i < fields.length; i++) {
				String field=fields[i];
				if(StringUtils.isEmpty(request.getParameter(field))) {
					map.put("error",1);
					map.put("msg",field+"字段为空");
					return map;
				}
			}
			map.put("error", 0);
		}else {
			map.put("error", -1);
		}
		return map;
	}
	
	
	/**
	 *判断字段是否传值 将有传值的字段返回 field键 value值
	 * @param fields
	 */
	public List<Map<String, String>> checkEmptyField(String[] fields,HttpServletRequest request) {
		// TODO Auto-generated constructor stub
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		
		for (int i = 0; i < fields.length; i++) {
			if(!StringUtils.isEmpty(request.getParameter(fields[i]))) {
				Map<String, String> map=new HashMap<String, String>();
				map.put("field", fields[i]);
				map.put("value",request.getParameter(fields[i]));
				list.add(map);
			}
		}
		
		return list;
	}
	
	/**
	 * @return
	 * 去除空参数 返回实体对象 适用于 灵活修改多个不确定字段的操作或者操作同一数据库实体但是每次字段不同的接口的合集
	 * fields 需要判断和赋值的数据
	 * clazzName 要返回的实体类的包路径
	 */
	public Map< String, Object> checkEmptytoEntity(String[] fields,HttpServletRequest request,String clazzName){
		Map<String,Object> map=new HashMap<>();
		System.out.println("--checkEmptytoEntity");
		try {
			Class<?> clazz=Class.forName(clazzName);
			//dao实体
			Object entity=clazz.newInstance();
			
			SimpleDateFormat sf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			for (int i = 0; i < fields.length; i++) {
				String field=fields[i];
				//获取的参数
				String param=request.getParameter(field);
				//遍历 字段 判断字段是否有值   有值的情况下 获取字段的set方法 赋值给实体
				if(!StringUtils.isEmpty(param)) {
					//只要拿本类中的方法
					Method[] methods=clazz.getDeclaredMethods();
					for(Method m:methods) {
						String m_name=m.getName();
						//找到对应字段的赋值方法 转小写防止驼峰写法导致不匹配
						if(("set"+field).toLowerCase().equals(m_name.toLowerCase())) {
							//得到这个类的所有的属性 
							Field[] fs=clazz.getDeclaredFields();
							//判断实体的字段类型
							for(Field f:fs) {
								String name=f.getName();
								if(name.equals(field)) {
									String classtype=f.getType().toString();
									if(classtype.equals("int")) {
										int param_=Integer.parseInt(param);
										m.invoke(entity, param_);
									}else if(classtype.equals("class java.math.BigDecimal")) {
										BigDecimal param_=new BigDecimal(param);
										m.invoke(entity, param_);
									}else if(classtype.equals("class java.util.Date")) {
										Date param_=sf.parse(param);
										m.invoke(entity, param_);
									}else if(classtype.equals("class java.lang.String")) {
										m.invoke(entity, param);
									}
									break;
								}
								
							}
							break;
						}
					}
					
				}
			}
			
			map.put("entity", entity);
			map.put("error",0);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error",1);
			return map;
		}
		
	}
	
	/**
	 * @return
	 * 判断字段是否为空  都不为空返回实体对象 有空值则保错
	 * fields 需要判断和赋值的数据
	 * clazzName 要返回的实体类的包路径
	 */
	public Map< String, Object> verifytoEntity(String[] fields,HttpServletRequest request,String clazzName){
		Map<String,Object> map=new HashMap<>();
		System.out.println("--verifytoEntity");
		try {
			Class<?> clazz=Class.forName(clazzName);
			//dao实体
			Object entity=clazz.newInstance();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < fields.length; i++) {
				String field=fields[i];
				//获取的参数
				String param=request.getParameter(field);
				//遍历 字段 判断字段是否有值   有值的情况下 获取字段的set方法 赋值给实体
				if(!StringUtils.isEmpty(param)) {
					//只要拿本类中的方法
					Method[] methods=clazz.getDeclaredMethods();
					for(Method m:methods) {
						String m_name=m.getName();
						//找到对应字段的赋值方法 转小写防止驼峰写法导致不匹配
						if(("set"+field).toLowerCase().equals(m_name.toLowerCase())) {
							//得到这个类的所有的属性 
							Field[] fs=clazz.getDeclaredFields();
							//判断实体的字段类型
							for(Field f:fs) {
								String name=f.getName();
								if(name.equals(field)) {
									String classtype=f.getType().toString();
									if(classtype.equals("int")) {
										int param_=Integer.parseInt(param);
										m.invoke(entity, param_);
									}else if(classtype.equals("class java.math.BigDecimal")) {
										BigDecimal param_=new BigDecimal(param);
										m.invoke(entity, param_);
									}else if(classtype.equals("class java.util.Date")) {
										Date param_=sf.parse(param);
										m.invoke(entity, param_);
									}else if(classtype.equals("class java.lang.String")) {
										m.invoke(entity, param);
									}
									break;
								}
								
							}
							break;
						}
					}
					
				}else {
					map.put("error",1);
					map.put("msg","字段"+field+"为空");
					return map;
				}
			}
			
			map.put("entity", entity);
			map.put("error",0);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error",1);
			map.put("msg",e.getMessage());
			return map;
		}
		
	}
	
	
	public static void main(String[] args) {
		
	}
	

	
}
