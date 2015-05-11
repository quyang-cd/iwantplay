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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping(value="/phonegap/singleactivity")
public class PhoneGapSingleActivityController extends PhoneGapBaseController{
	Logger logger = Logger.getLogger(PhoneGapSingleActivityController.class);
	public PhoneGapSingleActivityController() {
		logger.info(" ------------ PhoneGapSingleActivityController");
	}
	
	private ActivityService service;

	@Resource(name="activityService")
	public void setService(ActivityService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/singleactivityinit.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject index(HttpServletRequest request,String aid,String ret,String username,
			String passwordMD5,@RequestParam String callback) throws Exception {
		ModelAndView model = new ModelAndView();
		String islogin = isLogin(username,passwordMD5);
		if(!"1".equals(islogin)) {
			model.addObject("status","-1");
			return new JSONPObject(callback, model);
		}
		model.addObject("ret",ret);
		Activity activity = service.getActivityById(aid);
		if(activity == null || activity.getEndtime().compareTo(new Date()) <= 0 || "".equals(username)) {
			return new JSONPObject(callback,model);
		}
		model.addObject("activity", activity);
		model.addObject("atime", JSTLFunction.getATimeByNow(activity.getEndtime()));
		//查询该活动现在的所有成员情况
		List<ActivityGroup> group = service.getActivityGroup(activity.getId());
		model.addObject("group", group);
		if(group != null) {
			for(ActivityGroup ag : group) {
				if(ag.getUname().equals(username)) {
					model.addObject("hasBecomeGroup", "1");
					break;
				}
			}
		}
		if(activity.getLeader().equals(username)) {
			model.addObject("hasBecomeGroup", "1");
		}
		return new JSONPObject(callback,model);
	}

	@RequestMapping(value="/partin.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject partin(HttpServletRequest request,HttpServletResponse response,
			String username,String passwordMD5,String aid,String ulng,String ulat,@RequestParam String callback) throws Exception {
		ModelAndView model = new ModelAndView();
		String islogin = isLogin(username,passwordMD5);
		if(!"1".equals(islogin)) {
			model.addObject("status","-1");
			return new JSONPObject(callback, model);
		}
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("aid", aid);
		paramMap.put("username", username);
		if(service.isUserInThisActivityGroup(paramMap) > 0) {
			model.addObject("status","-2");
			return new JSONPObject(callback, model);
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
			int currentNum = getMySaeMemcache().get(Constants.ACTIVITY_GROUP_NUM + aid);
			getMySaeMemcache().set(Constants.ACTIVITY_GROUP_NUM + aid, 
					(currentNum + 1), expireSeconds);
			model.addObject("status", "0");
		} else {
			model.addObject("status", "1");
		}
        return new JSONPObject(callback,model);
	}
}
