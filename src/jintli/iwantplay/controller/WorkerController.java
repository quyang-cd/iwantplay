package jintli.iwantplay.controller;

import java.io.PrintWriter;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping(value="/worker")
public class WorkerController extends BaseController{
	Logger logger = Logger.getLogger(WorkerController.class);
	public WorkerController() {
		logger.info("-------------WorkerController");
	}
	
	@RequestMapping(value="/singleactivitygroup", method=RequestMethod.GET)
	public void singleactivitygroup(HttpServletRequest request,
			HttpServletResponse response,String aid) throws Exception {
		PrintWriter out = response.getWriter();
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
		out.write(jsonObject.toString());
		out.write("\n\n");
		out.flush();
	}
	@RequestMapping(value="/singleactivitymsg", method=RequestMethod.GET)
	public void singleactivitymsg(HttpServletRequest request,
			HttpServletResponse response,String aid) throws Exception {
		PrintWriter out = response.getWriter();
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
		out.write(jsonArray.toJSONString());
		out.write("\n\n");
		out.flush();
	}
}
