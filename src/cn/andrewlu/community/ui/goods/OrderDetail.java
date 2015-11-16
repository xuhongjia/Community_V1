package cn.andrewlu.community.ui.goods;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.OrderAllGoods;
import cn.andrewlu.community.entity.OrderGoods;
import cn.andrewlu.community.service.ActivityManager;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;

public class OrderDetail extends BaseActivity{

	@ViewInject(value=R.id.tv_orderdetail_state)
	private TextView tv_state;
	@ViewInject(value=R.id.tv_orderdetail_address)
	private TextView tv_address;
	@ViewInject(value=R.id.tv_orderdetail_oid)
	private TextView tv_oid;
	@ViewInject(value=R.id.tv_orderdetail_consignee)
	private TextView tv_consignee;
	@ViewInject(value=R.id.tv_orderdetail_ordertime)
	private TextView tv_ordertime;
	@ViewInject(value=R.id.tv_orderdetail_phone)
	private TextView tv_phone;
	@ViewInject(value=R.id.tv_orderdetail_typepaid)
	private TextView tv_typepaid;
	@ViewInject(value=R.id.tv_orderdetail_num)
	private TextView tv_num;
	@ViewInject(value=R.id.tv_orderdetail_totalPrice)
	private TextView tv_totalPrice;
	@OnClick(value=R.id.back)
	public void OnClick(View view){
		if(view.getId()==R.id.back){
			finish();
		}
	}
	private OrderGoods ordergoosDate;
	private List<OrderAllGoods> goodsData = new ArrayList<OrderAllGoods>();
	private CommonAdapter<OrderAllGoods> adapter_orderlist;
	private ListView listview;
	private SimpleDateFormat format;
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.orderdetails);
		ViewUtils.inject(this); //注入view和事件
		format=new SimpleDateFormat("yyyy年MM月dd日");
		ordergoosDate = (OrderGoods) getIntent().getSerializableExtra("ordergoods"); 
		goodsData.addAll((ArrayList<OrderAllGoods>) getIntent().getSerializableExtra("goods"));
		String state ="";			
		switch(ordergoosDate.getState()){
		case 1:{
			state="未付款";

			break;
		}
		case 2:{
			state="待发货";

			break;
		}
		case 3:{
			state="待收货";
			break;
		}
		case 4:{
			state="已完成";

			break;
		}
		}
		String typepaid ="";			
		switch(ordergoosDate.getPayType()){
		case 0:{
			typepaid="在线付款";

			break;
		}
		case 1:{
			typepaid="货到付款";

			break;
		}
		case 2:{
			typepaid="到店支付";
			break;
		}
		}

		tv_state.setText(state);
		tv_address.setText(ordergoosDate.getAddress());
		tv_consignee.setText(ordergoosDate.getReceiveName());
		tv_oid.setText("订单号： "+ordergoosDate.getoId());
		tv_ordertime.setText("下单时间： "+format.format(ordergoosDate.getOrderTime()));
		tv_phone.setText(ordergoosDate.getReceivePhone());
		tv_typepaid.setText("付款方式:"+typepaid);
		tv_num.setText("x"+String.valueOf(ordergoosDate.getNum()));
		tv_totalPrice.setText("￥"+String.valueOf(ordergoosDate.getTotalPrice()));
		listview=(ListView)findViewById(R.id.lv_odertatail_goods);	
		adapter_orderlist = new CommonAdapter<OrderAllGoods>(this, goodsData,
				R.layout.layout_shopersorder_children_listview){
			@Override
			public void convert(ViewHolder helper, OrderAllGoods item) {			

				helper.setText(R.id.tv_shopsorder_chlidlistview_title, item.getTitle());				
				helper.setText(R.id.tv_shopsorder_chlidlistview_num,"x "+item.getNum());
				helper.setText(R.id.tv_shopsorder_chlidlistview_description,""+item.getDescription());
				helper.setText(R.id.tv_shopsorder_chlidlistview_price,"￥"+item.getPrice());
				helper.setText(R.id.tv_shopsorder_chlidlistview_extra,""+item.getExtra());
				helper.setImageByView(R.id.imv_shopsorder_chlidlistview_img, item.getImgs().split(";")[0]);

			}
		};
		listview.setAdapter(adapter_orderlist);
		/*initListView();*/
	}

/*	//跳转订单详情界面
	private void initListView()
	{
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				goToGoodsDetails(position);
			}
		});
	}
	*//**
	 * 传递商品
	 * @param index
	 *//*
	private void goToGoodsDetails(int index)
	{
		Intent intent=new Intent(this,Commoditydetail.class);
		intent.putExtra("good", goodsData.get(index));		
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}*/
}
