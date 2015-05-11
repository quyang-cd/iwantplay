package jintli.iwantplay.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jintli.iwantplay.pojo.Activity;
import jintli.iwantplay.pojo.response.Article;
import jintli.iwantplay.pojo.response.NewsMessageResponse;
import jintli.iwantplay.pojo.support.Constants;
import jintli.iwantplay.support.JSTLFunction;
import jintli.iwantplay.support.MyMemeray;
import jintli.iwantplay.support.util.Encoder;
import jintli.iwantplay.support.util.MapDistance;
import jintli.iwantplay.support.util.POIUtils;
import jintli.iwantplay.support.util.ResponseUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: lijing3
 * Date: 14-4-2
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MsgResponse {
    private static final Logger logger = Logger.getLogger(MsgResponse.class);
    private String welcomeMsg = Constants.JD_O2O_WECHAT_WELCOME_MSG;

    private ActivityService service;

	@Resource(name="activityService")
	public void setService(ActivityService service) {
		this.service = service;
	}
    public String settleText(Document doc,String toUser,String fromUser) {
        String content = doc.selectSingleNode("/xml/Content").getText();
        logger.info(content);
        String lnglat = MyMemeray.getInstance().get(fromUser + Constants.SUFFIX_FOR_WECHAT_LNGLAT);
        String lng ="116.397428",lat ="39.90923";
        if(lnglat != null) {
        	lng = lnglat.split(",")[0];
            lat = lnglat.split(",")[1];
        }
        NewsMessageResponse resp = getActivities(lng, lat, fromUser, content);
        return ResponseUtil.responseNewsReturn(resp,fromUser,toUser);
    }

    public String settleVoice(Document doc,String toUser,String fromUser) {
        String recognition = doc.selectSingleNode("/xml/Recognition").getText();
        logger.info("语音识别结果：" + recognition);
        return ResponseUtil.responseTextReturn("暂时听不懂您说什么，输入文字试试",fromUser,toUser);
    }

    public String settleLocation(Document doc,String toUser,String fromUser) {
        String lat = doc.selectSingleNode("/xml/Location_X").getText();
        String lng = doc.selectSingleNode("/xml/Location_Y").getText();
        String label = doc.selectSingleNode("/xml/Label").getText();
        label = Encoder.stringToEncode(label,"UTF8");
        logger.info("label:" + label);
        NewsMessageResponse resp = getActivities(lng,lat,fromUser,"");
        return ResponseUtil.responseNewsReturn(resp,fromUser,toUser);
    }

    public String settleImg(Document doc,String toUser,String fromUser) {
        String picUrl = doc.selectSingleNode("/xml/PicUrl").getText();
        logger.info(picUrl);
        return ResponseUtil.responseTextReturn("看着挺漂亮的，哈哈",fromUser,toUser);
    }

    public String settleEvent(Document doc,String toUser,String fromUser) {
        String reply = "";
        boolean isEvent = true;
        String eventKey= "";
        String userLat = "";
        String userLng = "";
        NewsMessageResponse resp = null;
        String eventType = doc.selectSingleNode("/xml/Event").getText();
        if("CLICK".equals(eventType)) {
            eventKey = doc.selectSingleNode("/xml/EventKey").getText();
            return ResponseUtil.responseNewsReturn(resp,fromUser,toUser);
        } else if("LOCATION".equals(eventType)) {
            userLng = doc.selectSingleNode("/xml/Longitude").getText();
            userLat = doc.selectSingleNode("/xml/Latitude").getText();
            if(!StringUtils.isNotEmpty(userLng) || !StringUtils.isNotEmpty(userLat) )  {
            	//保存到缓存
            	MyMemeray.getInstance().set(fromUser + Constants.SUFFIX_FOR_WECHAT_LNGLAT, userLng+","+userLat);
            	List<Activity> activities= service.getActivities("",userLng, userLat,0);
        		for(Activity activity : activities) {
        			
        		}
                return reply;
            }
            //将用户上报的地理位置信息存储到缓存中
        } else if("subscribe".equals(eventType)) {
        	welcomeMsg += "\n\ue10f 您还可以访问<a href='"+Constants.BATH_PATH+"index/home?openid="+fromUser+"'>网页版</a>！";
            reply = ResponseUtil.responseTextReturn(welcomeMsg,fromUser,toUser);
        }
        return reply;
    }

    NewsMessageResponse getActivities(String lng,String lat,String fromUser,String keyword) {
    	NewsMessageResponse resp = new NewsMessageResponse();
        //保存到缓存
    	MyMemeray.getInstance().set(fromUser + Constants.SUFFIX_FOR_WECHAT_LNGLAT, lng+","+lat);
    	//图文标题信息，地图位置
        Article mapInfo = new Article();
        //通过地图经纬度查找地理位置信息。
        String newLabel = POIUtils.getPOIByCoordinate(lng,lat);
        mapInfo.setTitle(newLabel == null ? "搜索结果如下：" : "您的位置：" + newLabel);
        StringBuffer mapUrl = new StringBuffer();
        mapUrl.append(Constants.STATIC_MAP_URL.replace("{X}", String.valueOf(lng))
                .replace("{Y}", String.valueOf(lat)));
        
    	List<Activity> activities= service.getActivitiesInWeChat("",lng, lat,0,keyword);
    	List<Article> resultarticles = new ArrayList<Article>();
    	List<Article> articles = new ArrayList<Article>();
    	if(activities == null || activities.size() <= 0) { //未能找到门店信息
            mapUrl.append("&zoom=10");
            resp.setArticleCount(2);
            Article noActivityMsg = new Article();
            if("".equals(keyword)) {
            	noActivityMsg.setTitle("未找到附近活动信息！重新发送地址试试");
            } else {
            	noActivityMsg.setTitle("未找到与["+keyword+"]相关的活动，您可以戳我访问网页版");
            	noActivityMsg.setUrl(Constants.BATH_PATH + "index/home?openid=" + fromUser);
            }
            articles.add(noActivityMsg);
        } else {
        	if(activities.size() <=5) {
        		resp.setArticleCount(activities.size() + 2);
            } else {
            	resp.setArticleCount(7);
            }
			for(int i = 0;i<activities.size();i++) {
				Activity activity = activities.get(i);
				mapUrl.append("|" + activity.getAddrlng() + "," + activity.getAddrlat() + ",red," + (i+1));
				Article article = new Article();
				article.setTitle((i+1) + "、" + activity.getActivityname() + " [" + JSTLFunction.getATimeByNow(activity.getEndtime()) +
						activity.getGathertime() + "]" + 
				Constants.CRLF + "     " + activity.getAddr() + Constants.CRLF + 
				"     距您" + MapDistance.getDistance(lat,lng,activity.getAddrlat(),activity.getAddrlng()) + 
				" 限制" + activity.getNumlimit() + "人");
				article.setUrl(Constants.BATH_PATH + "singleactivity/view?aid="+activity.getId()+"&ret=1&openid=" + fromUser);
				articles.add(article);	
			}
        }
		String image = "";
		try {
            image = URLEncoder.encode(mapUrl.toString(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		String url = Constants.BATH_PATH + "image/exchangeFromOutToInner?image=" + image;
		mapInfo.setPicUrl(url);
		mapInfo.setUrl(Constants.BATH_PATH + "activity/view?reqLng=" + lng + "&reqLat=" + lat + "&openid=" + fromUser);
		resultarticles.add(mapInfo);
		resultarticles.addAll(articles);
		Article getMoreArticle = new Article();
        getMoreArticle.setTitle("	>> 附近更多活动");
        getMoreArticle.setUrl(Constants.BATH_PATH + "activity/view?reqLng=" + lng + "&reqLat=" + lat + "&openid=" + fromUser);
        resultarticles.add(getMoreArticle);
		resp.setArticles(resultarticles);
		return resp;
    }

}