package jintli.iwantplay.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jintli.iwantplay.pojo.Activity;
import jintli.iwantplay.pojo.ActivityGroup;
import jintli.iwantplay.pojo.support.Constants;
import jintli.iwantplay.service.ActivityService;
import jintli.iwantplay.support.JSTLFunction;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping(value="/singleactivity")
public class SingleActivityController extends BaseController{
	Logger logger = Logger.getLogger(SingleActivityController.class);
	public SingleActivityController() {
		logger.info(" ------------ SingleActivityController");
	}
	
	private ActivityService service;

	@Resource(name="activityService")
	public void setService(ActivityService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/view", method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request,String aid,String ret,String openid) throws Exception {
		ModelAndView mv = new ModelAndView();
		String islogin = isLogin(request);
		if(!"1".equals(islogin)) {
			return new ModelAndView("redirect:/admin/index?urlAfterLogin=3&aid=" + aid);
		}
		mv.addObject("ret",ret);
		mv.addObject("openid",openid);
		mv.addObject("islogin",islogin);
		Activity activity = service.getActivityById(aid);
		if(activity == null || activity.getEndtime().compareTo(new Date()) <= 0) {
			return new ModelAndView("redirect:/activity/view");
		}
		mv.addObject("activity", activity);
		mv.addObject("logo", getLogo(activity.getAtype()));
		mv.addObject("atime", JSTLFunction.getATimeByNow(activity.getEndtime()));
		//查询该活动现在的所有成员情况
		List<ActivityGroup> group = service.getActivityGroup(activity.getId());
		mv.addObject("group", group);
		String username = getUsername(request);
		mv.addObject("currentusername", username);
		if(group != null) {
			for(ActivityGroup ag : group) {
				if(ag.getUname().equals(username)) {
					mv.addObject("hasBecomeGroup", "1");
					break;
				}
			}
		}
		if(activity.getLeader().equals(username)) {
			mv.addObject("hasBecomeGroup", "1");
		}
		mv.setViewName("singleactivity");
		return mv;
	}

	private String getLogo(String atype) {
		String type = atype.substring(1);
		String ret = "";
		if("0".equals(type)) {
			ret = "lanqiu.gif";
		} else if("1".equals(type)) {
			ret = "zuqiu.gif";
		} else if("2".equals(type)) {
			ret = "yumaoqiu.gif";
		} else if("3".equals(type)) {
			ret = "pingpang.gif";
		} else if("4".equals(type)) {
			ret = "youyong.gif";
		} else if("5".equals(type)) {
			ret = "jianshen.gif";
		} else if("6".equals(type)) {
			ret = "yoga.gif";
		} else if("a".equals(type)) {
			ret = "xiangqi.gif";
		} else if("b".equals(type)) {
			ret = "weiqi.gif";
		} else if("c".equals(type)) {
			ret = "majiang.gif";
		} else if("d".equals(type)) {
			ret = "jinhua.gif";
		} else if("e".equals(type)) {
			ret = "liaotian.gif";
		} else if("f".equals(type)) {
			ret = "sharen.gif";
		} else if("g".equals(type)) {
			ret = "sanguosha.gif";
		}
		return ret;
	}
	
	@RequestMapping(value="/partin", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView partin(HttpServletRequest request,HttpServletResponse response,
			String username,String aid,String ulng,String ulat) throws Exception {
		String islogin = isLogin(request);
		if(!"1".equals(islogin)) {
			return new ModelAndView("redirect:/activity/view");
		}
		ModelAndView model = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("aid", aid);
		paramMap.put("username", username);
		if(service.isUserInThisActivityGroup(paramMap) > 0) {
			model.addObject("status","-2");
			return model;
		}
		ActivityGroup ag = new ActivityGroup();
		ag.setAid(Integer.parseInt(aid));
		ag.setUlat(ulat);
		ag.setUlng(ulng);
		ag.setUname(username);
		int result = service.saveActivityGroup(ag);
		if(result > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String,String> groups = getMySaeMemcache().get(Constants.ACTIVITY_GROUP+aid);
			if(groups == null) {
				groups = new HashMap<String,String>();
			}
			groups.put(username, ag.getUlng()
					+ "#" + ag.getUlat() + "#" + sdf.format(new Date()));
			getMySaeMemcache().set(Constants.ACTIVITY_GROUP+aid, groups,5000);
			//将活动人数写入缓存
			long expireSeconds = (service.getActivityById(aid).getEndtime().getTime() - System.currentTimeMillis())/1000;
			int currentNum = 1;
			if(getMySaeMemcache().get(Constants.ACTIVITY_GROUP_NUM + aid) != null) {
				currentNum = getMySaeMemcache().get(Constants.ACTIVITY_GROUP_NUM + aid);
			}
			getMySaeMemcache().set(Constants.ACTIVITY_GROUP_NUM + aid, 
					(currentNum + 1), expireSeconds);
			model.addObject("status", "0");
		} else {
			model.addObject("status", "1");
		}
        return model;
	}
	
	@RequestMapping(value="/exitin", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView exitActivity(HttpServletRequest request,HttpServletResponse response,
			String username,String aid) throws Exception {
		String islogin = isLogin(request);
		if(!"1".equals(islogin)) {
			return new ModelAndView("redirect:/activity/view");
		}
		ModelAndView model = new ModelAndView();
		model.addObject("status", "1");
        return model;
	}
}
