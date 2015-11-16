package cn.andrewlu.community.ui.goods;

import io.rong.imkit.RongIM;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.andrewlu.community.R;
import cn.andrewlu.community.RefreshManager;
import cn.andrewlu.community.api.HttpGoodsApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.entity.OrderAllGoods;
import cn.andrewlu.community.entity.OrderGoods;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.BaseFragment;
import cn.andrewlu.community.utils.AliPay;
import cn.andrewlu.community.utils.PayResult;
import cn.andrewlu.community.utils.AliPay.OnPayResultListener;
import cn.andrewlu.community.widget.ConformDialog;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

public class BuyesOrderFragment extends BaseFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.layout_order_buyesorder, null);
	}
	public void onViewCreated(View v, Bundle b) {
		super.onViewCreated(v, b);
	}
	private final List<List<OrderAllGoods>> goodsData = new ArrayList<List<OrderAllGoods>>();
	private final List<OrderGoods> ordergoosDate =new ArrayList<OrderGoods>();
	private CommonAdapter<OrderAllGoods> adapter_goods;
	private CommonAdapter<OrderGoods> adapter_orderlist;
	private SimpleDateFormat format;
	public void onInitView(View view) {	
		/*ViewUtils.inject(this,view);//注入view和事件	
		 * 	
		 */		format=new SimpleDateFormat("yyyy年MM月dd日");
		 getBuyesOrder();
		 ListView parent_listView=(ListView)getView().findViewById(R.id.lv_business_buyesorder);
		 adapter_orderlist = new CommonAdapter<OrderGoods>(getActivity(), ordergoosDate,goodsData,
				 R.layout.layout_buyesorder_item){
			 @Override
			 public void convert(ViewHolder helper, OrderGoods item) {
				 String state ="";
				 switch(item.getState()){
				 case 1:{
					 state="未付款";
					 helper.setText(R.id.btn_buyesoder_evaluation,"付款");
					 break;
				 }
				 case 2:{
					 state="待发货";
					 helper.setText(R.id.btn_buyesoder_evaluation,"催货");
					 break;
				 }
				 case 3:{
					 state="待收货";
					 helper.setText(R.id.btn_buyesoder_evaluation,"收货");
					 break;
				 }
				 case 4:{
					 state="交易完成";
					 helper.setText(R.id.btn_buyesoder_evaluation,"评价");
					 break;
				 }
				 }
				 helper.setText(R.id.tv_buyesitem_orderstatus, state);				
				 helper.setText(R.id.tv_buyesitem_ordertime,""+format.format(item.getOrderTime()));
				 helper.setText(R.id.tv_buyesitem_number,""+item.getNum());
				 helper.setText(R.id.tv_buyesitem_price, "" + item.getTotalPrice()/item.getNum());

				 //在父listview添加评价按钮点击事件
				 helper.setViewClickable(R.id.btn_buyesoder_evaluation);
			 }
			 //为子listview新建一个adpter
			 @Override
			 public View getView(int position, View convertView, ViewGroup parent)
			 {
				 final ViewHolder viewHolder = getViewHolder(position, convertView,
						 parent);
				 convert(viewHolder, getItem(position));
				 View retView = viewHolder.getConvertView();

				 //试着加入

				 adapter_goods=new CommonAdapter<OrderAllGoods>(mContext, childData.get(position),
						 R.layout.layout_buyesorder_childitem){
					 @Override
					 public void convert(ViewHolder helper, OrderAllGoods item) {
						 helper.setText(R.id.tv_buyesitem_title, item.getTitle());				
						 helper.setText(R.id.tv_buyesitem_number,"x "+item.getNum());
						 helper.setText(R.id.tv_buyesitem_price,""+item.getPrice());
						 helper.setImageByView(R.id.imv_buyesitem_imv, item.getImgs().split(";")[0]);											
					 }
				 };
				 ListView child_listView=(ListView)retView.findViewById(R.id.lv_buyesorder_childitem);
				 //跳转商品详情，监听事件下交到了子listview,所以该方法只能在子listview调用，获取响应数据需要用到position
				 initListView(child_listView,position);
				 child_listView.setAdapter(adapter_goods);						
				 for(Integer id: clickableIds){
					 View item = retView.findViewById(id);
					 item.setOnClickListener(this);
					 item.setTag(getItem(position));
				 }
				 return retView;

			 }
			 //adpter 父listview点击处理
			 public void onItemClick(View view, OrderGoods data) {
				 switch (view.getId()) {
				 case R.id.btn_buyesoder_evaluation:{
					 TextView textView=(TextView)view.findViewById(R.id.btn_buyesoder_evaluation);
					 int index=ordergoosDate.indexOf(data);
					 if(textView.getText().equals("评价")){
						 Intent intent=new Intent(getActivity(),BuyersOrderEvaluation.class);
						 
						 intent.putExtra("oid", data.getoId());
						 //订单里的商品数据
						 intent.putExtra("goods",  (Serializable)goodsData.get(index)); 
						 startActivity(intent);
						 getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					 }
					 if(textView.getText().equals("付款")){
						 //实体OrderGoods、OrderAllGoods都没有gid，以后再搞，这里应付一下
						Intent intent=new Intent(getActivity(),PaymentActivity.class);
						intent.putExtra("biaoshi","1"); 
						startActivity(intent);
						getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					 }
					if(textView.getText().equals("收货")){
						setShoersOrderState(data.getoId(),4);
						 data.setState(4);					
						 adapter_orderlist.notifyDataSetChanged();
					}
					if(textView.getText().equals("催货")){
						// 联系卖家
						ConformDialog.createDialog(getActivity(), false)
						.setMessage("确定催货？")
						.setOkListener(new ConformDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, View v) {
								dialog.dismiss();
								showToast("已通知卖家");
							}
						}).setCancelListener(new ConformDialog.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, View view) {
								dialog.dismiss();
							}
						}).show();
					}
					
					 break;
				 }   
				 default:
					 break;
				 }
			 }

		 };
		 parent_listView.setAdapter(adapter_orderlist);		
	}
	
	//跳转订单详情界面
	private void initListView(ListView listview,final int index)
	{
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				goToGoodsDetails(index);
			}
		});
	}
	/**
	 * 传递商品
	 * @param index
	 */
	private void goToGoodsDetails(int index)
	{
		Intent intent=new Intent(getActivity(),OrderDetail.class);
		//订单数据
		intent.putExtra("ordergoods", ordergoosDate.get(index));
		//订单里的商品数据
		intent.putExtra("goods",  (Serializable)goodsData.get(index)); 
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}

	//获取订单列表数据
	private void getBuyesOrder(){
		UserManager userManager = UserManager.getInstance();
		User u=userManager.getLoginUser();
		HttpGoodsApi.getOrderList(u.getId(), new RequestCallBack<GeneralResponse<List<String[]>>>() {
			@Override
			public void onSuccess(GeneralResponse<List<String[]>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if(responseData.isSuccess()) {					
					ordergoosDate.clear();
					goodsData.clear();
					for(String[] s:responseData.getData() ){
						OrderGoods orderGoods =new Gson().fromJson(s[0],OrderGoods.class);						
						List<OrderAllGoods> listgoods= new Gson().fromJson(s[1], new TypeToken<List<OrderAllGoods>>(){}.getType());						
						goodsData.add(listgoods);
						ordergoosDate.add(orderGoods);
						int num=0;
						float totalPrice=0;
						for(int i=0;i<listgoods.size();i++){
							num+=listgoods.get(i).getNum();
							totalPrice+=listgoods.get(i).getPrice();
						}
						orderGoods.setNum(num);
						orderGoods.setTotalPrice(totalPrice);
					};										
					adapter_orderlist.notifyDataSetChanged();
					/*adapter_goods.notifyDataSetChanged();*/
				}
			}

			@Override
			public GeneralResponse<List<String[]>> onParseData(String responseData) {
				GeneralResponse<List<String[]>> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<List<String[]>>>() {
				}.getType());									
				return response;
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
			}
		});

	}
	//提交订单状态，就是店主发货把订单状态改为发货中
		private void setShoersOrderState(String oid,int state){
			HttpGoodsApi.getSellerOrderState(oid, state, new RequestCallBack<GeneralResponse<String>>(){
				@Override
				public void onSuccess(GeneralResponse<String> responseData, boolean isFromCache) {
					super.onSuccess(responseData, isFromCache);
					if (responseData.isSuccess()) {									
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					super.onFailure(error, msg);
					Toast.makeText(getActivity(), "发送失败，请检查网络", Toast.LENGTH_SHORT).show();
				}
			});
		}
}
