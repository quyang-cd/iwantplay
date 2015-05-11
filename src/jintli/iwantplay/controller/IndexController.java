package jintli.iwantplay.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value="/index")
public class IndexController extends BaseController{
	Logger logger = Logger.getLogger(IndexController.class);
	public IndexController() {
		logger.info("---------------IndexController");
	}
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request,String openid) throws Exception {
		
		ModelAndView mv = new ModelAndView("home");
		String islogin = isLogin(request);
		logger.info("当前登录与否：" + islogin);
		logger.info("当前登录人：" + getUsername(request));
		mv.addObject("islogin",islogin);
		mv.addObject("openid",openid);
		// 登陆成功
		mv.setViewName("home");
		return mv;
	}
	
	@RequestMapping(value="/qcback", method=RequestMethod.GET)
	public ModelAndView qcback(HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView("home");
		mv.setViewName("qc_back");
		return mv;
	}
}
