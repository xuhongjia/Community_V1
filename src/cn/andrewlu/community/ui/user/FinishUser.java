package cn.andrewlu.community.ui.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import cn.andrewlu.community.App;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Token;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.widget.ProgressDialogDIY;

public class FinishUser extends BaseActivity
{
	private ImageButton btBack;
	private User user;
	private Token token;
	private boolean isExitUser;
	private Dialog pd;
	private ImageView typeView;
	private int type;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_qq_register_finish);
		btBack=(ImageButton)findViewById(R.id.login_qq_back);
		btBack.setClickable(true);
		user=(User)getIntent().getSerializableExtra("user");
		token=(Token)getIntent().getSerializableExtra("token");
		type=getIntent().getIntExtra("type", 0);
		typeView=(ImageView)findViewById(R.id.typeView);
		if(type==Constant.QQ)
			typeView.setBackgroundResource(R.drawable.qq_icon);
		if(type==Constant.WEIBO)
			typeView.setBackgroundResource(R.drawable.drawable_login_weibo);
		isExitUser=getIntent().getBooleanExtra("isExitUser", false);
		btBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	//确认按钮事件 
	public void sure(View view)
	{
		
		pd = ProgressDialogDIY.createLoadingDialog(this, "登录中...");
		pd.show();
		saveToken();  //保存用户口令
		
			
		
	}
	
	/**
	 * 保存用户，老用户为更新用户资料，新用户为添加用户资料
	 */
	private void saveUser()
	{
		if(isExitUser)
		{
			HttpApi.updateUser(user, new RequestCallBack<GeneralResponse<String>>() {
				@Override
				public void onSuccess(GeneralResponse<String> responseData, boolean isFromCache) {
					if(responseData.isSuccess())
						pd.dismiss();
					App.getSharedPreferences().edit().putString("token", user.getToken()).commit();
					goToLogin();
				}
				@Override
				public void onFailure(HttpException error, String msg) {
					showToast(msg);
				};
				@Override
				public GeneralResponse<String> onParseData(String responseData) {
					// TODO Auto-generated method stub
					GeneralResponse<String> result=new Gson().fromJson(responseData,
							new TypeToken<GeneralResponse<String>>(){}.getType());
					return result;
				}
			});
		}
		else
		{
			HttpApi.insertUser(user, new RequestCallBack<GeneralResponse<String>>() {
				@Override
				public void onSuccess(GeneralResponse<String> responseData, boolean isFromCache) {
					if(responseData.isSuccess())
						pd.dismiss();
					goToLogin();
				}
				@Override
				public void onFailure(HttpException error, String msg) {
					showToast(msg);
				};
				@Override
				public GeneralResponse<String> onParseData(String responseData) {
					// TODO Auto-generated method stub
					GeneralResponse<String> result=new Gson().fromJson(responseData,
							new TypeToken<GeneralResponse<String>>(){}.getType());
;					return result;
				}
			});
		}
	
	}
	
	private void saveToken()
	{
		if(type==Constant.QQ)
		{
			HttpApi.insertToken(token, new RequestCallBack<GeneralResponse<String>>() {
				@Override
				public void onSuccess(GeneralResponse<String> responseData, boolean isFromCache) {
					if(responseData.isSuccess())
						pd.dismiss();
					saveUser();
				}
				@Override
				public void onFailure(HttpException error, String msg) {
					// TODO Auto-generated method stub
					showToast(msg);
				}
				@Override
				public GeneralResponse<String> onParseData(String responseData) {
					// TODO Auto-generated method stub
					GeneralResponse<String> result=new Gson().fromJson(responseData,
							new TypeToken<GeneralResponse<String>>(){}.getType());
						return result;
				}
			});
		}
		if(type==Constant.WEIBO)
		{
			HttpApi.insertWeiBoToken(token, new RequestCallBack<GeneralResponse<String>>() {
				@Override
				public void onSuccess(GeneralResponse<String> responseData, boolean isFromCache) {
					if(responseData.isSuccess())
						pd.dismiss();
					saveUser();
				}
				@Override
				public void onFailure(HttpException error, String msg) {
					// TODO Auto-generated method stub
					showToast(msg);
				}
				@Override
				public GeneralResponse<String> onParseData(String responseData) {
					// TODO Auto-generated method stub
					GeneralResponse<String> result=new Gson().fromJson(responseData,
							new TypeToken<GeneralResponse<String>>(){}.getType());
						return result;
				}
			});
		}
	}
	
	//登录
	private void goToLogin()
	{
		String account="";
		String psw="";
		if(!isExitUser)
		{
			account=user.getAccount();
			psw=user.getPassword();
		}
		HttpApi.login(account, psw, new RequestCallBack<GeneralResponse<User>>() {
			@Override
			public void onSuccess(GeneralResponse<User> result, boolean isFromCache) {
				if (result.isSuccess()) {
					finish();
				} else {
					showToast("用户名或密码错误");
				}
				pd.dismiss();
				
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				pd.dismiss();
			}
		});
	}
}
