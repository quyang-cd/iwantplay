package jintli.iwantplay.dao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import jintli.iwantplay.mapper.ActivityMapper;
import jintli.iwantplay.pojo.Activity;
import jintli.iwantplay.pojo.ActivityGroup;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;


@Repository("activityDao")
public class ActivityDao {

	public ActivityDao () {
		System.out.println(" ------------- ActivityDao");
	}

	private SqlSession session; // 如果比较简单的场合 直接用注入的Mapper即可,但需要得到Connection , 则注入一个session就启到作用了

	@Resource
	public void setSession(SqlSession session) {
		this.session = session;
	}
	
	private ActivityMapper mapper;
	
	@Resource
	public void setMapper(ActivityMapper mapper) {
		this.mapper = mapper;
	}

	public List<Activity> getActivitiesByRange(HashMap<String, Object> paramMap) {
		List<Activity> activities = null;
		
		activities = mapper.getActivitiesByRange(paramMap);
		return activities;
	}
	
	public List<Activity> getActivitiesForInstance(HashMap<String, Object> paramMap) {
		List<Activity> activities = null;
		
		activities = mapper.getActivitiesForInstance(paramMap);
		return activities;
	}
	
	public List<ActivityGroup> getActivityGroup(int aid) {
		List<ActivityGroup> activities = null;
		
		activities = mapper.getActivityGroup(aid);
		return activities;
	}
	
	public int saveActivity(Activity activity) {
		return mapper.saveActivity(activity);
	}
	
	public int saveActivityGroup(ActivityGroup ag) {
		return mapper.saveActivityGroup(ag);
	}
	
	public Activity getActivityById(String id) {
		return mapper.getActivityById(id);
	}
	
	public List<Activity> getSponseActivities(HashMap<String, Object> paramMap) {
		List<Activity> activities = null;
		activities = mapper.getSponseActivities(paramMap);
		return activities;
	}
	public List<Activity> getPartinActivities(HashMap<String, Object> paramMap) {
		List<Activity> activities = null;
		activities = mapper.getPartinActivities(paramMap);
		return activities;
	}
	
	public int isUserInThisActivityGroup(HashMap<String, Object> paramMap) {
		return mapper.isUserInThisActivityGroup(paramMap);
	}

}
