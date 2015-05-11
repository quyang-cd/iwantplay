package jintli.iwantplay.dao;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import jintli.iwantplay.mapper.AdminMapper;
import jintli.iwantplay.pojo.Admin;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;


@Repository("adminDao")
public class AdminDao {

	public AdminDao () {
		System.out.println(" ------------- AdminDao");
	}

	private SqlSession session; // 如果比较简单的场合 直接用注入的Mapper即可,但需要得到Connection , 则注入一个session就启到作用了

	@Resource
	public void setSession(SqlSession session) {
		this.session = session;
	}
	
	private AdminMapper mapper;
	
	@Resource
	public void setMapper(AdminMapper mapper) {
		this.mapper = mapper;
	}

	public Admin getAdmin(String name) {
		Admin admin = mapper.getAdmin(name);
		return admin;
	}
	
	public int saveAdmin(Admin admin) {
		return mapper.saveAdmin(admin);
	}

	public void successLogin(String info, Admin admin) {
		
		// insert1
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("info", info);
		paramMap.put("time", new Date());
		mapper.addLog(paramMap);
		
		// insert2
		admin.setLastlogintime(new Date());
		mapper.updateLastLoginTime(admin);
	} 
	
}
