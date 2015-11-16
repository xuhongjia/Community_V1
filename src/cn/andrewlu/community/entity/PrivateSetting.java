package cn.andrewlu.community.entity;

public class PrivateSetting {
	private int id;
	private int uId;
	private int showPersonalInfo;
	private int showAddress;
	private int allowAddFriend;
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
	public int getShowPersonalInfo() {
		return showPersonalInfo;
	}
	public void setShowPersonalInfo(int showPersonalInfo) {
		this.showPersonalInfo = showPersonalInfo;
	}
	public int getShowAddress() {
		return showAddress;
	}
	public void setShowAddress(int showAddress) {
		this.showAddress = showAddress;
	}
	public int getAllowAddFriend() {
		return allowAddFriend;
	}
	public void setAllowAddFriend(int allowAddFriend) {
		this.allowAddFriend = allowAddFriend;
	}
	
	
}
