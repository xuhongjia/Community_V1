package cn.andrewlu.community.entity;

public class SalesStatistics {
	private int id;
	private int gId;
	private String goods;
	private float count;
	private float saleVolume;
	private long time;
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
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public float getCount() {
		return count;
	}
	public void setCount(float count) {
		this.count = count;
	}
	public float getSaleVolume() {
		return saleVolume;
	}
	public void setSaleVolume(float saleVolume) {
		this.saleVolume = saleVolume;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
