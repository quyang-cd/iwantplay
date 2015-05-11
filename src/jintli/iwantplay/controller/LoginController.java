package jintli.iwantplay.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jintli.iwantplay.pojo.Admin;
import jintli.iwantplay.service.AdminService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping(value="/admin")
public class LoginController extends BaseController{
	Logger logger = Logger.getLogger(LoginController.class);
	public LoginController() {
		logger.info(" ------------ LoginController");
	}
	
	private AdminService service;

	@Resource(name="adminService")
	public void setService(AdminService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request,String urlAfterLogin,
			String aid) throws Exception {
		
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("urlAfterLogin",urlAfterLogin);
		mv.addObject("aid",aid);
		// 登陆成功
		mv.setViewName("login");
		return mv;
	}
	
	@RequestMapping(value="/loginOld", method=RequestMethod.POST, headers="Content-Type=application/x-www-form-urlencoded")
	public ModelAndView loginOld(HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView("login");
		
		// form data
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		if(password == null) {
			password = "";
		}
		
		Admin admin = service.getAdmin(name);
		
		if(admin == null) {
			mv.addObject("error", "登陆名不存在");
		} else if(!password.equals(admin.getPassword())) {
			mv.addObject("error", "密码不正确");
		} else if(!admin.isEffective()) {
			mv.addObject("error", "有效期已过");
		} else {
			// 登陆成功
			mv.setViewName("myPage");
			System.out.println("登陆成功" + admin.toString());
			service.successLogin("登陆操作", admin);
		}

		return mv;
	}
	
	@RequestMapping(value="/reg", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView reg(HttpServletRequest request,HttpServletResponse response,
			String username,String password) throws Exception {
		ModelAndView model = new ModelAndView();
		Admin hasAdmin = service.getAdmin(username);
		if(hasAdmin != null) {
			model.addObject("status", "1");
			return model;
		}
		Admin admin = new Admin();
		admin.setName(username);
		admin.setPassword(password);
		int result = service.saveAdmin(admin);
		//注册成功后进行登录操作
		if(result > 0) {
			doLogin(request, response, username, password);
			model.addObject("status", "0");
		} else {
			model.addObject("status", "1");
		}
        return model;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response,
			String username,String password) throws Exception {
		ModelAndView model = new ModelAndView();
		Admin admin = service.getAdmin(username);
		if(admin == null) {
			model.addObject("status", "3");
		} else if(!password.equals(admin.getPassword())) {
			model.addObject("status", "2");
		} else if(!admin.isEffective()) {
			model.addObject("status", "1");
		} else {
			doLogin(request, response, username, password);
			// 登陆成功
			model.addObject("status", "0");
		}
        return model;
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView logout(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		doLogout(request);
		//退出成功
		model.addObject("status", "0");
        return model;
	}
}
