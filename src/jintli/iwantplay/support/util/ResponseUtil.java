package jintli.iwantplay.support.util;

import java.util.List;

import jintli.iwantplay.pojo.response.Article;
import jintli.iwantplay.pojo.response.NewsMessageResponse;

import org.apache.commons.lang.StringUtils;

/**
 * 响应消息封装
 * Created with IntelliJ IDEA.
 * User: lijing3
 * Date: 14-4-2
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
public class ResponseUtil {

    /**
     * 响应文本消息
     * @param replyContent
     * @param fromUser
     * @param toUser
     * @return
     */
    public static String responseTextReturn(String replyContent,String fromUser,String toUser) {
        if(replyContent == null) {
            return null;
        }
        String responseStr = "<xml>";
        responseStr += "<ToUserName><![CDATA[" + fromUser + "]]></ToUserName>";
        responseStr += "<FromUserName><![CDATA[" + toUser + "]]></FromUserName>";
        responseStr += "<CreateTime>" + System.currentTimeMillis() + "</CreateTime>";
        responseStr += "<MsgType><![CDATA[text]]></MsgType>";
        responseStr += "<Content><![CDATA["+replyContent+"]]></Content>";
        responseStr += "<FuncFlag>0</FuncFlag>";
        responseStr += "</xml>";
        return responseStr;
    }

    /**
     * 响应响应图片消息
     * @param media_id
     * @param fromUser
     * @param toUser
     * @return
     */
    public static String responseImageReturn(String media_id,String fromUser,String toUser) {
        if(media_id == null) {
            return null;
        }
        String responseStr = "<xml>";
        responseStr += "<ToUserName><![CDATA[" + fromUser + "]]></ToUserName>";
        responseStr += "<FromUserName><![CDATA[" + toUser + "]]></FromUserName>";
        responseStr += "<CreateTime>" + System.currentTimeMillis() + "</CreateTime>";
        responseStr += "<MsgType><![CDATA[image]]></MsgType>";
        responseStr += "<Image>";
        responseStr += "<MediaId><![CDATA["+media_id+"]]></MediaId>";
        responseStr += "</Image>";
        responseStr += "</xml>";
        return responseStr;
    }

    /**
     * 响应图文消息
     * @param news
     * @param fromUser
     * @param toUser
     * @return
     */
    public static String responseNewsReturn(NewsMessageResponse news,String fromUser,String toUser) {
        String responseStr = "<xml>";
        responseStr += "<ToUserName><![CDATA[" + fromUser + "]]></ToUserName>";
        responseStr += "<FromUserName><![CDATA[" + toUser + "]]></FromUserName>";
        responseStr += "<CreateTime>" + System.currentTimeMillis() + "</CreateTime>";
        responseStr += "<MsgType><![CDATA[news]]></MsgType>";
        responseStr += "<ArticleCount>"+news.getArticleCount()+"</ArticleCount>";
        responseStr += "<Articles>";
        List<Article> articleList = news.getArticles();
        for(int i=0;i<news.getArticleCount();i++){
            Article article = articleList.get(i);
            responseStr += "<item>";
            responseStr += "<Title><![CDATA[" + article.getTitle() + "]]></Title>";
            if(StringUtils.isNotEmpty(article.getDescription())) {
                responseStr += "<Description><![CDATA[" + article.getDescription() + "]]></Description>";
            }
            if(StringUtils.isNotEmpty(article.getPicUrl())) {
                responseStr += "<PicUrl><![CDATA[" + article.getPicUrl() + "]]></PicUrl>";
            }
            if(StringUtils.isNotEmpty(article.getUrl())) {
                responseStr += "<Url><![CDATA[" + article.getUrl() + "]]></Url>";
            }
            responseStr += "</item>";
        }
        responseStr += "</Articles>";
        responseStr += "</xml>";
        return responseStr;
    }
}
