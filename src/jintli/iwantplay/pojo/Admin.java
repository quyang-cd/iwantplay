package jintli.iwantplay.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Admin {

	private int id;
	private String name;
	private String password;
	private Date registdate;
	private boolean effective;
	private Date lastlogintime;
	
	public String toString() {
		return "id:" + this.id
				+ ",登陆名:" + this.name
				+ ",登陆密码:" + this.password
				+ ",注册日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.registdate)
				+ ",是否有效："  + this.effective
				+ ",最近一次登陆时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.lastlogintime);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getRegistdate() {
		return registdate;
	}
	public void setRegistdate(Date registdate) {
		this.registdate = registdate;
	}
	public boolean isEffective() {
		return effective;
	}
	public void setEffective(boolean effective) {
		this.effective = effective;
	}

	public Date getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
}
