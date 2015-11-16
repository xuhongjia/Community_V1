package cn.andrewlu.community.entity;

public class ShoppingCart {
	private int id;
	private int uId;
	private int gId;
	private float num;
	private String note;
	public int getId() {
		return id;
	}
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public int getgId() {
		return gId;
	}
	public void setgId(int gId) {
		this.gId = gId;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getNum() {
		return num;
	}
	public void setNum(float num) {
		this.num = num;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
