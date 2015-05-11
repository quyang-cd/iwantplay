package jintli.iwantplay.service;

import javax.annotation.Resource;

import jintli.iwantplay.dao.AdminDao;
import jintli.iwantplay.pojo.Admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("adminService")
public class AdminService {
	
	public AdminService() {
		System.out.println(" -------------AdminService ");
	}

	private AdminDao dao;
	
	@Resource(name="adminDao")
	public void setDao(AdminDao dao) {
		this.dao = dao;
	}

	public Admin getAdmin(String name) {
		return dao.getAdmin(name);
	}
	
	public int saveAdmin(Admin admin) {
		return dao.saveAdmin(admin);
	}
	
	// in this i use transaction
	@Transactional
	public void successLogin(String info, Admin admin) {
		
		dao.successLogin(info, admin);
		
	}
}
