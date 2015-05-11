package jintli.iwantplay.mapper;

import java.util.HashMap;
import java.util.List;

import jintli.iwantplay.pojo.Activity;
import jintli.iwantplay.pojo.ActivityGroup;


public interface ActivityMapper {
	
	// 获得周遭的活动列表
	List<Activity> getActivitiesByRange(HashMap<String, Object> paramMap);
	//由近到远获取活动列表
	List<Activity> getActivitiesForInstance(HashMap<String, Object> paramMap);
	
	List<ActivityGroup> getActivityGroup(int aid);
	
	// 新增Activity
	int saveActivity(Activity activity);
	
	int saveActivityGroup(ActivityGroup ag);
	
	Activity getActivityById(String id);
	
	List<Activity> getSponseActivities(HashMap<String, Object> paramMap);
	
	List<Activity> getPartinActivities(HashMap<String, Object> paramMap);
	
	int isUserInThisActivityGroup(HashMap<String, Object> paramMap);
	
}
