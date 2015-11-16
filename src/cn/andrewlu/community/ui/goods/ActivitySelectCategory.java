package cn.andrewlu.community.ui.goods;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpGoodsApi;
import cn.andrewlu.community.entity.Classify;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.service.ActivityManager;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;
public class ActivitySelectCategory extends BaseActivity{
	@ViewInject(value = R.id.selectcategory_back)
    private View selectcategory_back;
	@ViewInject(value = R.id.selectcagetory_listview)
    private ListView selectcagetory_listview;
	private  CommonAdapter<Classify> adapter_category;
	private final ArrayList<Classify> classify_list = new ArrayList<Classify>();  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectcategory);
		ViewUtils.inject(this);
		getClassify();
		adapter_category=new CommonAdapter<Classify>(this,classify_list,
				R.layout.category_list_item) {
			@Override
			public void convert(ViewHolder helper, Classify item) {			
				helper.setText(R.id.category_list_item_text,item.getType());
			}
		};
		selectcagetory_listview.setAdapter(adapter_category);
		selectcagetory_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent data = new Intent();
				String text1= ""+classify_list.get(position).getId();
				String text2=classify_list.get(position).getType();
				data.putExtra("id", text1);
				data.putExtra("type", text2);
				setResult(3, data);
				finish();
			}
			
		});
	}
	@OnClick({R.id.selectcategory_back})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.selectcategory_back: {
			finish();
			break;
		}
		default:
			break;
		}
	}
	private void getClassify() {
		HttpGoodsApi.getAllClassify(new RequestCallBack<GeneralResponse<List<Classify>>>() {
			@Override
			public void onSuccess(GeneralResponse<List<Classify>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if (responseData.isSuccess()) {
					classify_list.clear();
					classify_list.addAll(responseData.getData());
					adapter_category.notifyDataSetChanged();
				}
			}

			@Override
			public GeneralResponse<List<Classify>> onParseData(String responseData) {
				GeneralResponse<List<Classify>> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<List<Classify>>>() {
				}.getType());
				return response;
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
			}

			public void onFinished() {
				System.out.println();
			}
		});
	}
}
