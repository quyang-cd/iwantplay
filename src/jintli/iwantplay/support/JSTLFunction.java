package jintli.iwantplay.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JSTLFunction {
	
	/**
	 * 根据活动结束时间获得当前时间的称谓
	 * @param endtime
	 * @return
	 */
	public static String getATimeByNow(Date endtime){
		String ret = "已过期";
		Date now = new Date();
		DateFormat sdf=new SimpleDateFormat("dd");
		int nowDD = Integer.parseInt(sdf.format(now));
		int endDD = Integer.parseInt(sdf.format(endtime));
		int minus = daysBetween(now,endtime);
		if(minus == 0) {
			ret = "今天";
		} else if(minus == 1) {
			ret = "明天";
		} else if(minus == 2) {
			ret = "后天";
		}
		DateFormat sdf2=new SimpleDateFormat("HH");
		String hh = sdf2.format(endtime);
		if("11".equals(hh)) {
			ret += "上午";
		} else if("23".equals(hh)) {
			ret += "下午";
		}
		return ret;
	}
	
	public static String test(){
		return "lijingtest";
	}
	
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate)    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        try {
        	smdate=sdf.parse(sdf.format(smdate));  
			bdate=sdf.parse(sdf.format(bdate));
		} catch (ParseException e) {
		}  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }    
}
