package jintli.iwantplay.service;

import java.util.TreeSet;

import javax.annotation.Resource;

import jintli.iwantplay.support.util.SHA1;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: lijing3
 * Date: 14-4-2
 * Time: 下午3:20
 * To change this template use File | Settings | File Templates.
 */
@Service
public class WechatBasicService {

    private static final Logger logger = Logger.getLogger(WechatBasicService.class);
    @Value("${IWANTPLAY_WECHAT_TOKEN}")
    private String token;
    
    @Resource
    private MsgResponse msgResponse;

    public boolean validateEntranceURL(String signature, String timestamp, String nonce, String echostr) {
        if(StringUtils.isEmpty(signature) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce)) {
            return false;
        }
        TreeSet<String> set = new TreeSet<String>();
        set.add(token);
        set.add(timestamp);
        set.add(nonce);
        StringBuilder sb = new StringBuilder("");
        for(String value : set) {
            sb.append(value);
        }
        String result = new SHA1().getDigestOfString(sb.toString().getBytes()).toLowerCase();
        logger.info(result+"______"+signature);
        if(signature.equals(result)){
            return true;
        }
        return false;
    }

    public String settleRecieveResponse(String recievedXML) {
        if (!StringUtils.isNotEmpty(recievedXML))
            return "";
        String replyContent = "";
        logger.info("----------post result"+recievedXML+"----------");

        try {
            Document doc = DocumentHelper.parseText(recievedXML);
            String toUser = doc.selectSingleNode("/xml/ToUserName").getText();
            String fromUser = doc.selectSingleNode("/xml/FromUserName").getText();
            String createTime = doc.selectSingleNode("/xml/CreateTime").getText();
            logger.info("doc:" + doc);
            String msgType = doc.selectSingleNode("/xml/MsgType").getText();
            if("text".equals(msgType)) {
                replyContent =  msgResponse.settleText(doc,toUser,fromUser);
            } else if("image".equals(msgType)) {
                replyContent = msgResponse.settleImg(doc,toUser,fromUser);
            } else if("voice".equals(msgType)) {
                replyContent =  msgResponse.settleVoice(doc,toUser,fromUser);
            } else if("location".equals(msgType)) {
                replyContent = msgResponse.settleLocation(doc,toUser,fromUser);
            } else if ("event".equals(msgType)) {   //事件，包括（取消）收听、点击click事件
                replyContent = msgResponse.settleEvent(doc,toUser,fromUser);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return replyContent;
    }
}
