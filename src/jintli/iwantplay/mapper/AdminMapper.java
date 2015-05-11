package jintli.iwantplay.mapper;

import java.util.HashMap;

import jintli.iwantplay.pojo.Admin;


public interface AdminMapper {
	
	// 查询登陆信息在DB中是否存在
	Admin getAdmin(String name);
	
	// 登陆成功后添加登陆LOG
	int addLog(HashMap<String, Object> paramMap);
	
	// 新增Admin
	int saveAdmin(Admin admin);
	
	// 登陆成功后 更新最近一次登陆时间
	int updateLastLoginTime(Admin admin);
	
}
