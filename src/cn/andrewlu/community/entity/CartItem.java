package cn.andrewlu.community.entity;

public class CartItem {

	private Goods goods;// 商品信息.
	private float count = 1;// 购买数量,初始为1
	private String note;// 填写备注.
	private long createTime; // 添加时间.

	public final synchronized void incCount() {
		count++;
	}

	public final synchronized void decCount() {
		if (count > 1) {
			count--;
		}
	};

	public CartItem(Goods goods) {
		this.goods = goods;
	}

	public CartItem(){}
	
	// 判断是否是同一件商品.
	public final boolean isSame(Goods goods) {
		if (goods != null && goods.equals(this.goods)) {
			return true;
		}
		return false;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public float getCount() {
		return count;
	}

	public void setCount(float count) {
		this.count = count;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
