package jintli.iwantplay.support.util;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: lijing3
 * Date: 14-4-2
 * Time: 下午5:53
 * To change this template use File | Settings | File Templates.
 */
public class StreamUtils {
    private StreamUtils() {}

    /**
     * 读取输入流
     * @param in
     * @return
     */
    public static String readStreamParameter(ServletInputStream in){
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader=null;
        try{
            reader = new BufferedReader(new InputStreamReader(in));
            String line=null;
            while((line = reader.readLine())!=null){
                buffer.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(null!=reader){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 读取输入流
     * @param request
     * @return
     */
    public static String readRequestStrByScanner(HttpServletRequest request) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer(100);
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
        }
        return sb.toString();
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

}
