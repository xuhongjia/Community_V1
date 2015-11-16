package cn.andrewlu.community.api;

import java.util.List;

import cn.andrewlu.community.entity.Classify;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.entity.GoodsInOrder;
import cn.andrewlu.community.entity.HotWord;
import cn.andrewlu.community.entity.Order;
import cn.andrewlu.community.entity.OrderGoods;
import cn.andrewlu.community.entity.User;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.util.Log;

/**
 * Created by lenovo on 2015/9/10.
 */
public class HttpGoodsApi extends API {

	public static void getGoodsList(
			final RequestCallBack<GeneralResponse<List<Goods>>> callBack) {
		String url = URL + "/action/goods/queryAllGoods";
		http.send(HttpRequest.HttpMethod.GET, url, callBack);
	}
	public static void getAllClassify(
			final RequestCallBack<GeneralResponse<List<Classify>>> callBack) {
		String url = URL + "/action/goods/queryAllClassify";
		http.send(HttpRequest.HttpMethod.GET, url, callBack);
	}
	public static void getSearchGoodsList(String keyword,
			final RequestCallBack<GeneralResponse<List<Goods>>> callBack) {
		RequestParams params = new RequestParams("content", keyword);

		String url = URL + "/action/goods/queryGoodsByContent";

		http.send(HttpRequest.HttpMethod.POST, url,params, callBack);
	}
	public static void getMyGoodsList(int Uid,
			final RequestCallBack<GeneralResponse<List<Goods>>> callBack) {
		RequestParams params = new RequestParams("uId", Uid);
		String url = URL + "/action/goods/getGoods";
		http.send(HttpRequest.HttpMethod.POST, url, params,callBack);
	}
	public static void getOrderList(int Uid,final RequestCallBack<GeneralResponse<List<String[]>>> callBack) {
		RequestParams params = new RequestParams("uid", Uid);
		String url = URL + "/action/order/getBuyerOrders";
		http.send(HttpRequest.HttpMethod.POST, url, params,callBack);
	}

	public static void getHotWord(final RequestCallBack<GeneralResponse<List<HotWord>>> callBack) {		
		String url = URL + "/action/hotWord/getHotWord";
		http.send(HttpRequest.HttpMethod.POST, url,callBack);
	}
	public static void deleteGoods(int gid,RequestCallBack<GeneralResponse<String>> requestCallBack){
		RequestParams params = new RequestParams("gId",gid);
		String url = URL.concat("/action/goods/deleteGoods");
		http.send(HttpMethod.POST, url,params, requestCallBack);
	}
	public static void updateGoods(Goods goods,RequestCallBack<GeneralResponse<String>> requestCallBack){
		RequestParams params = new RequestParams(goods);
		String url = URL.concat("/action/goods/updateGoods");
		http.send(HttpMethod.POST, url,params, requestCallBack);
	}
	public static void addGoodsUser (Goods goods,RequestCallBack<GeneralResponse<String>> requestCallBack){
		RequestParams params = new RequestParams(goods);
		String url = URL.concat("/action/goods/insertGoods");
		http.send(HttpMethod.POST, url,params, requestCallBack);
	}
	
	public static void getSellerOrderList(int Uid,final RequestCallBack<GeneralResponse<List<String[]>>> callBack) {
		RequestParams params = new RequestParams("sellerId", Uid);
		String url = URL + "/action/order/getSellerOrders";
		http.send(HttpRequest.HttpMethod.POST, url, params,callBack);
	}
	//提交订单状态，就是店主发货把订单状态改为发货中
	public static void getSellerOrderState(String oId,int state,RequestCallBack<GeneralResponse<String>> callBack) {
		RequestParams params = new RequestParams("oId", oId,"state",state);
		String url = URL + "/action/order//insertOrderState";
		http.send(HttpRequest.HttpMethod.POST, url, params,callBack);
	}
	public static void insertOrder(Order order,GoodsInOrder goodsInOrder,RequestCallBack<GeneralResponse<String>> callBack)
	{
		String url = URL.concat("/action/order/insertOrder");
		RequestParams params=new RequestParams(order);
		params.addBodyParameter("gId", goodsInOrder.getgId()+"");
		params.addBodyParameter("num",goodsInOrder.getNum()+"");
		params.addBodyParameter("note",goodsInOrder.getNote()+"");
		http.send(HttpMethod.POST, url,params, callBack);
	}
}