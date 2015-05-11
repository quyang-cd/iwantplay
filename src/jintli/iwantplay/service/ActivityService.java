package jintli.iwantplay.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import jintli.iwantplay.dao.ActivityDao;
import jintli.iwantplay.pojo.Activity;
import jintli.iwantplay.pojo.ActivityGroup;
import jintli.iwantplay.support.GeoUtils;

import org.springframework.stereotype.Service;


@Service("activityService")
public class ActivityService {
	
	public static final int PAGE_NUM = 3;
	
	public ActivityService() {
		System.out.println(" -------------ActivityService ");
	}

	private ActivityDao dao;
	
	@Resource(name="activityDao")
	public void setDao(ActivityDao dao) {
		this.dao = dao;
	}

	public List<Activity> getActivities(String username,String lng,String lat,int pageNo) {
//		double[] around = GeoUtils.getAround(Double.parseDouble(lat), Double.parseDouble(lng), 2000);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		/*paramMap.put("minLat", around[0]);
		paramMap.put("maxLat", around[1]);
		paramMap.put("minLng", around[2]);
		paramMap.put("maxLng", around[3]);*/
		paramMap.put("username",username);
		paramMap.put("lng", Double.parseDouble(lng));
		paramMap.put("lat", Double.parseDouble(lat));
		paramMap.put("pageIndex", pageNo * PAGE_NUM);
		paramMap.put("pageNum", PAGE_NUM);
		return dao.getActivitiesForInstance(paramMap);
	}
	
	public List<Activity> getActivitiesInWeChat(String username,String lng,String lat,int pageNo,String keyword) {
//		double[] around = GeoUtils.getAround(Double.parseDouble(lat), Double.parseDouble(lng), 2000);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		/*paramMap.put("minLat", around[0]);
		paramMap.put("maxLat", around[1]);
		paramMap.put("minLng", around[2]);
		paramMap.put("maxLng", around[3]);*/
		paramMap.put("username",username);
		paramMap.put("lng", Double.parseDouble(lng));
		paramMap.put("lat", Double.parseDouble(lat));
		paramMap.put("pageIndex", pageNo * 5);
		paramMap.put("pageNum", 5);
		paramMap.put("keyword", keyword);
		return dao.getActivitiesForInstance(paramMap);
	}
	
	public List<ActivityGroup> getActivityGroup(int aid) {
		return dao.getActivityGroup(aid);
	}
	
	public int saveActivity(Activity activity) {
		return dao.saveActivity(activity);
	}
	
	public int saveActivityGroup(ActivityGroup ag) {
		return dao.saveActivityGroup(ag);
	}
	
	public Activity getActivityById(String id) {
		return dao.getActivityById(id);
	}
	
	public List<Activity> getSponseActivities(String username) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username",username);
		return dao.getSponseActivities(paramMap);
	}
	
	public List<Activity> getPartinActivities(String username) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username",username);
		return dao.getPartinActivities(paramMap);
	}
	
	public int isUserInThisActivityGroup(HashMap<String, Object> paramMap) {
		return dao.isUserInThisActivityGroup(paramMap);
	}
}
