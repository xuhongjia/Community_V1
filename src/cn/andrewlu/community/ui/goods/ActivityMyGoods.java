package cn.andrewlu.community.ui.goods;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.RefreshManager;
import cn.andrewlu.community.api.HttpGoodsApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.widget.ConformDialog;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.MyAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class ActivityMyGoods extends BaseActivity implements
RadioGroup.OnCheckedChangeListener{
	@ViewInject(id = R.id.my_goods_back)
	private ImageView my_goods_back;
	@ViewInject(id = R.id.lv_my_goods_sell)
	private ListView lv_my_goods_sell;
	@ViewInject(id = R.id.lv_my_goods_stock)
	private ListView lv_my_goods_stock;
	@ViewInject(id = R.id.radio_goods_group)
	private RadioGroup radio_goods_group;
	@ViewInject(id = R.id.rbtn_goods_sell)
	private RadioButton rbtn_goods_sell;
	@ViewInject(id = R.id.rbtn_goods_stock)
	private RadioButton rbtn_goods_stock;
	private final ArrayList<Goods> goodsData = new ArrayList<Goods>();  
	private final ArrayList<Goods> goods_sell = new ArrayList<Goods>();  
	private final ArrayList<Goods> goods_stock = new ArrayList<Goods>();  
	private  CommonAdapter<Goods> adapter_sell;
	private  CommonAdapter<Goods> adapter_stock;
	private int uId;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_goods_fragment);
		FinalActivity.initInjectedView(this);//注入view和事件
		my_goods_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		radio_goods_group.setOnCheckedChangeListener(this);
		Bundle extras = getIntent().getExtras(); 
		uId=extras.getInt("uId"); 
		getMyGoods(uId);
		initView();
		lv_my_goods_stock.setVisibility(View.GONE);
		}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rbtn_goods_sell:
			lv_my_goods_stock.setVisibility(View.GONE);
			lv_my_goods_sell.setVisibility(View.VISIBLE);
			break;
		case R.id.rbtn_goods_stock:
			lv_my_goods_sell.setVisibility(View.GONE);
			lv_my_goods_stock.setVisibility(View.VISIBLE);
		default:
			break;
	}
  }
	public void getMyGoods(int Uid){
		HttpGoodsApi
		.getMyGoodsList(Uid,new RequestCallBack<GeneralResponse<List<Goods>>>() {
			@Override
			public void onSuccess(
					GeneralResponse<List<Goods>> responseData,
					boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if (responseData.isSuccess()) {
					goodsData.clear();
					goodsData.addAll(responseData.getData());
					for (int i = 0; i < goodsData.size(); i++) {
						if(goodsData.get(i).getIsShow()==1)
							goods_sell.add(goodsData.get(i));
						else
						   goods_stock.add(goodsData.get(i));						
				} 			
					adapter_sell.notifyDataSetChanged();
					adapter_stock.notifyDataSetChanged();
					rbtn_goods_sell.setText("出售中（"+goods_sell.size()+"）");
					rbtn_goods_stock.setText("仓库中（"+goods_stock.size()+"）");
				}
			}

			@Override
			public GeneralResponse<List<Goods>> onParseData(
					String responseData) {
				GeneralResponse<List<Goods>> response = new Gson()
						.fromJson(
								responseData,
								new TypeToken<GeneralResponse<List<Goods>>>() {
								}.getType());
				return response;
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
			}
		});
	}
	public void on(final Goods gid){
		ConformDialog.createDialog(this, false)
		.setMessage("确定上架？")
		.setOkListener(new ConformDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, View v) {
				dialog.dismiss();
				gid.setIsShow(1);
				HttpGoodsApi.updateGoods(gid,new RequestCallBack<GeneralResponse<String>>() {  
					@Override
					public void onSuccess(ResponseInfo<Object> response,boolean isFromCache)
					{
						showToast("上架成功");
						goods_sell.clear();
						goods_stock.clear();
						getMyGoods(uId);
						RefreshManager.refreshFlag=true;
					}

					@Override
					public void onFailure(HttpException e, String msg) {
						
					}
				});
			}
		}).setCancelListener(new ConformDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, View view) {
				dialog.dismiss();
			}
		}).show();
	}
	public void off(final Goods gid){
		ConformDialog.createDialog(this, false)
		.setMessage("确定下架？")
		.setOkListener(new ConformDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, View v) {
				dialog.dismiss();
				gid.setIsShow(0);
				HttpGoodsApi.updateGoods(gid,new RequestCallBack<GeneralResponse<String>>() {  
					@Override
					public void onSuccess(ResponseInfo<Object> response,boolean isFromCache)
					{
						showToast("下架成功");
						goods_sell.clear();
						goods_stock.clear();
						getMyGoods(uId);
						RefreshManager.refreshFlag=true;
					}

					@Override
					public void onFailure(HttpException e, String msg) {
						
					}
				});
			}
		}).setCancelListener(new ConformDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, View view) {
				dialog.dismiss();
			}
		}).show();
	}
	public void delete(final int gid){
		ConformDialog.createDialog(this, false)
		.setMessage("确认删除？")
		.setOkListener(new ConformDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, View v) {
				dialog.dismiss();
				HttpGoodsApi.deleteGoods(gid,new RequestCallBack<GeneralResponse<String>>() {  
					@Override
					public void onSuccess(ResponseInfo<Object> response,boolean isFromCache)
					{
						showToast("删除成功");
						goods_sell.clear();
						goods_stock.clear();
						getMyGoods(uId);
						RefreshManager.refreshFlag=true;
					}

					@Override
					public void onFailure(HttpException e, String msg) {
						
					}
				});
			}
		}).setCancelListener(new ConformDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, View view) {
				dialog.dismiss();
			}
		}).show();
	}
	public void initView(){
		adapter_sell = new CommonAdapter<Goods>(this,goods_sell,
				R.layout.my_goods_list) {
			@Override
			public void convert(ViewHolder helper, Goods item) {			
				helper.setImageByView(R.id.good, item.getImgs().split(";")[0]);
				helper.setText(R.id.my_goods_title, item.getTitle());
				helper.setText(R.id.my_goods_stock, "库存："+item.getCount());
				helper.setText(R.id.my_goods_price, "￥" + item.getPrice());
				helper.setViewClickable(R.id.edit_mygoods);
				helper.setViewClickable(R.id.off_mygoods);
				helper.setViewClickable(R.id.share_mygoods);
				helper.setViewClickable(R.id.delete_mygoods);
			}
			public void onItemClick(View view, Goods data) {
				switch (view.getId()) {
				case R.id.edit_mygoods:
					Intent intent1 = new Intent(ActivityMyGoods.this,ActivityMyGoodsEdit.class);
					intent1.putExtra("good",data);
					startActivityForResult(intent1, 2);
					break;
                case R.id.off_mygoods:{
                	off(data);
					break;}
                case R.id.share_mygoods:
                	Intent intent=new Intent(Intent.ACTION_SEND); 
            		intent.setType("image/*"); 
            		intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); 
            		intent.putExtra(Intent.EXTRA_TEXT, "商品： "+data.getTitle()+"\n价格： "+data.getPrice());  
            		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            		startActivity(Intent.createChooser(intent, getTitle()+"-分享商品")); 
					break;
                case R.id.delete_mygoods:{
                	int gid2=data.getId();
                	delete(gid2);
					break;}
				default:
					break;
				}
			}
		};
		adapter_stock = new CommonAdapter<Goods>(this,goods_stock,
				R.layout.my_goods_list2) {
			@Override
			public void convert(ViewHolder helper, Goods item) {			
				helper.setImageByView(R.id.good, item.getImgs().split(";")[0]);
				helper.setText(R.id.my_goods_title, item.getTitle());
				helper.setText(R.id.my_goods_stock, "库存："+item.getCount());
				helper.setText(R.id.my_goods_price, "￥" + item.getPrice());
				helper.setViewClickable(R.id.edit_mygoods);
				helper.setViewClickable(R.id.on_mygoods);
				helper.setViewClickable(R.id.share_mygoods);
				helper.setViewClickable(R.id.delete_mygoods);
			}
			public void onItemClick(View view, Goods data) {
				switch (view.getId()) {
				case R.id.edit_mygoods:
					Intent intent1 = new Intent(ActivityMyGoods.this,ActivityMyGoodsEdit.class);
					intent1.putExtra("good",data);
					startActivityForResult(intent1, 2);
					break;
                case R.id.on_mygoods:{
                	on(data);
					break;}
                case R.id.share_mygoods:
                	Intent intent2=new Intent(Intent.ACTION_SEND); 
            		intent2.setType("image/*"); 
            		intent2.putExtra(Intent.EXTRA_SUBJECT, "分享"); 
            		intent2.putExtra(Intent.EXTRA_TEXT, "商品： "+data.getTitle()+"\n价格： "+data.getPrice());  
            		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            		startActivity(Intent.createChooser(intent2, getTitle()+"-分享商品")); 
					break;
                case R.id.delete_mygoods:{
                	int gid2=data.getId();
                	delete(gid2);
					break;}
				default:
					break;
				}
			}
		};
		lv_my_goods_sell.setAdapter(adapter_sell);
		lv_my_goods_stock.setAdapter(adapter_stock);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 2 && resultCode == 2) {
	    	goods_sell.clear();
			goods_stock.clear();
			getMyGoods(uId);
		}
	}
}
