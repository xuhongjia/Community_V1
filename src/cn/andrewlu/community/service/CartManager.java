package cn.andrewlu.community.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.andrewlu.community.entity.CartItem;
import cn.andrewlu.community.entity.Goods;

/**
 * 购物车管理器.
 * 
 * @author andrewlu 实现添加一条购买记录,删除一条购买记录 实现添加商品数量, 减少商品数量 实现填写商品备注. 实现商品数量及价值统计.
 *         实现在线支付功能.
 */
public class CartManager {
	private static CartManager _instance = new CartManager();
	private final List<CartItem> goods = new LinkedList<CartItem>();

	public static CartManager getInstance() {
		return _instance;
	}

	public final List<CartItem> getGoods() {
		return goods;
	}

	public final void addGoods(Goods g) {
		// 如果购物车中已有本商品,则将数量加1.
		for (CartItem item : goods) {
			if (item.isSame(g)) {// 如果是同类商品,则处理后直接返回.
				item.incCount();
				return;
			}
		}

		CartItem cartItem = new CartItem(g);
		cartItem.setCreateTime(System.currentTimeMillis());
		goods.add(cartItem);
	}

	public final void removeGoods(CartItem cartItem) {
		goods.remove(cartItem);
	}

	public final void clearCart() {
		goods.clear();
	}

	public final void gotoPay() {
		// 去支付.

	}
}
