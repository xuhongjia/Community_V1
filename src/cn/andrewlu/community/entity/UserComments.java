package cn.andrewlu.community.entity;

public class UserComments extends Comment {
	private String name;
	private String img;
	private Integer touId;
	private String toname;
	private String toimg;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Integer getTouId() {
		return touId;
	}
	public void setTouId(Integer touId) {
		this.touId = touId;
	}
	public String getToname() {
		return toname;
	}
	public void setToname(String toname) {
		this.toname = toname;
	}
	public String getToimg() {
		return toimg;
	}
	public void setToimg(String toimg) {
		this.toimg = toimg;
	}
	
}
