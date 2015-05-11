package jintli.iwantplay.support.util;

import java.text.DecimalFormat;

/**
 * 计算两个坐标点之间的距离
 * Created with IntelliJ IDEA.
 * User: lijing3
 * Date: 14-4-3
 * Time: 下午1:21
 * To change this template use File | Settings | File Templates.
 */
public class MapDistance {

    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static String test() {
        return "tttest";
    }

    /**
     * 或得两个坐标点之间的距离并进行格式化
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static String getDistance(double lat1, double lng1,
                                     double lat2, double lng2) {
        double s = getDistanceInMeters(lat1,lng1,lat2,lng2);
        DecimalFormat df = new DecimalFormat("#.00");
        if(s > 1000) {
            return df.format(s/1000) + "公里";
        }
        return Math.round(s) + "米";
    }
    public static String getDistance(String lat1, String lng1,
    		String lat2, String lng2) {
    	return getDistance(Double.parseDouble(lat1),Double.parseDouble(lng1),
    			Double.parseDouble(lat2),Double.parseDouble(lng2));
    }


    /**
     * 获得两个坐标点之间的距离 单位：米
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistanceInMeters(double lat1, double lng1,
                                  double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s * 1000.0;
    }
}