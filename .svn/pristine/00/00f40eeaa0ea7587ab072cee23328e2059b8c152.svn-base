package com.wink.livemall.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wink.livemall.goods.dao.UserDao;
import com.wink.livemall.goods.dto.User;

 
public interface UserService {
	
	
	 
	    /**
	     * 根据名字查找用户
	     */
	    public User selectUserByName(String name) ;

	    /**
	     * 查找所有用户
	     */
	    public List<User> selectAllUser() ;

	    /**
	     * 插入两个用户
	     */
	    public void insertService() ;

	    /**
	     * 根据id 删除用户
	     */

	    public void deleteService(int id) ;

	    /**
	     * 模拟事务。由于加上了 @Transactional注解，如果转账中途出了意外 SnailClimb 和 Daisy 的钱都不会改变。
	     */
	    @Transactional
	    public void changemoney() ;
}