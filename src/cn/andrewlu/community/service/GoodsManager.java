package cn.andrewlu.community.service;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import cn.andrewlu.community.App;
import cn.andrewlu.community.entity.Goods;

public class GoodsManager 
{
	private static List<Goods> collectList=new ArrayList<Goods>(); 
	private static DbUtils utils=DbUtils.create(App.getInstance(),"collectionGoods");
	
	//添加收藏
	public static void addCollecGoods(Goods good)
	{
		collectList.clear();
		collectList=getAllCollectGoods();
		collectList.add(good);
		saveToDB();
	}
	
	//删除收藏
	public static void deleteCollectGoods(Goods good)
	{
		collectList.remove(good);
		saveToDB();
	}
	
	//保存到本地数据库
	public static void saveToDB()
	{
		try {
			utils.saveAll(collectList);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//读取本地数据库的所有收藏商品
	public static List<Goods> getAllCollectGoods()
	{
		try {
			List<Goods> list=utils.findAll(Goods.class);
			return list;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
