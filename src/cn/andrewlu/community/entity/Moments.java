package cn.andrewlu.community.entity;

public class Moments {
	private int id ;
	private int uId;
	private String content;
	private String images;
	private Long time;
	private boolean isActivity;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public boolean isActivity() {
		return isActivity;
	}
	public void setActivity(boolean isActivity) {
		this.isActivity = isActivity;
	}
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}

}
