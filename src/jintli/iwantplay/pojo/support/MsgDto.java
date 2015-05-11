package jintli.iwantplay.pojo.support;

import java.io.Serializable;

public class MsgDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4653759984375478541L;
	private String id;
	private int type;//0-时间 1-内容
	private String time;//为0时有值
	private String username;
	private String content;
	public MsgDto(String id,int type,String time) {
		this.id = id;
		this.type = type;
		this.time = time;
	}
	public MsgDto(String id,int type,String username,String content,String time) {
		this.id = id;
		this.type = type;
		this.username = username;
		this.content = content;
		this.time = time;
	}
	
	public String toString() {
		return id + "-" + type + "-" + username + "-"+ content + "-"+ time;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
