package jintli.iwantplay.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jintli.iwantplay.pojo.support.Constants;
import jintli.iwantplay.pojo.support.MsgDto;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping(value="/phonegap/worker")
public class PhoneGapWorkerController extends PhoneGapBaseController{
	Logger logger = Logger.getLogger(PhoneGapWorkerController.class);
	public PhoneGapWorkerController() {
		logger.info("-------------WorkerController");
	}
	
	@RequestMapping(value="/singleactivitygroup.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject singleactivitygroup(HttpServletRequest request,
			HttpServletResponse response,String aid,@RequestParam String callback) throws Exception {
		//获取当前变化的参加者
		Map<String,String> groups = getMySaeMemcache().get(Constants.ACTIVITY_GROUP + aid);
		JSONArray jsonArray2 = new JSONArray();
		if(groups !=null) {
			Set<Map.Entry<String, String>> set = groups.entrySet();
	        for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
	            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
	            String value = entry.getValue();
	            String lng = value.split("#")[0];
	            String lat = value.split("#")[1];
	            String jointime = value.split("#")[2];
	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("username", entry.getKey());
	            jsonObject.put("ulng", lng);
	            jsonObject.put("ulat", lat);
	            jsonObject.put("jointime", jointime);
	            jsonArray2.add(jsonObject);
	        }
		}
		JSONObject jsonObject = new JSONObject();
		List<MsgDto> msgs = getMySaeMemcache().get(Constants.ACTIVITY_KEY + aid);
		jsonObject.put("agc", jsonArray2);
		jsonObject.put("msgsize", (msgs == null ? 0 : msgs.size()));
		return new JSONPObject(callback, jsonObject);
	}
	
	
	@RequestMapping(value="/singleactivitymsg.json", method=RequestMethod.GET)
	@ResponseBody
	public JSONPObject singleactivitymsg(HttpServletRequest request,
			HttpServletResponse response,String aid,@RequestParam String callback) throws Exception {
		//获取消息的变化值
		List<MsgDto> msgs = getMySaeMemcache().get(Constants.ACTIVITY_KEY + aid);
		JSONArray jsonArray = new JSONArray();
		if(msgs !=null) {
	        for (int i=0;i<msgs.size();i++) {
	        	MsgDto msgDto = msgs.get(i);
	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("id", msgDto.getId());
	            jsonObject.put("type", msgDto.getType());
	            jsonObject.put("time", msgDto.getTime());
	            jsonObject.put("username", msgDto.getUsername());
	            jsonObject.put("content", msgDto.getContent());
	            jsonArray.add(jsonObject);
	        }
		}
		return new JSONPObject(callback, jsonArray);
	}
}
