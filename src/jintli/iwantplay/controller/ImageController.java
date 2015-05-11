package jintli.iwantplay.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * M端一小时达的类别展示
 * Created with IntelliJ IDEA.
 * User: lijing3
 * Date: 14-4-9
 * Time: 上午9:25
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/image")
public class ImageController {

    @RequestMapping(value = "/exchangeFromOutToInner", method = RequestMethod.GET)
    public void exchangeFromOutToInner(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(value = "image", required = true)String image) throws Exception {
        image = URLDecoder.decode(image,"utf-8");
        //new一个URL对象
        URL url = new URL(image);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream imageIn = conn.getInputStream();
        OutputStream output = response.getOutputStream();// 得到输出流
        BufferedInputStream bis = new BufferedInputStream(imageIn);// 输入缓冲流
        BufferedOutputStream bos = new BufferedOutputStream(output);// 输出缓冲流
        byte data[] = new byte[4096];// 缓冲字节数
        int size = 0;
        size = bis.read(data);
        while (size != -1) {
            bos.write(data, 0, size);
            size = bis.read(data);
        }
        bis.close();
        bos.flush();// 清空输出缓冲流
        bos.close();
        //关闭输出流
        output.close();
    }

}
