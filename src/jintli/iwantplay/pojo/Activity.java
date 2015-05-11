package jintli.iwantplay.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity {

	private int id;
	private String atype;//活动类型。第一位为一级：0-运动，1-休闲；第二位为具体：0-篮球，1-足球；2-羽毛球；3-乒乓球，a-看电视，b-围棋
	private String leader;
	private String lng;
	private String lat;
	private String addrlng;
	private String addrlat;
	private String addr;
	private String atime;//时间：0-今天下午；1-明天上午；2-明天下午；3-后天上午
	private Date starttime;
	private String starttimestr;
	private Date endtime;//活动结束时间 = 发起时间+atime
	private String atimeForShow;
	private String description;
	private int groupnum;
	private String activityname;
	private String gathertime;
	private String numlimit;
	


	public String getActivityname() {
		return activityname;
	}


	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}


	public String getGathertime() {
		return gathertime;
	}


	public void setGathertime(String gathertime) {
		this.gathertime = gathertime;
	}


	public String getNumlimit() {
		return numlimit;
	}


	public void setNumlimit(String numlimit) {
		this.numlimit = numlimit;
	}


	public int getGroupnum() {
		return groupnum;
	}


	public void setGroupnum(int groupnum) {
		this.groupnum = groupnum;
	}


	public String toString() {
		return "id:" + this.id
				+ ",活动类型:" + this.atype
				+ ",发起人:" + this.leader
				+ ",发起人经度:" + this.lng
				+ ",发起人纬度:" + this.lat
				+ ",活动地点:" + this.addr
				+ ",活动时间:" + this.atime
				+ ",备注:" + this.description
				+ ",发起活动时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.starttime);
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}


	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	
	public String getAtype() {
		return atype;
	}


	public void setAtype(String atype) {
		this.atype = atype;
	}


	public String getAtime() {
		return atime;
	}


	public void setAtime(String atime) {
		this.atime = atime;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getEndtime() {
		return endtime;
	}


	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}


	public String getAtimeForShow() {
		return atimeForShow;
	}


	public void setAtimeForShow(String atimeForShow) {
		this.atimeForShow = atimeForShow;
	}


	public String getAddrlng() {
		return addrlng;
	}


	public void setAddrlng(String addrlng) {
		this.addrlng = addrlng;
	}
	
	public String getAddrlat() {
		return addrlat;
	}


	public void setAddrlat(String addrlat) {
		this.addrlat = addrlat;
	}


	public String getStarttimestr() {
		return starttimestr;
	}


	public void setStarttimestr(String starttimestr) {
		this.starttimestr = starttimestr;
	}

}
