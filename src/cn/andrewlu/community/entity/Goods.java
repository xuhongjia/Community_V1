package cn.andrewlu.community.entity;

import java.io.Serializable;

import android.R.integer;

import com.lidroid.xutils.db.annotation.Id;

public class Goods implements Serializable {
	/**
	 * 
	 */
	@Id
	private int id;
	private int uId;
	private String title;
	private int classifyId;
	private float price;
	private int count;
	private String description;
	private int isShow;
	private String imgs;
	private Long publishTime;
	private String extra = "";

	// 处理图片链接.
	public String[] getImageUrls() {
		if (imgs == null || imgs.length() == 0) {
			return new String[0];
		}
		return imgs.split(";");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public Long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public int getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(int classifyId) {
		this.classifyId = classifyId;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	// 重写相等比较.
	public boolean equals(Object object) {
		if (object != null && object instanceof Goods) {
			if (((Goods) object).id == this.id) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return id;
	}
}
