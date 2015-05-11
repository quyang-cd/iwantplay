package jintli.iwantplay.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jintli.iwantplay.service.WechatBasicService;
import jintli.iwantplay.support.util.Encoder;
import jintli.iwantplay.support.util.StreamUtils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 微信转发服务入口
 * Created with IntelliJ IDEA.
 * User: lijing3
 * Date: 14-4-1
 * Time: 下午7:17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/weChatEntrance")
public class WeChatEntranceController {
    private Logger logger = Logger.getLogger(WeChatEntranceController.class);
    @Resource
    private WechatBasicService wechatBasicService;
    /**
     * 微信校验入口，用于微信校验服务用
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public void validate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        vali(request,response);
        //测试
        //return  settleRequest(request,response);
    }

    public void vali(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if(wechatBasicService.validateEntranceURL(signature,timestamp,nonce,echostr)){
            response.getWriter().write(echostr);
            response.getWriter().flush();
        }
    }

    /**
     * 微信处理事件
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public void settleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("----------post---------------");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        //校验是否从微信而来
        if(!wechatBasicService.validateEntranceURL(signature,timestamp,nonce,"val")){
            return;
        }
        String result = "";
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        result = StreamUtils.readRequestStrByScanner(request);
        result = Encoder.stringToEncode(result,"UTF-8");
        logger.info("result:" + result);
        String replyContent = "";
        replyContent = wechatBasicService.settleRecieveResponse(result);
        logger.info("-------------------------------");
        logger.info(replyContent);
        logger.info("-------------------------------");
        if (replyContent != null) {
            response.getWriter().write(replyContent);
        }
        logger.info("----------post end---------------");
    }
}