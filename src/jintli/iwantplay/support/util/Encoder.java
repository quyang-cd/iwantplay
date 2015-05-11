package jintli.iwantplay.support.util;

import java.io.UnsupportedEncodingException;

/**
 * 编码工具类
 * Created with IntelliJ IDEA.
 * User: lijing3
 * Date: 14-4-2
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
public class Encoder {
    private Encoder(){}
    /**
     * 将字符串转换为某种编码
     */
    public static String stringToEncode(String oriStr,String encoder){
        String newStr = "";
        try {
            newStr = new String(oriStr.getBytes(),encoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return newStr;
    }
}
