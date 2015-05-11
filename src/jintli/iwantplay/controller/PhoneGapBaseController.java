package jintli.iwantplay.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jintli.iwantplay.support.EscapeUtils;

import com.sina.sae.memcached.SaeMemcache;

public abstract class PhoneGapBaseController {
	
	SaeMemcache getMySaeMemcache() {
		SaeMemcache mc = new SaeMemcache();
		mc.init();
		return mc;
	}
	
	void doLogin(String username,String password){
		/*String host = request.getServerName(); 
		Cookie cookie = new Cookie("SESSION_LOGIN_USERNAME", username); // 保存用户名到Cookie 
		cookie.setPath("/"); 
		cookie.setDomain(host); 
		cookie.setMaxAge(5*365*24*60*60); 
		response.addCookie(cookie); 
		cookie = new Cookie("SESSION_LOGIN_PASSWORD", password);
		cookie.setPath("/"); 
		cookie.setDomain(host); 
		cookie.setMaxAge(5*365*24*60*60); 
		response.addCookie(cookie);*/
		getMySaeMemcache().set(username + password, "1",60*60*24*7);
	}
	
	void doLogout(String username,String password){
		getMySaeMemcache().delete(username + password);
	}
	
	/**
	 * 判断用户是否登录
	 * @param request
	 * @return
	 */
	String isLogin(String username,String password) {
		if("1".equals(getMySaeMemcache().get(username + password))) {
			return "1";
		}
		return "-1";
	}
}
