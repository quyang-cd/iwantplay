package jintli.iwantplay.controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jintli.iwantplay.pojo.Admin;
import jintli.iwantplay.service.AdminService;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping(value="/phonegap/admin")
public class PhoneGapLoginController extends PhoneGapBaseController {
	Logger logger = Logger.getLogger(PhoneGapLoginController.class);
	public PhoneGapLoginController() {
		logger.info(" ------------ PhoneGapLoginController");
	}
	
	private AdminService service;

	@Resource(name="adminService")
	public void setService(AdminService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/reg.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject reg(HttpServletRequest request,HttpServletResponse response,
			String username,String password,@RequestParam String callback) throws Exception {
		ModelAndView model = new ModelAndView();
		Admin hasAdmin = service.getAdmin(username);
		if(hasAdmin != null) {
			model.addObject("status", "1");
			return new JSONPObject(callback, model);
		}
		Admin admin = new Admin();
		admin.setName(username);
		admin.setPassword(password);
		int result = service.saveAdmin(admin);
		//注册成功后进行登录操作
		if(result > 0) {
			doLogin(username, password);
			model.addObject("status", "0");
		} else {
			model.addObject("status", "1");
		}
        return new JSONPObject(callback, model);
	}
	
	@RequestMapping(value="/login.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject login(HttpServletRequest request,HttpServletResponse response,
			String username,String password,@RequestParam String callback) throws Exception {
		ModelAndView model = new ModelAndView();
		Admin admin = service.getAdmin(username);
		if(admin == null) {
			model.addObject("status", "3");
		} else if(!password.equals(admin.getPassword())) {
			model.addObject("status", "2");
		} else if(!admin.isEffective()) {
			model.addObject("status", "1");
		} else {
			doLogin(username, password);
			// 登陆成功
			model.addObject("status", "0");
		}
        return new JSONPObject(callback, model);
	}
	
	@RequestMapping(value="/logout.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject logout(HttpServletRequest request,HttpServletResponse response,
			String username,String passwordMD5,@RequestParam String callback) throws Exception {
		ModelAndView model = new ModelAndView();
		doLogout(username,passwordMD5);
		//退出成功
		model.addObject("status", "0");
        return new JSONPObject(callback, model);
	}
}
