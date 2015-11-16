package cn.andrewlu.community.ui.goods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpGoodsApi;
import cn.andrewlu.community.entity.Classify;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.OrderAllGoods;
import cn.andrewlu.community.entity.OrderGoods;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.BaseFragment;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class ShopsOrderFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.layout_order_shopsorder, null);
	}
	public void onViewCreated(View v, Bundle b) {
		super.onViewCreated(v, b);
	}
	private List<List<OrderAllGoods>> goodsData = new ArrayList<List<OrderAllGoods>>();
	private List<List<OrderAllGoods>> goodsDataBackup = new ArrayList<List<OrderAllGoods>>();
	private List<Integer> NoPaid=new ArrayList<Integer>();
	private List<Integer> NoDeliver=new ArrayList<Integer>();
	private List<Integer> NoReceipt=new ArrayList<Integer>();
	private List<Integer> Completed=new ArrayList<Integer>();
	private List<OrderGoods> ordergoosDate =new ArrayList<OrderGoods>();
	private List<OrderGoods> ordergoosDateBackup =new ArrayList<OrderGoods>();
	private CommonAdapter<OrderAllGoods> adapter_goods;
	private CommonAdapter<OrderGoods> adapter_orderlist;
	private ListView parent_listView;
	public void onInitView(View view) {	
		/*ViewUtils.inject(this,view);//注入view和事件		
		 */		getSellersOrder();
		 RadioGroup group =(RadioGroup)view.findViewById(R.id.rgout_shopsorder_rgout);
		 group.setOnCheckedChangeListener(this);
		 parent_listView=(ListView)getView().findViewById(R.id.lv_business_shopsorder);	
		 adapter_orderlist = new CommonAdapter<OrderGoods>(getActivity(), ordergoosDate,goodsData,
				 R.layout.layout_shopsorder_item){
			 @Override
			 public void convert(ViewHolder helper, OrderGoods item) {
				 String state ="";			
				 switch(item.getState()){
				 case 1:{
					 state="未付款";
					 helper.setText(R.id.btn_shopersoder_deliver, "发货");				 
					 break;
				 }
				 case 2:{
					 state="待发货";
					 helper.setText(R.id.btn_shopersoder_deliver, "发货");	
					 break;
				 }
				 case 3:{
					 state="待收货";
					 helper.setText(R.id.btn_shopersoder_deliver, "已发货");
					 break;
				 }
				 case 4:{
					 state="已完成";
					 helper.setText(R.id.btn_shopersoder_deliver, "已发货");
					 break;
				 }
				 }
				 helper.setText(R.id.tv_shopersorder_state, state);
				 helper.setText(R.id.tv_shopersorder_order_num,""+item.getoId());
				 helper.setText(R.id.tv_shopersorder_totalPrice,"￥"+item.getTotalPrice());
				 helper.setText(R.id.tv_shopersorder_num,""+item.getNum());
				 helper.setText(R.id.tv_shopersorder_account, "" + item.getAaccount());
				 helper.setViewClickable(R.id.btn_shopersoder_deliver);
				 //在父listview添加评价按钮点击事件
				 
			 }

			 //adpter 父listview的发货按钮添加点击处理
			 public void onItemClick(View view, OrderGoods data) {
				 switch (view.getId()) {
				 case R.id.btn_shopersoder_deliver:{					
					 TextView textView=(TextView)view.findViewById(R.id.btn_shopersoder_deliver);
					 if(textView.getText().toString().equals("发货")){				
						 setShoersOrderState(data.getoId(),3);
						 int index=ordergoosDateBackup.indexOf(data);
						 ordergoosDateBackup.get(index).setState(3);
						 BackupStateIndex();
						 adapter_orderlist.notifyDataSetChanged();
					 }
					 else{
						 Toast.makeText(getActivity(), "已发货了哦哦", Toast.LENGTH_SHORT).show();
					 }
					
					 break;
				 }   
				 default:
					 break;
				 }
			 }
			 //为子listview新建一个adpter
			 @Override
			 public View getView(int position, View convertView, ViewGroup parent)
			 {
				 final ViewHolder viewHolder = getViewHolder(position, convertView,
						 parent);
				 convert(viewHolder, getItem(position));
				 View retView = viewHolder.getConvertView();
				 //为子listview添加数据
				 adapter_goods=new CommonAdapter<OrderAllGoods>(mContext, childData.get(position),
						 R.layout.layout_shopersorder_children_listview){
					 @Override
					 public void convert(ViewHolder helper, OrderAllGoods item) {
						 helper.setText(R.id.tv_shopsorder_chlidlistview_title, item.getTitle())
						 .setText(R.id.tv_shopsorder_chlidlistview_num,"x "+item.getNum())
						 .setText(R.id.tv_shopsorder_chlidlistview_description,""+item.getDescription());
						 helper.setText(R.id.tv_shopsorder_chlidlistview_price,"￥"+item.getPrice());
						 helper.setText(R.id.tv_shopsorder_chlidlistview_extra,""+item.getExtra());
						 helper.setImageByView(R.id.imv_shopsorder_chlidlistview_img, item.getImgs().split(";")[0]);				
					 }
				 };
				 ListView child_listView=(ListView)retView.findViewById(R.id.lv_shopersorder_childitem);
				 /*child_listView.setFocusable(false);*/
				 child_listView.setAdapter(adapter_goods);
				 //跳转商品详情，监听事件下交到了子listview,所以该方法只能在子listview调用，获取响应数据需要用到position
				 initListView(child_listView,position);
				 for(Integer id: clickableIds){
					 View item = retView.findViewById(id);
					 item.setOnClickListener(this);
					 item.setTag(getItem(position));
				 }
				 return retView;

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
		intent.putExtra("ordergoods", ordergoosDateBackup.get(index));
		//订单里的商品数据
		intent.putExtra("goods",  (Serializable)goodsData.get(index)); 
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}
	//按钮组监听事件
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int type=1;
		switch(checkedId){
		case R.id.rbt_shoporder_paid:{
			break;
		}
		case R.id.rbt_shoporder_deliver:{
			type=2;
			break;
		}
		case R.id.rbt_shoporder_receipt:{
			type=3;
			break;
		}
		case R.id.rbt_shoporder_completed:{
			type=4;
			break;
		}
		}
		//添加数据
		AddData(type);
		adapter_orderlist.notifyDataSetChanged();
	}
	//保存各类型状态订单的索引，为了到时候可以找出这类型的数据显示在listview
	private void BackupStateIndex(){
		NoPaid.clear();
		NoDeliver.clear();
		NoReceipt.clear();
		Completed.clear();
		for(int i=0;i<ordergoosDateBackup.size();i++){					 
			switch(ordergoosDateBackup.get(i).getState()){
			case 1:{				
				NoPaid.add(i);

				break;
			}
			case 2:{				
				NoDeliver.add(i);

				break;
			}
			case 3:{
				NoReceipt.add(i);
				break;
			}
			case 4:{				
				Completed.add(i);
				break;
			}
			}
		}
	}
	private void AddData(int type){
		ordergoosDate.clear();
		goodsData.clear();
		switch(type){
		
		case 1:{
			for(int i=0;i<NoPaid.size();i++){
				ordergoosDate.add(ordergoosDateBackup.get(NoPaid.get(i)));
				goodsData.add(goodsDataBackup.get(NoPaid.get(i)));
			}
			break;
		}
		case 2:{
			for(int i=0;i<NoDeliver.size();i++){
				ordergoosDate.add(ordergoosDateBackup.get(NoDeliver.get(i)));
				goodsData.add(goodsDataBackup.get(NoDeliver.get(i)));
			}
			break;
		}
		case 3:{
			for(int i=0;i<NoReceipt.size();i++){
				ordergoosDate.add(ordergoosDateBackup.get(NoReceipt.get(i)));
				goodsData.add(goodsDataBackup.get(NoReceipt.get(i)));
			}
			break;
		}
		case 4:{
			for(int i=0;i<Completed.size();i++){
				ordergoosDate.add(ordergoosDateBackup.get(Completed.get(i)));
				goodsData.add(goodsDataBackup.get(Completed.get(i)));
			}
			break;
		}
		default:{

			break;
		}
		}
	}
	//获取订单列表数据
	private void getSellersOrder(){
		UserManager userManager = UserManager.getInstance();
		User u=userManager.getLoginUser();
		HttpGoodsApi.getSellerOrderList(u.getId(), new RequestCallBack<GeneralResponse<List<String[]>>>() {
			@Override
			public void onSuccess(GeneralResponse<List<String[]>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if(responseData.isSuccess()) {					
					ordergoosDateBackup.clear();
					goodsDataBackup.clear();
					for(String[] s:responseData.getData() ){
						OrderGoods orderGoods =new Gson().fromJson(s[0],OrderGoods.class);					
						List<OrderAllGoods> listgoods= new Gson().fromJson(s[1], new TypeToken<List<OrderAllGoods>>(){}.getType());
						int num=0;
						float totalPrice=0;
						for(int i=0;i<listgoods.size();i++){
							num+=listgoods.get(i).getNum();
							totalPrice+=listgoods.get(i).getPrice();
						}
						orderGoods.setNum(num);
						orderGoods.setTotalPrice(totalPrice);
						ordergoosDateBackup.add(orderGoods);
						goodsDataBackup.add(listgoods);
					};
					//保存各类型状态订单的索引，为了到时候可以找出这类型的数据显示在listview									
					BackupStateIndex();
					//显示未付款数据
					AddData(1);
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
