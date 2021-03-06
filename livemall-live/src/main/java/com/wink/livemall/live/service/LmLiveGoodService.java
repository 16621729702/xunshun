package com.wink.livemall.live.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wink.livemall.live.dto.LmLiveGood;

public interface LmLiveGoodService {
	
	void addGood(LmLiveGood good);
	
	int checkGood(Map<String, String> params);
	
	void delGood(int id);
	
	List<Map<String, Object>> findPage(Map<String, String> params);

    List<Map<String, Object>> findByLiveIdByApi(int liveid);

    List<Map<String, Object>> findByLiveIdByApi(String liveid, String type);

	List<Map<String, Object>> findAllInfo();

	List<Map<String, Object>> findInfoByLiveidAndName(Map<String, String> condient);

    List<LmLiveGood> findByLiveid(String liveid);

	LmLiveGood checkGood(String good_id, String liveid);

	List<Map<String, Object>> findLiveGoodByLiveid(int liveid);

	List<Map<String, Object>> findLivegoodinfoById(String liveid, String type);

	List<Map<String, Object>> findLivegoodtomemberid(String liveid, String type, String userid);

	List<LmLiveGood> findlivegoodByGoodid(int id);

}


