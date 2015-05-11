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
import jintli.iwantplay.support.MyMemeray;
import jintli.iwantplay.support.constants.ATypeDef;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping(value="/activity")
public class ActivityController extends BaseController{
	Logger logger = Logger.getLogger(ActivityController.class);
	
	public ActivityController() {
		logger.info("ActivityController");
	}
	
	private ActivityService service;

	@Resource(name="activityService")
	public void setService(ActivityService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/view", method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request,String reqLng,String reqLat,String openid) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		// 登陆成功
		mv.setViewName("activity");
		String islogin = isLogin(request);
		mv.addObject("islogin",islogin);
		mv.addObject("reqLng",reqLng);
		mv.addObject("reqLat",reqLat);
		mv.addObject("openid",openid);
		return mv;
	}
	
	@RequestMapping(value="/getArroundActivities.json", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView getArroundActivities(HttpServletRequest request,HttpServletResponse response,
			String lng,String lat, int pageNo,String openid) throws Exception {
		ModelAndView model = new ModelAndView();
		String username = getUsername(request); //登陆用户
		MyMemeray.getInstance().set(openid + Constants.SUFFIX_FOR_WECHAT_LNGLAT, lng+","+lat);
		List<Activity> activities= service.getActivities(username,lng, lat,pageNo);
		String key;
		for(Activity activity : activities) {
			activity.setAtimeForShow(JSTLFunction.getATimeByNow(activity.getEndtime()));
			key = Constants.ACTIVITY_GROUP_NUM + activity.getId();
			int currentNum = 1;
			if(getMySaeMemcache().get(key) != null) {
				currentNum = getMySaeMemcache().get(key);
			}
			activity.setGroupnum(currentNum);
		}
        model.addObject("activities", activities);
        return model;
	}
	
	
	/**
	 * 发起活动
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sponse", method=RequestMethod.GET)
	public ModelAndView sponse(HttpServletRequest request) throws Exception {
		String islogin = isLogin(request);
		if(!"1".equals(islogin)) {
			return new ModelAndView("redirect:/index/home");
		}
		ModelAndView mv = new ModelAndView();
		Map<String,String> yundongMap = ATypeDef.YUNDONGMAP;
		Map<String,String> xiuxianMap = ATypeDef.XIUXIANMAP;
		mv.addObject("yundongMap", yundongMap);
		mv.addObject("xiuxianMap", xiuxianMap);
		//获取当前时间
		SimpleDateFormat df = new SimpleDateFormat("HH");//设置日期格式
		String currentH = df.format(new Date());// new Date()为获取当前系统时间
		mv.addObject("currentH", currentH);
		// 登陆成功
		mv.setViewName("sponse");
		return mv;
	}
	
	@RequestMapping(value="/submitActivity", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView submitActivity(HttpServletRequest request,HttpServletResponse response,
			Activity activity) throws Exception {
		ModelAndView model = new ModelAndView();
		String username = getUsername(request);
		if(StringUtils.isEmpty(username)) {
			model.addObject("status", "-1");
			return model;
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
        return model;
	}
	
	/**
	 * 发起活动
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/person", method=RequestMethod.GET)
	public ModelAndView person(HttpServletRequest request) throws Exception {
		String islogin = isLogin(request);
		if(!"1".equals(islogin)) {
			return new ModelAndView("redirect:/index/home");
		}
		String username = getUsername(request);
		ModelAndView mv = new ModelAndView();
		String key;
		//查询自己发起的活动
		List<Activity> sponseActivities = service.getSponseActivities(username);
		for(Activity activity : sponseActivities) {
			activity.setAtimeForShow(JSTLFunction.getATimeByNow(activity.getEndtime()));
			key = Constants.ACTIVITY_GROUP_NUM + activity.getId();
			int currentNum = 1;
			if(getMySaeMemcache().get(key) != null) {
				currentNum = getMySaeMemcache().get(key);
			}
			activity.setGroupnum(currentNum);
		}
		mv.addObject("sponseActivities", sponseActivities);
		//参与的活动
		List<Activity> partinActivities = service.getPartinActivities(username);
		for(Activity activity : partinActivities) {
			activity.setAtimeForShow(JSTLFunction.getATimeByNow(activity.getEndtime()));
			key = Constants.ACTIVITY_GROUP_NUM + activity.getId();
			int currentNum = 1;
			if(getMySaeMemcache().get(key) != null) {
				currentNum = getMySaeMemcache().get(key);
			}
			activity.setGroupnum(currentNum);
		}
		mv.addObject("partinActivities", partinActivities);
		mv.setViewName("person");
		return mv;
	}
}
