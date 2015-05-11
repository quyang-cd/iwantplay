package jintli.iwantplay.support;
/**
 * 地理相关工具类
 * @author lijing3
 *
 */
public class GeoUtils {
	
	//圆周率
	private static final double PI = 3.14159265358979323;
	//地球半径
    private static final double R = 6371229;
	    
	/**
	 * 根据给定纬度，经度，给定半径，计算最小和最大经纬度范围 
	 * @param lat		纬度
	 * @param lng		经度
	 * @param raidus	半径 单位：米
	 * @return	
	 *  [0]:minLat	最小纬度
	 *  [1]:maxLat	最大纬度
	 *  [2]:minLng	最小经度
	 *  [3]:maxLng	最大经度
	 */
	public static double[] getAround(double lat, double lng, int raidus) {

		Double latitude = lat;
		Double longitude = lng;

		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;

		Double mpdLng = degree * Math.cos(latitude * (PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLat, maxLat, minLng, maxLng };
	}
	
	/**
	 * 计算给定地球表面上两点之间距离（坐标:经纬度）
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	private static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double x, y, distance;
		x = (lng2 - lng1) * PI * R * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
		y = (lat2 - lat1) * PI * R / 180;
		distance = Math.hypot(x, y);
		return distance;
	}
}
