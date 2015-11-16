package cn.andrewlu.community.ui.user;

import cn.andrewlu.community.App;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.utils.Encrypt;
import cn.andrewlu.community.widget.ProgressDialogDIY;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * Created by lenovo on 2015/9/7.
 */
public class ActivityInfocomplete extends BaseActivity{
	@ViewInject(value = R.id.tv_back)
	private View tv_back;
	@ViewInject(value = R.id.tv_complete)
	private View tv_complete;
	@ViewInject(value = R.id.radio_sex)
	private RadioGroup radio_sex;
	@ViewInject(value = R.id.radio_man)
	private View radio_man;
	@ViewInject(value = R.id.radio_women)
	private View radio_women;
	@ViewInject(value = R.id.edt_age)
	private EditText edt_age;
	@ViewInject(value = R.id.edt_address)
	private EditText edt_address;
	@ViewInject(value = R.id.edt_housenumber)
	private EditText edt_housenumber;
	private String name;
	private String phoneNumber;
	private String password;
	private String imgSrc;
	private Integer sex;
	private Integer age; 
	private String address;
	private Dialog pd;
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_infocomplete);
		ViewUtils.inject(this);
		SharedPreferences preferences = App.getSharedPreferences();
		name=preferences.getString("Rname", "");
		phoneNumber=preferences.getString("RphoneNumber", "");		
		password=preferences.getString("Rpassword", "");
		imgSrc = preferences.getString("imgSrc", "");
		System.out.print("");
	}
	@OnClick({R.id.tv_complete,R.id.tv_back})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_back: {
			Intent intent = new Intent(this, ActivityRegister.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.tv_complete: {
			pd=ProgressDialogDIY.createLoadingDialog(this, "注册中，请稍后...");
			pd.show();
			if(((RadioButton)findViewById(radio_sex.getCheckedRadioButtonId())).getText().toString().trim().equals("男")){
				sex = 1;
			}
			else
			{
				sex = 2;
			}
			age = Integer.getInteger(edt_age.getText().toString().trim()) ;
			address = edt_address.getText().toString().trim()+","+edt_housenumber.getText().toString().trim();
			if(Check())
			{
				User user = new User();
				user.setAccount(phoneNumber);
				user.setAddress(address);
				user.setAge(age);
				user.setImg(imgSrc);
				user.setPassword(Encrypt.MD5(password));
				user.setSex(sex);
				user.setName(name);
				HttpApi.registerUser(user, new RequestCallBack<GeneralResponse<String>>() {  
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
					}

					@Override
					public void onFailure(HttpException e, String msg) {
						finish();
						pd.dismiss();
					}
				});
			}
			else
			{
				pd.dismiss();
				showToast("请输入必要的信息！");
			}
			break;
		}
		}
	}
	private boolean Check(){
		if(age==null){ 		
			return false;
		}
		else if(address.equals(",")){
			return false;
		}
		else
		{
			return true;
		}
	}
}

