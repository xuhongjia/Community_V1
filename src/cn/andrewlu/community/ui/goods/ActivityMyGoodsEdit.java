package cn.andrewlu.community.ui.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.view.ImageViewPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.RefreshManager;
import cn.andrewlu.community.api.BitMaoUtils;
import cn.andrewlu.community.api.HttpGoodsApi;
import cn.andrewlu.community.entity.Classify;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
public class ActivityMyGoodsEdit extends BaseActivity{
	@ViewInject(value = R.id.editgoods_back)
    private View editgoods_back;
	@ViewInject(value = R.id.edit_mygoods_pager)
    private ViewPager edit_mygoods_pager;
	@ViewInject(value = R.id.editgoods_goodstitle)
    private EditText editgoods_goodstitle;
    @ViewInject(value = R.id.editgoods_category)
    private View editgoods_category;
    @ViewInject(value = R.id.editgoods_category_text)
    private TextView editgoods_category_text;
    @ViewInject(value = R.id.editgoods_description_text)
    private TextView editgoods_description_text;
    @ViewInject(value = R.id.editgoods_extra_text)
    private TextView editgoods_extra_text;
    @ViewInject(value = R.id.editgoods_price)
    private EditText editgoods_price;
    @ViewInject(value = R.id.editgoods_count)
    private EditText editgoods_count; 
    @ViewInject(value = R.id.editgoods_extra)
    private View editgoods_extra;
    @ViewInject(value = R.id.editgoods_description)
    private View editgoods_description;  
    @ViewInject(value = R.id.editgoods_group)
    private RadioGroup editgoods_group;
    @ViewInject(value = R.id.editgoods_off)
    private View editgoods_off;
    @ViewInject(value = R.id.editgoods_on)
    private View editgoods_on;
	private Handler handler;
	private String[] imgUrlGroup;
	private List<ImageView> viewGroup;
	private int currentIndex = 0;
	private boolean isReverse = false;
	private Goods goods;
	private final ArrayList<Classify> classify_list = new ArrayList<Classify>();  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_mygoods);
		ViewUtils.inject(this);
		goods = (Goods) getIntent().getSerializableExtra("good");
		if (goods != null) {
			imgUrlGroup = goods.getImageUrls();
		}
		getClassifyId();
		initText();
		initViewPager();
		}
	private void initViewPager() {
		initImageViewGroup();
		ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(this,
				viewGroup);
		edit_mygoods_pager.setAdapter(adapter);
		currentIndex = 0;
		isReverse = false;
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					edit_mygoods_pager.setCurrentItem(currentIndex);

					if (!isReverse) {
						currentIndex++;
						if (currentIndex == 4) {
							currentIndex = 3;
							isReverse = true;
						}
					}

					else if (isReverse) {
						currentIndex--;
						if (currentIndex == 0) {
							isReverse = false;
						}
					}
					break;
				}
			}
		};
		tranImage();

	}

	private void initImageViewGroup() {

		viewGroup = new ArrayList<ImageView>();

		for (String uri : imgUrlGroup) {
			ImageView view = new ImageView(this);
			view.setScaleType(ScaleType.CENTER_CROP);
			BitMaoUtils.downLoadImage(view, uri);
			viewGroup.add(view);
		}
	}

	private void tranImage() {

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);

			}
		};
		timer.schedule(task, 0, 2000);
	}
	@OnClick({R.id.editgoods_back,R.id.editgoods_category,R.id.editgoods_extra
		,R.id.editgoods_description,R.id.editgoods_off,R.id.editgoods_on})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.editgoods_back: {
			finish();
			break;
		}
		case R.id.editgoods_category: {
			Intent intent=new Intent(ActivityMyGoodsEdit.this,ActivitySelectCategory.class);
			startActivityForResult(intent,3);
			break;
		}
		case R.id.editgoods_extra: {
			if (editgoods_extra_text.getText().toString().trim().length()!=0) {
				Intent intent=new Intent(ActivityMyGoodsEdit.this,ActivityExtraGoods.class);
				intent.putExtra("extra", goods.getExtra());
				startActivityForResult(intent, 1);
			}else{
			Intent intent=new Intent(ActivityMyGoodsEdit.this,ActivityExtraGoods.class);
			startActivityForResult(intent, 1);}
			break;
		}
		case R.id.editgoods_description: {
			if (editgoods_description_text.getText().toString().trim().length()!=0) {
				Intent intent=new Intent(ActivityMyGoodsEdit.this,ActivityGoodsDescription.class);
				intent.putExtra("description",goods.getDescription());
				startActivityForResult(intent, 2);
			}else{
			Intent intent=new Intent(ActivityMyGoodsEdit.this,ActivityGoodsDescription.class);
			startActivityForResult(intent, 2);}
			break;
		}
		case R.id.editgoods_off: {
            goods.setIsShow(0);
            update();
			break;
		}
		case R.id.editgoods_on: {
			goods.setIsShow(1);
			update();
			break;
		}
		}
	}
	private void initText() {
		editgoods_goodstitle.setText(goods.getTitle());
		editgoods_price.setText(""+goods.getPrice());
		editgoods_count.setText(""+goods.getCount());
		if(goods.getExtra()!=null)
			editgoods_extra_text.setText("已编辑");
		if(goods.getDescription()!=null)
			editgoods_description_text.setText("已编辑");
	}
	private void getClassifyId() {
		HttpGoodsApi.getAllClassify(new RequestCallBack<GeneralResponse<List<Classify>>>() {
			@Override
			public void onSuccess(GeneralResponse<List<Classify>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if (responseData.isSuccess()) {
					classify_list.clear();
					classify_list.addAll(responseData.getData());
					for (int i=0;i<classify_list.size();i++) {
						if(classify_list.get(i).getId()==goods.getClassifyId())
							editgoods_category_text.setText(classify_list.get(i).getType());
					}
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 3 && resultCode == 3) {
			int classifyId =Integer.parseInt(data.getStringExtra("id").toString().trim());
			String text=data.getStringExtra("type");
			editgoods_category_text.setText(text);
			goods.setClassifyId(classifyId);
			editgoods_category_text.setText(""+text);
		}
		if (requestCode == 1 && resultCode == 1) {
			String extra = data.getStringExtra("extra");
			editgoods_extra_text.setText("已编辑");
			goods.setExtra(extra);
		}
	    if (requestCode == 2 && resultCode == 2) {
	    	String description = data.getStringExtra("description");
	    	editgoods_description_text.setText("已编辑");
			goods.setDescription(description);
		}
	}
	public void update(){
		goods.setTitle(editgoods_goodstitle.getText().toString().trim());
		float price=Float.parseFloat(editgoods_price.getText().toString().trim());
						goods.setPrice(price);
		int count=Integer.parseInt(editgoods_count.getText().toString().trim());
						goods.setCount(count);
		HttpGoodsApi.updateGoods(goods,new RequestCallBack<GeneralResponse<String>>() {  
			@Override
			public void onSuccess(ResponseInfo<Object> response,boolean isFromCache)
			{
				showToast("修改成功");
				Intent data = new Intent();
				setResult(2, data);
			    finish();
			    RefreshManager.refreshFlag=true;
			}

			@Override
			public void onFailure(HttpException e, String msg) {
				
			}
		});
	}
}
