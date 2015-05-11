package jintli.iwantplay.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jintli.iwantplay.pojo.Activity;
import jintli.iwantplay.pojo.support.Constants;
import jintli.iwantplay.pojo.support.MsgDto;
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
@RequestMapping(value="/phonegap/singleactivitymsg")
public class PhoneGapSingleActivityMsgController extends PhoneGapBaseController{
	Logger logger = Logger.getLogger(PhoneGapSingleActivityMsgController.class);
	public PhoneGapSingleActivityMsgController() {
		logger.info(" ------------ PhoneGapSingleActivityMsgController");
	}
	
	private ActivityService service;

	@Resource(name="activityService")
	public void setService(ActivityService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/singleactivitymsg.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject index(HttpServletRequest request,String aid,String username,
			String passwordMD5,@RequestParam String callback) throws Exception {
		ModelAndView model = new ModelAndView();
		String islogin = isLogin(username,passwordMD5);
		if(!"1".equals(islogin)) {
			model.addObject("status","-1");
			return new JSONPObject(callback, model);
		}
		Activity activity = service.getActivityById(aid);
		if(activity == null || activity.getEndtime().compareTo(new Date()) <= 0) {
			return new JSONPObject(callback,model);
		}
		model.addObject("activity", activity);
		model.addObject("currentusername", username);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("aid", aid);
		paramMap.put("username", username);
		if(service.isUserInThisActivityGroup(paramMap)<=0 && !activity.getLeader().equals(username)) {
			return new JSONPObject(callback,model);
		}
		model.addObject("atime", JSTLFunction.getATimeByNow(activity.getEndtime()));
		return new JSONPObject(callback,model);
	}

	
	@RequestMapping(value="/sendmsg.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject sendmsg(HttpServletRequest request,HttpServletResponse response,
			String username,String passwordMD5,String msg,String aid,@RequestParam String callback) throws Exception {
		ModelAndView model = new ModelAndView();
		String islogin = isLogin(username,passwordMD5);
		if(!"1".equals(islogin)) {
			model.addObject("status","-1");
			return new JSONPObject(callback, model);
		}
		List<MsgDto> msgs = getMySaeMemcache().get(Constants.ACTIVITY_KEY + aid);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		int msgsSize = msgs==null ? 0 : msgs.size();
		if(msgs == null) {
			msgs = new ArrayList<MsgDto>();
			MsgDto msgDtoTime = new MsgDto(username + (msgsSize++),0,sdf.format(new Date()));
			msgs.add(msgDtoTime);
		} else {
			if(!sdf.format(new Date()).equals(msgs.get(msgs.size()-1).getTime())) {
				MsgDto msgDtoTime = new MsgDto(username + (msgsSize++),0,sdf.format(new Date()));
				msgs.add(msgDtoTime);
			}
		}
		String[] msgArray = msg.split("[?,.;。，； ]");
		for(int i=0;i<msgArray.length;i++) {
			String smsg = msgArray[i];
			if(smsg.length() > 0) {
				MsgDto msgDtoContent = new MsgDto(username + (msgsSize++),1,username,
						smsg.length() > 20? smsg.substring(0,18) + "..." : smsg,sdf.format(new Date()));
				msgs.add(msgDtoContent);
			}
		}
		long expireSeconds = (service.getActivityById(aid).getEndtime().getTime() - System.currentTimeMillis())/1000;
		getMySaeMemcache().set(Constants.ACTIVITY_KEY + aid, msgs,expireSeconds);
		model.addObject("status", "0");
		return new JSONPObject(callback,model);
	}
}