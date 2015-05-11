package jintli.iwantplay.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityGroup {

	private int aid;
	private String uname;
	private String ulng;
	private String ulat;
	private Date jointime;
	private String jointimestr;
	


	public String toString() {
		return "aid:" + this.aid
				+ ",参与者:" + this.uname
				+ ",参与者经度:" + this.ulng
				+ ",参与者纬度:" + this.ulat
				+ ",加入时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.jointime);
	}



	public int getAid() {
		return aid;
	}



	public void setAid(int aid) {
		this.aid = aid;
	}



	public String getUname() {
		return uname;
	}



	public void setUname(String uname) {
		this.uname = uname;
	}



	public String getUlng() {
		return ulng;
	}



	public void setUlng(String ulng) {
		this.ulng = ulng;
	}



	public String getUlat() {
		return ulat;
	}



	public void setUlat(String ulat) {
		this.ulat = ulat;
	}



	public Date getJointime() {
		return jointime;
	}



	public void setJointime(Date jointime) {
		this.jointime = jointime;
	}



	public String getJointimestr() {
		return jointimestr;
	}



	public void setJointimestr(String jointimestr) {
		this.jointimestr = jointimestr;
	}
	
	
}
