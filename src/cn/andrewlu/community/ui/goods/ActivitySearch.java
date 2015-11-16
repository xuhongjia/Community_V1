package cn.andrewlu.community.ui.goods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpGoodsApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.entity.HotWord;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


public class ActivitySearch extends BaseActivity {
	@ViewInject(value=R.id.btn_search_back)
	private View btn_search_back;
	@ViewInject(value=R.id.edt_search_search)
	private EditText edt_search_search;
	@ViewInject(value=R.id.btn_search_search)	
	private View btn_search_search;
	@ViewInject(value=R.id.btn_search_clear)
	private View btn_search_clear;
	@ViewInject(value=R.id.tv_search_hotword)
	private EditText tv_search_hotword;
	@ViewInject(value=R.id.tv_search_hints)
	private TextView tv_search_hints;
	@ViewInject(value=R.id.lv_search_goods)
	private ListView lv_search_goods;
	@ViewInject(value=R.id.gridview)
	private GridView gridview;
	@OnClick(value={R.id.btn_search_back,R.id.edt_search_search,R.id.btn_search_search,R.id.btn_search_clear})
	public void Onclick(View view){
		switch(view.getId()){
		case R.id.btn_search_back:{
			finish();
			break;
		}
		case R.id.edt_search_search:{

			break;
		}
		case R.id.btn_search_search:{
			getSearchGoods(edt_search_search.getText().toString());
		

			break;
		}
		case R.id.btn_search_clear:{
			if(!edt_search_search.getText().toString().isEmpty()){
				edt_search_search.setText("");
				btn_search_clear.setVisibility(View.GONE);
			}			
			break;
		}
		}
	}
	private final List<HotWord> hotword=new ArrayList<HotWord>();
	private final List<Goods> goodsData = new ArrayList<Goods>();
	private CommonAdapter<Goods> adapter;
	private CommonAdapter<HotWord> adapter_hotword;
	private SimpleDateFormat format;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ViewUtils.inject(this); //注入view和事件
		format=new SimpleDateFormat("yyyy年MM月dd日");
		//让输入框不显示
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//搜索商品
		getGoods();
		adapter = new CommonAdapter<Goods>(this, goodsData,
				R.layout.listview_item){
			@Override
			public void convert(ViewHolder helper, Goods item) {
				helper.setText(R.id.tv_display_title, item.getTitle());
				helper.setText(R.id.tv_display_time,""+format.format(item.getPublishTime()));
				helper.setText(R.id.tv_display_price, "" + item.getPrice());
				helper.setImageByView(R.id.img_list_commodity, item.getImgs().split(";")[0]);
			}
		};
		lv_search_goods.setAdapter(adapter);

		//显示热词
		getHotWord();
		adapter_hotword = new CommonAdapter<HotWord>(this, hotword,
				R.layout.layout_search_gridv_item){
			@Override
			public void convert(ViewHolder helper, HotWord item) {
				helper.setText(R.id.tv_search_hotword, item.getWord());

			}			
			int red[]=new int[]{153,204,255,102,102,102,102,102,204,255},blue[]=new int[]{102,51,102,153,102,51,153,153,153,102},green[]=new int[]{255,102,51,255,51,51,0,153,102,153};
			//重写getView()
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				final ViewHolder viewHolder = getViewHolder(position, convertView,
						parent);
				convert(viewHolder, getItem(position));
				View retView = viewHolder.getConvertView();															
				for(Integer id: clickableIds){
					View item = retView.findViewById(id);
					item.setOnClickListener(this);
					item.setTag(getItem(position));
				}
				int b=(int) (Math.random() * 9);				
				TextView textView=(TextView)retView.findViewById(R.id.tv_search_hotword);
				GradientDrawable myGrad = (GradientDrawable)textView.getBackground();
				myGrad.setColor(Color.rgb(red[b], green[b], blue[b]));																					
				return retView;

			}

		};
		gridview.setAdapter(adapter_hotword);

		//为编辑框添加监听器
		edt_search_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(!edt_search_search.getText().toString().equals("")){
					btn_search_clear.setVisibility(View.VISIBLE);
				}
			}
		});
		//点击商品跳转详情页
		initListView();
		//点击搜索热词标签可以传递热词到搜索框
		initGridView();
	}
	//下拉列表跳转详情页
	private void initListView()
	{
		lv_search_goods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				goToGoodsDetails(position);
			}
		});
	}
	/**
	 * 传递商品
	 * @param index
	 */
	private void goToGoodsDetails(int index)
	{
		Intent intent=new Intent(ActivitySearch.this,Commoditydetail.class);		
		intent.putExtra("good",goodsData.get(index));
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}

	//点击搜索热词标签可以传递热词到搜索框
	private void initGridView()
	{
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				transferHotword(position);
			}
		});
	}
	/**
	 * 传递搜索热词到搜索框
	 * @param index
	 */
	private void transferHotword(int index)
	{
		edt_search_search.setText(hotword.get(index).getWord().toString());
		getSearchGoods(hotword.get(index).getWord().toString());
		
	}

	//首次進入查詢商品
	private void getGoods() {
		HttpGoodsApi.getGoodsList(new RequestCallBack<GeneralResponse<List<Goods>>>() {
			@Override
			public void onSuccess(GeneralResponse<List<Goods>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if(responseData.isSuccess()) {
					goodsData.clear();
					goodsData.addAll(responseData.getData());
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public GeneralResponse<List<Goods>> onParseData(String responseData) {
				GeneralResponse<List<Goods>> response = new Gson().fromJson(responseData,
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
	//查詢商品
	private void getSearchGoods(String keyword){
		HttpGoodsApi.getSearchGoodsList(keyword, new RequestCallBack<GeneralResponse<List<Goods>>>(){
			@Override
			public void onSuccess(GeneralResponse<List<Goods>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if(responseData.isSuccess()) {				
					goodsData.clear();
					goodsData.addAll(responseData.getData());
					adapter.notifyDataSetChanged();
					tv_search_hints.setVisibility(View.GONE);
					lv_search_goods.setVisibility(View.VISIBLE);
				}
				else{
					tv_search_hints.setVisibility(View.VISIBLE);
					lv_search_goods.setVisibility(View.GONE);
				}
			}

			@Override
			public GeneralResponse<List<Goods>> onParseData(String responseData) {
				GeneralResponse<List<Goods>> response = new Gson().fromJson(responseData,
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
	//获取搜索热词
	private void getHotWord(){
		HttpGoodsApi.getHotWord(new RequestCallBack<GeneralResponse<List<HotWord>>>(){
			@Override
			public void onSuccess(GeneralResponse<List<HotWord>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if(responseData.isSuccess()) {
					hotword.clear();
					hotword.addAll(responseData.getData());
					adapter_hotword.notifyDataSetChanged();
				}
			}

			@Override
			public GeneralResponse<List<HotWord>> onParseData(String responseData) {
				GeneralResponse<List<HotWord>> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<List<HotWord>>>() {
				}.getType());
				return response;
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
			}
		});
	}

}

