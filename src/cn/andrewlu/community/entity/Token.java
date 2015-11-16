package cn.andrewlu.community.entity;

import java.io.Serializable;

public class Token implements Serializable{

	private int id;
	private String phone;
	private String token;
	private String weiBoToken;
	private String weiXinToken;
	public String getWeiBoToken() {
		return weiBoToken;
	}
	public void setWeiBoToken(String weiBoToken) {
		this.weiBoToken = weiBoToken;
	}
	public String getWeiXinToken() {
		return weiXinToken;
	}
	public void setWeiXinToken(String weiXinToken) {
		this.weiXinToken = weiXinToken;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
