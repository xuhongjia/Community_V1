package cn.andrewlu.community.ui.goods;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.utils.Log;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.RefreshManager;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.api.HttpGoodsApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.widget.ProgressDialogDIY;
import cn.andrewlu.community.widget.common.MyAdapter;
public class ActivityReleaseGoods extends BaseActivity{
	@ViewInject(value = R.id.addgoods_back)
    private View addgoods_back;
	@ViewInject(value = R.id.addgoods_goodsview)
    private View addgoods_goodsview;
	@ViewInject(value = R.id.addgoods_goodstitle)
    private EditText addgoods_goodstitle;
    @ViewInject(value = R.id.addgoods_category)
    private View addgoods_category;
    @ViewInject(value = R.id.addgoods_category_text)
    private TextView addgoods_category_text;
    @ViewInject(value = R.id.addgoods_category_text2)
    private TextView addgoods_category_text2;
    @ViewInject(value = R.id.addgoods_type_text)
    private TextView addgoods_type_text;
    @ViewInject(value = R.id.addgoods_description_text)
    private TextView addgoods_description_text;
    @ViewInject(value = R.id.addgoods_extra_text)
    private TextView addgoods_extra_text;
    @ViewInject(value = R.id.addgoods_price)
    private EditText addgoods_price;
    @ViewInject(value = R.id.addgoods_count)
    private EditText addgoods_count; 
    @ViewInject(value = R.id.addgoods_extra)
    private View addgoods_extra;
    @ViewInject(value = R.id.addgoods_description)
    private View addgoods_description;  
    @ViewInject(value = R.id.addgoods_group)
    private RadioGroup addgoods_group;
    @ViewInject(value = R.id.addgoods_off)
    private View addgoods_off;
    @ViewInject(value = R.id.addgoods_on)
    private View addgoods_on;
	private int uId;
	private final Goods newgoods=new Goods();
	private int first=461678;
	private ArrayList<String> ImageSrc=new ArrayList<String>();
	private  ArrayList<String> ImageSrcBack=new ArrayList<String>();
	private final static StringBuilder sb = new StringBuilder();
	private Dialog pd;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addgoods);
		Bundle extras = getIntent().getExtras(); 
		uId=extras.getInt("uId"); 
		newgoods.setuId(uId);
		newgoods.setClassifyId(first);
		ViewUtils.inject(this);
	}
	@OnClick({R.id.addgoods_back,R.id.addgoods_goodsview,R.id.addgoods_category,R.id.addgoods_extra
		,R.id.addgoods_description,R.id.addgoods_off,R.id.addgoods_on})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.addgoods_back: {
			MyAdapter.mSelectedImage.clear();
			finish();
			break;
		}
		case R.id.addgoods_goodsview: {
			Intent intent=new Intent(ActivityReleaseGoods.this,ActivityPhotoChoose.class);
			startActivityForResult(intent,4);
			break;
		}
		case R.id.addgoods_category: {
			Intent intent=new Intent(ActivityReleaseGoods.this,ActivitySelectCategory.class);
			startActivityForResult(intent,3);
			break;
		}
		case R.id.addgoods_extra: {
			if (addgoods_extra_text.getText().toString().trim().length()!=0) {
				Intent intent=new Intent(ActivityReleaseGoods.this,ActivityExtraGoods.class);
				intent.putExtra("extra", newgoods.getExtra());
				startActivityForResult(intent, 1);
			}else{
			Intent intent=new Intent(ActivityReleaseGoods.this,ActivityExtraGoods.class);
			startActivityForResult(intent, 1);}
			break;
		}
		case R.id.addgoods_description: {
			if (addgoods_description_text.getText().toString().trim().length()!=0) {
				Intent intent=new Intent(ActivityReleaseGoods.this,ActivityGoodsDescription.class);
				intent.putExtra("description", newgoods.getDescription());
				startActivityForResult(intent, 2);
			}else{
			Intent intent=new Intent(ActivityReleaseGoods.this,ActivityGoodsDescription.class);
			startActivityForResult(intent, 2);}
			break;
		}
		case R.id.addgoods_off: {
           check();
           addgoods_type_text.setText(""+0);
			break;
		}
		case R.id.addgoods_on: {
			check(); 
			addgoods_type_text.setText(""+1);
			break;
		}
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 3 && resultCode == 3) {
			int classifyId =Integer.parseInt(data.getStringExtra("id").toString().trim());
			String text=data.getStringExtra("type");
			addgoods_category_text.setText(text);
			newgoods.setClassifyId(classifyId);
			addgoods_category_text2.setText(""+classifyId);
		}
		if (requestCode == 1 && resultCode == 1) {
			String extra = data.getStringExtra("extra");
			addgoods_extra_text.setText("已编辑");
			newgoods.setExtra(extra);
		}
	    if (requestCode == 2 && resultCode == 2) {
	    	String description = data.getStringExtra("description");
	    	addgoods_description_text.setText("已编辑");
			newgoods.setDescription(description);
		}
	    if (requestCode == 4 && resultCode == 4) {
	    	ImageSrc=(ArrayList<String>)data.getSerializableExtra("imagelist"); 
		}
	}

	public boolean check(){
		if(addgoods_goodstitle.getText().toString().trim().length()!=0&&addgoods_price.getText().toString().trim().length()!=0
				&&addgoods_count.getText().toString().trim().length()!=0&&ImageSrc.size()!=0&&newgoods.getClassifyId()!=first
				&&newgoods.getDescription()!=null){
			pd=ProgressDialogDIY.createLoadingDialog(this, "上传中，请稍等...");
			pd.show();
			for(int i=0;i<ImageSrc.size();i++){
				File file = new File(ImageSrc.get(i));
				HttpApi.upLoadGoodsImg(file, new RequestCallBack<GeneralResponse<String>>(){
					@Override
					public void onSuccess(ResponseInfo<Object> response,boolean isFromCache)
					{   
						GeneralResponse<String> s = new Gson().fromJson((String)response.result, new TypeToken<GeneralResponse<String>>(){}.getType()) ;
						sb.append(s.getData().split(";")[0]).append(";");
						String[] img=sb.toString().substring(0,sb.toString().length()-1).split(";");
						if(img.length==ImageSrc.size()){
							add(sb.toString().substring(0,sb.toString().length()-1));
						}
					}
					@Override
					public void onFailure(HttpException e, String msg) {
						pd.dismiss();
						showToast("图片上传失败，图片过大");
					}
				});
			}
			return true;
		}
		else{
		      if(addgoods_goodstitle.getText().toString().trim().length()==0){
                  showToast("请输入商品标题");
                  }else if(addgoods_price.getText().toString().trim().length()==0){
                	  showToast("请输入价格");
                	  }else if(addgoods_count.getText().toString().trim().length()==0){
                		  showToast("请输入库存");
                		  }else if(ImageSrc.size()==0){
                			  showToast("点击上方图标选择图片");
                			  }else if(newgoods.getClassifyId()==first){
                				  showToast("请选择类目");
                				  }else if(newgoods.getDescription()==null){
                					  showToast("请编辑宝贝描述");
                					  }
		      return false;
		}
	}
	public void add(String string){
		Date d = new Date();
		long longtime = d.getTime();
		newgoods.setPublishTime(longtime);
		newgoods.setImgs(string);
		newgoods.setIsShow(Integer.parseInt(addgoods_type_text.getText().toString().trim()));
		newgoods.setClassifyId(Integer.parseInt(addgoods_category_text2.getText().toString().trim()));
		newgoods.setTitle(addgoods_goodstitle.getText().toString().trim());
		float price=Float.parseFloat(addgoods_price.getText().toString().trim());
						newgoods.setPrice(price);
		int count=Integer.parseInt(addgoods_count.getText().toString().trim());
						newgoods.setCount(count);
		HttpGoodsApi.addGoodsUser(newgoods, new RequestCallBack<GeneralResponse<String>>() {  
			@Override
			public void onSuccess(ResponseInfo<Object> response,boolean isFromCache)
			{
				GeneralResponse<String> s = new Gson().fromJson((String)response.result, new TypeToken<GeneralResponse<String>>(){}.getType()) ;
				showToast(s.getMsg());
				if(s.isSuccess())
				{
					finish();
				}
				pd.dismiss();
				showToast("上传成功");
				MyAdapter.mSelectedImage.clear();		
				RefreshManager.refreshFlag=true;
				finish();
			}

			@Override
			public void onFailure(HttpException e, String msg) {
				pd.dismiss();
				showToast("上传失败");
			}
		});
	}
}
