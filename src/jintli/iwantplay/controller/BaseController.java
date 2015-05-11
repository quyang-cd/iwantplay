package jintli.iwantplay.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jintli.iwantplay.support.EscapeUtils;

import com.sina.sae.memcached.SaeMemcache;

public abstract class BaseController {
	
	SaeMemcache getMySaeMemcache() {
		SaeMemcache mc = new SaeMemcache();
		mc.init();
		return mc;
	}
	String getMySession(HttpServletRequest request,String key) {
		request.getSession(true);
		return (String) request.getSession().getAttribute(key);
	}
	
	void setMySession(HttpServletRequest request,String key,String value) {
		request.getSession(true);
		request.getSession().setAttribute(key, value);
	}
	
	void doLogin(HttpServletRequest request,HttpServletResponse response,
			String username,String password){
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
	
	void doLogout(HttpServletRequest request){
		String usernameCookie = null; 
		String passwordCookie = null; 
		Cookie[] cookies = request.getCookies(); 
		if (cookies != null) { 
			for (Cookie cookie : cookies) { 
				if ("SESSION_LOGIN_USERNAME".equals(cookie.getName())) { 
					usernameCookie =  EscapeUtils.unescape(cookie.getValue()); // 得到cookie的用户名 
					continue;
				} 
				if("SESSION_LOGIN_PASSWORD".equals(cookie.getName())) { 
					passwordCookie = cookie.getValue(); // 得到cookie的密码 
				} 
			} 
			if (usernameCookie != null && passwordCookie != null) { 
				getMySaeMemcache().delete(usernameCookie + passwordCookie);
			} 
		}
	}
	
	/**
	 * 判断用户是否登录
	 * @param request
	 * @return
	 */
	String isLogin(HttpServletRequest request) {
		String usernameCookie = null; 
		String passwordCookie = null; 
		Cookie[] cookies = request.getCookies(); 
		if (cookies != null) { 
			for (Cookie cookie : cookies) { 
				if ("SESSION_LOGIN_USERNAME".equals(cookie.getName())) { 
					usernameCookie =  EscapeUtils.unescape(cookie.getValue()); // 得到cookie的用户名 
					continue;
				} 
				if("SESSION_LOGIN_PASSWORD".equals(cookie.getName())) { 
					passwordCookie = cookie.getValue(); // 得到cookie的密码 
				} 
			} 
			if (usernameCookie != null && passwordCookie != null) { 
				if("1".equals(getMySaeMemcache().get(usernameCookie + passwordCookie))) {
					return "1";
				}
			} 
		}
		return "-1";
	}
	/**
	 * 获取cookie中的username
	 * @param request
	 * @return
	 */
	String getUsername(HttpServletRequest request) {
		String usernameCookie = null; 
		Cookie[] cookies = request.getCookies(); 
		if (cookies != null) { 
			for (Cookie cookie : cookies) { 
				if ("SESSION_LOGIN_USERNAME".equals(cookie.getName())) { 
					usernameCookie = EscapeUtils.unescape(cookie.getValue()); // 得到cookie的用户名 
					break;
				} 
			} 
		}
		return usernameCookie;
	}
}
