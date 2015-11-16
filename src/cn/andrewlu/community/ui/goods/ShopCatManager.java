package cn.andrewlu.community.ui.goods;

import java.util.List;

import android.content.Context;
import cn.andrewlu.community.entity.ShopCat;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

/**
 * 购物车助手
 * @author Administrator
 *
 */
public class ShopCatManager 
{
	private static DbUtils utils;
	
	//获得一个数据库操作对象
	public  static DbUtils  getInstantce(Context c)
	{
		if(utils==null)
		{
			utils=DbUtils.create(c, "shopcat.db");
			
			try {
				utils.createTableIfNotExist(ShopCat.class);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		return utils;
	}
	
	/**
	 * 添加购物车
	 * @param goods
	 */
	public static void addShopCat(ShopCat shopCat)
	{
		try {
			utils.save(shopCat);
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	/**
	 * 删除购物车
	 * @param gId
	 */
	public static void deleteShopCat(int id)
	{
		try {
			utils.deleteById(ShopCat.class, id);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新购物车
	 * @param good
	 */
	public static void updateShopCat(ShopCat shopCat)
	{
		String sql="update ShopCat set count = "+shopCat.getCount()+"price ="+shopCat.getPrice()+"where id ="+shopCat.getId();
		
		try {
			utils.execNonQuery(sql);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询购物车
	 * @return
	 */
	public static List<ShopCat> queryShopCat()
	{
		
		try {
			
			return utils.findAll(ShopCat.class);
		
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 清空购物车
	 */
	public static void clearShopCat()
	{
		try {
			utils.deleteAll(ShopCat.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
