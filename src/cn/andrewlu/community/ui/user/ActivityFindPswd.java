package cn.andrewlu.community.ui.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.util.TimeCount;

import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.UserApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.utils.Encrypt;
import cn.andrewlu.community.widget.ProgressDialogDIY;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityFindPswd extends BaseActivity implements Callback{
	@ViewInject(R.id.back_findpw)
	private View back_findpw;
	
	@ViewInject(R.id.title_findpw)
	private TextView title;

	@ViewInject(R.id.xin_pw)
	private EditText xin_pw;

	@ViewInject(R.id.check_pw)
	private View check_pw;

	@ViewInject(R.id.password_complete)
	private View password_complete;

	@ViewInject(R.id.findpwd_btnCheckcode)
	private View findpwd_btnCheckcode;

	@ViewInject(R.id.findpwd_phoneNumber)
	private EditText findpwd_phoneNumber;

	@ViewInject(R.id.smscode)
	private EditText smscode;
	
	private boolean mbDisplayFlg = false;

	//mob的Key
	private static String APPKEY = "a656aaebb7bc";
	private static String APPSECRET = "4d3d859f42559cff2c64862862f1aa61";
	private TimeCount time;
	private boolean isSuccess;
	private Dialog pd;

	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.activity_find_password);
		ViewUtils.inject(this);
		Intent i=getIntent();
		String account=i.getStringExtra("account");		
		if(account!=null){
			title.setText("修改密码");
			findpwd_phoneNumber.setText(account);
			findpwd_phoneNumber.setFocusable(false);
		}
		time = new TimeCount(60000, 1000,findpwd_btnCheckcode);
		initSDK();
	}
	@OnClick({R.id.back_findpw,R.id.check_pw,R.id.password_complete,R.id.findpwd_btnCheckcode})
	public void onClick(View view){
		switch (view.getId()){
		case R.id.back_findpw:{
			finish();
			break;
		}
		case R.id.check_pw:{
			if(!mbDisplayFlg){
				//显示密码
				xin_pw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				//隐藏密码
			}else{
				xin_pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
			mbDisplayFlg = !mbDisplayFlg;
			xin_pw.postInvalidate();
			break;
		}
		case R.id.password_complete:{
			pd=ProgressDialogDIY.createLoadingDialog(this, "验证中，请稍后...");
			pd.show();
			complete();
			break;
		}
		case R.id.findpwd_btnCheckcode:{
			if(findpwd_phoneNumber.getText().toString().trim().length()==11)
			{
				SMSSDK.getVerificationCode("86", findpwd_phoneNumber.getText().toString().trim());
				time.start();
			}
			else
			{
				showToast("手机号不正确！");
			}
			break;
		}
		}
	}
	private void updatepwd()
	{ 
		if(isSuccess)
		{
			UserApi.updatepwd(findpwd_phoneNumber.getText().toString().trim(), Encrypt.MD5(xin_pw.getText().toString().trim()), new RequestCallBack<GeneralResponse<String>>() {
				@Override
				public GeneralResponse<String> onParseData(String response) {
					return new Gson().fromJson(response,
							new TypeToken<GeneralResponse<String>>() {
							}.getType());
				}
				@Override
				public void onSuccess(final GeneralResponse<String> data,
						boolean isFromCache) 
				{
					pd.dismiss();  
					showToast(data.getMsg());
					if(data.isSuccess())
					{
						finish();
					}
				}
				@Override
				public void onFailure(HttpException e, String msg) {
					pd.dismiss();  
					showToast(msg);
				}
			});
		}
		else
		{
			showToast("验证码不正确或手机号与验证码不匹配");
		}
	}
	private void complete(){
		if(xin_pw.getText().toString().trim().length()>=6)
		{
			SMSSDK.submitVerificationCode("86",findpwd_phoneNumber.getText().toString().trim() ,smscode.getText().toString().trim());
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					updatepwd();
				}
			}, 2000);
		}
		else
		{
			showToast("密码不能小于6位");
		}
	}
	//mob短信验证码
	private void initSDK() {
		// 初始化SDK
		final Handler handler = new Handler(this);
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			} 
		};
		//注册监听器
		SMSSDK.registerEventHandler(eventHandler);
	}

	@Override
	public boolean handleMessage(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (result == SMSSDK.RESULT_COMPLETE) {

			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
				//验证成功
				Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show();
				isSuccess =	true;
			}else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
				//获取成功
				Toast.makeText(this, "获取成功", Toast.LENGTH_SHORT).show();
			}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
				//获取国家信息
			} 
		}else{                                                                 
			((Throwable)data).printStackTrace(); 
		}
		return false;
	}

}



