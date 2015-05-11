package jintli.iwantplay.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	
	/**
	 * 获得结束时间
	 * @param starttime
	 * @param flag
	 * @return
	 */
	public static Date getEndDate(Date starttime,String flag) {
		Date endtime = null;
		if("1".equals(flag) || "2".equals(flag)) {
			starttime = nextDay(starttime,1);
		}
		if("3".equals(flag) || "5".equals(flag)) {
			starttime = nextDay(starttime,2);
		}
		String yyy = sdf.format(starttime);
		if("1".equals(flag) || "3".equals(flag) || "4".equals(flag)) {
			yyy += " 11:59:59";
		} else {
			yyy += " 23:59:59";
		}
		endtime = StringToDate(yyy,"yyyy-MM-dd HH:mm:ss");
		return endtime;
	}
	
	private static Date nextDay(Date date,int num) {
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(date); 
		calendar.add(calendar.DATE,num);//把日期往后增加一天.整数往后推,负数往前移动 
		date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
		return date;
	}
	
	/**
	 * 字符串转换到时间格式
	 * @param dateStr 需要转换的字符串
	 * @param formatStr 需要格式的目标字符串  举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException 转换异常
	 */
	public static Date StringToDate(String dateStr,String formatStr){
		DateFormat sdf=new SimpleDateFormat(formatStr);
		Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static void main(String[] args) {
		System.out.println(getEndDate(new Date(),"5"));
	}
	
}
