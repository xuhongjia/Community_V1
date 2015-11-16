package cn.andrewlu.community.entity;

import java.io.Serializable;

public class OrderAllGoods implements Serializable{
	private float num;
	private Integer uId ;
	private String title;
	private float price;
	private String description;
	private String imgs;
	private String extra ;
	public float getNum() {
		return num;
	}
	public void setNum(float num) {
		this.num = num;
	}
	public Integer getuId() {
		return uId;
	}
	public void setuId(Integer uId) {
		this.uId = uId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgs() {
		return imgs;
	}
	public void setImgs(String imgs) {
		this.imgs = imgs;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}

}