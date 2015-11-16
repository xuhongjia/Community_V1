package cn.andrewlu.community.entity;

public class SellerOrder {
	//是否需要这个模型？？？？？？   决定不需要了
	private int id;
	private String parentOrderId;
	private String oId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getParentOrderId() {
		return parentOrderId;
	}
	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}
	public String getoId() {
		return oId;
	}
	public void setoId(String oId) {
		this.oId = oId;
	}
	
}
