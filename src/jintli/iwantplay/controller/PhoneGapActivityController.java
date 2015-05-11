package jintli.iwantplay.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jintli.iwantplay.pojo.Activity;
import jintli.iwantplay.pojo.support.Constants;
import jintli.iwantplay.service.ActivityService;
import jintli.iwantplay.support.DateUtils;
import jintli.iwantplay.support.JSTLFunction;
import jintli.iwantplay.support.constants.ATypeDef;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping(value="/phonegap/activity")
public class PhoneGapActivityController extends PhoneGapBaseController{
	Logger logger = Logger.getLogger(PhoneGapActivityController.class);
	
	public PhoneGapActivityController() {
		logger.info("PhoneGapActivityController");
	}
	
	private ActivityService service;

	@Resource(name="activityService")
	public void setService(ActivityService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/getArroundActivities.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject getArroundActivities(HttpServletRequest request,HttpServletResponse response,
			String lng,String lat, int pageNo,@RequestParam String callback) throws Exception {
		ModelAndView model = new ModelAndView();
		String username = ""; //登陆用户
		String key;
		List<Activity> activities= service.getActivities(username,lng, lat,pageNo);
		for(Activity activity : activities) {
			activity.setAtimeForShow(JSTLFunction.getATimeByNow(activity.getEndtime()));
			key = Constants.ACTIVITY_GROUP_NUM + activity.getId();
			int currentNum = 1;
			if(getMySaeMemcache().get(key) != null) {
				currentNum = getMySaeMemcache().get(key);
			}
			activity.setAtimeForShow(JSTLFunction.getATimeByNow(activity.getEndtime()));
			activity.setGroupnum(currentNum);
		}
        model.addObject("activities", activities);
        return new JSONPObject(callback, model);
	}
	
	@RequestMapping(value="/mySponseActivity.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject mySponseActivity(HttpServletRequest request,HttpServletResponse response,
			String username,@RequestParam String callback) throws Exception {
		//查询自己发起的活动
		ModelAndView model = new ModelAndView();
		List<Activity> sponseActivities = service.getSponseActivities(username);
		String key;
		for(Activity activity : sponseActivities) {
			activity.setAtimeForShow(JSTLFunction.getATimeByNow(activity.getEndtime()));
			key = Constants.ACTIVITY_GROUP_NUM + activity.getId();
			int currentNum = 1;
			if(getMySaeMemcache().get(key) != null) {
				currentNum = getMySaeMemcache().get(key);
			}
			activity.setAtimeForShow(JSTLFunction.getATimeByNow(activity.getEndtime()));
			activity.setGroupnum(currentNum);
		}
		model.addObject("activities", sponseActivities);
        return new JSONPObject(callback, model);
	}
	
	
	@RequestMapping(value="/myPartinActivity.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject myPartinActivity(HttpServletRequest request,HttpServletResponse response,
			String username,@RequestParam String callback) throws Exception {
		//参与的活动
		ModelAndView model = new ModelAndView();
		List<Activity> partinActivities = service.getPartinActivities(username);
		String key;
		for(Activity activity : partinActivities) {
			activity.setAtimeForShow(JSTLFunction.getATimeByNow(activity.getEndtime()));
			key = Constants.ACTIVITY_GROUP_NUM + activity.getId();
			int currentNum = 1;
			if(getMySaeMemcache().get(key) != null) {
				currentNum = getMySaeMemcache().get(key);
			}
			activity.setAtimeForShow(JSTLFunction.getATimeByNow(activity.getEndtime()));
			activity.setGroupnum(currentNum);
		}
		model.addObject("activities", partinActivities);
        return new JSONPObject(callback, model);
	}
	
	@RequestMapping(value="/sponseInit.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject sponseInit(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String callback) throws Exception {
		//参与的活动
		ModelAndView model = new ModelAndView();
		Map<String,String> yundongMap = ATypeDef.YUNDONGMAP;
		Map<String,String> xiuxianMap = ATypeDef.XIUXIANMAP;
		model.addObject("yundongMap", yundongMap);
		model.addObject("xiuxianMap", xiuxianMap);
		//获取当前时间
		SimpleDateFormat df = new SimpleDateFormat("HH");//设置日期格式
		String currentH = df.format(new Date());// new Date()为获取当前系统时间
		model.addObject("currentH", currentH);
        return new JSONPObject(callback, model);
	}
	
	
	@RequestMapping(value="/submitActivity.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject submitActivity(HttpServletRequest request,HttpServletResponse response,
			Activity activity,String username,String passwordMD5,@RequestParam String callback) throws Exception {
		ModelAndView model = new ModelAndView();
		String islogin = isLogin(username,passwordMD5);
		if(!"1".equals(islogin)) {
			model.addObject("status","-1");
			return new JSONPObject(callback, model);
		}
		if(StringUtils.isEmpty(username)) {
			model.addObject("status", "-1");
			return new JSONPObject(callback, model);
		}
		activity.setLeader(username);
		//设置活动
		if(!"2".equals(activity.getAtype().substring(0, 1))) {
			activity.setActivityname(ATypeDef.getNameByAtype1(activity.getAtype().substring(1)));
		}
		Date now = new Date();
		activity.setEndtime(DateUtils.getEndDate(now, activity.getAtime()));
		if(StringUtils.isEmpty(activity.getAddrlng()) || StringUtils.isEmpty(activity.getAddrlat())) {
			activity.setAddrlng(activity.getLng());
			activity.setAddrlat(activity.getLat());
		}
		int result = service.saveActivity(activity);
		if(result > 0) {
			model.addObject("status", "0");
			model.addObject("aid", activity.getId());
			//将活动人数写入缓存
			long expireSeconds = (activity.getEndtime().getTime() - System.currentTimeMillis())/1000;
			getMySaeMemcache().set(Constants.ACTIVITY_GROUP_NUM + activity.getId(), 1, expireSeconds);
		} else {
			model.addObject("status", "1");
		}
		return new JSONPObject(callback, model);
	}
}
