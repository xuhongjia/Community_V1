package cn.andrewlu.community.entity;

public class GoodsApprase {
	private int id=1;
	private int gId=110;
	private int uId=120;

	private int rating=5;//评分1~10
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getgId() {
		return gId;
	}
	public void setgId(int gId) {
		this.gId = gId;
	}
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}


}
