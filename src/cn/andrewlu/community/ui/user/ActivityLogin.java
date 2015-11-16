package cn.andrewlu.community.ui.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeUser;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchUserListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cn.andrewlu.community.App;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.MainActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.ui.rongyun.RongYunUtils;
import cn.andrewlu.community.utils.Encrypt;
import cn.andrewlu.community.widget.ProgressDialogDIY;

public class ActivityLogin extends BaseActivity {
	@ViewInject(R.id.phoneNumber)
	private EditText phoneNumber;

	@ViewInject(R.id.passsword)
	private EditText password;

	@ViewInject(R.id.checkPswdBtn)
	private View checkPswBtn;

	@ViewInject(R.id.loginBtn)
	private View loginBtn;

	@ViewInject(R.id.btnLoginQQ)
	private View btnLoginQQ;

	@ViewInject(R.id.btnLoginWeibo)
	private View btnLoginWeibo;

	@ViewInject(R.id.btnLoginWeixin)
	private View btnLoginWeixin;

	@ViewInject(R.id.btnRegister)
	private View btnRegister;

	@ViewInject(R.id.btnFindPswd)
	private View btnFindPswd;

	private String phone = "";
	private boolean isLogin = false;
	private boolean mbDisplayFlg = false;
	private Dialog pd;
	private UMSocialService mController; // 友盟
	private UMQQSsoHandler qqSsoHandler;
	private SnsAccount account;
	private String icon; // 用户头像
	private String userName; // 昵称
	private String token; // 令牌
	private User user;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
	}

	@OnClick({ R.id.phoneNumber, R.id.passsword, R.id.checkPswdBtn, R.id.loginBtn, R.id.btnLoginQQ, R.id.btnLoginWeibo,
			R.id.btnLoginWeixin, R.id.btnRegister, R.id.btnFindPswd, R.id.login_back })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.phoneNumber: {
			Toast.makeText(this, "请输入11位手机号码", Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.passsword: {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.btnLoginQQ: {
			QQLogin();
			break;
		}
		case R.id.checkPswdBtn: {
			if (!mbDisplayFlg) {
				// 显示密码
				password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				// 隐藏密码
			} else {
				password.setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
			mbDisplayFlg = !mbDisplayFlg;
			password.postInvalidate();
			break;
		}
		case R.id.loginBtn: {
			login();
			break;
		}
		case R.id.btnLoginWeibo:{
			loginWeiBo();
			break;
		}
		case R.id.btnRegister: {
			Intent intent = new Intent(this, ActivityRegister.class);
			startActivity(intent);
			break;
		}
		case R.id.btnFindPswd: {
			Intent intent = new Intent(this, ActivityFindPswd.class);
			startActivity(intent);
			break;
		}
		case R.id.login_back: {
			finish();
			break;
		}
		}
	}

	private void login() {
		// 检查参数/.....
		pd = ProgressDialogDIY.createLoadingDialog(this, "登录中...");
		pd.show();
		String username = phoneNumber.getText().toString();
		String password = Encrypt.MD5(this.password.getText().toString());
		HttpApi.login(username, password, new RequestCallBack<GeneralResponse<User>>() {
			@Override
			public void onSuccess(GeneralResponse<User> result, boolean isFromCache) {
				if (result.isSuccess()) {
					Intent i = new Intent(ActivityLogin.this, MainActivity.class);
					startActivity(i);
					finish();
				} else {
					showToast("用名或密码错误");
				}
				pd.dismiss();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				pd.dismiss();
			}
		});
		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// pd.dismiss();
		// }
		// }, 2000);
	}

	/**
	 * qq登录
	 */
	private void QQLogin() {
		
		qqSsoHandler = new UMQQSsoHandler(ActivityLogin.this, "1104865852", "9DqGXesmAA2RZz3V");
		
		qqSsoHandler.addToSocialSDK();
		// 授记口令
		mController.doOauthVerify(ActivityLogin.this, SHARE_MEDIA.QQ, new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA platform) {
				Toast.makeText(ActivityLogin.this, "授权开始", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				Toast.makeText(ActivityLogin.this, "授权错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				// 获取用户资料
				Toast.makeText(ActivityLogin.this, "授权完成,即将为您完成后续步骤", Toast.LENGTH_SHORT).show();
				mController.getUserInfo(ActivityLogin.this, new FetchUserListener() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onComplete(int arg0, SocializeUser user) {
						// TODO Auto-generated method stub
						if (user != null) {
							List<SnsAccount> list = user.mAccounts;
							account = list.get(0);
							getInfo();
						}
					}
				});
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				Toast.makeText(ActivityLogin.this, "授权取消", Toast.LENGTH_SHORT).show();
			}
		});

	}

	/**
	 * 获取用户资料
	 */
	private void getInfo() {
		icon = account.getAccountIconUrl();
		userName = account.getUserName();

		token = account.getUsid();
		// 先根据口令判断用户是否是手机绑定者，是的话直接登录，不是的话，跳到绑定界面
		getUserByToken();

	}

	/**
	 * 根据qq口令判断用户是否存在
	 */
	private void getUserByToken() {

		HttpApi.getUserByToken(token, new RequestCallBack<GeneralResponse<User>>() {
			@Override
			public void onSuccess(GeneralResponse<User> responseData, boolean isFromCache) {
				if (responseData.isSuccess()) {
					// 直接登录
					user = responseData.getData();
					cn.andrewlu.community.service.UserManager.getInstance().setLoginUser(user);
					App.getSharedPreferences().edit().putString("token", user.getToken()).commit();
					RongYunUtils.ConnectRongYun(ActivityLogin.this, user.getToken());
					finish();
				}

				else {
					// 绑定用户
					Intent intent = new Intent(ActivityLogin.this, ValidPhone.class);
					intent.putExtra("icon", icon);
					intent.putExtra("userName", userName);
					intent.putExtra("token", token);
					intent.putExtra("type",Constant.QQ);
					startActivity(intent);
					finish();
				}
			}

			@Override
			public GeneralResponse<User> onParseData(String responseData) {
				GeneralResponse<User> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<User>>() {
				}.getType());
				return response;
			}
		});
	}

	public void loginWeiBo() {
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.doOauthVerify(ActivityLogin.this, SHARE_MEDIA.SINA, new UMAuthListener() {
			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
					getWeiBoInfo();
				} else {
					Toast.makeText(ActivityLogin.this, "授权失败", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
			}

			@Override
			public void onStart(SHARE_MEDIA platform) {
			}
		});
	}
	
	private void getWeiBoInfo()
	{
		mController.getPlatformInfo(ActivityLogin.this, SHARE_MEDIA.SINA, new UMDataListener() {
		    @Override
		    public void onStart() {
		        Toast.makeText(ActivityLogin.this, "授权完成,即将为您完成后续步骤", Toast.LENGTH_SHORT).show();
		    }                                              
		    @Override
		        public void onComplete(int status, Map<String, Object> info) {
		            if(status == 200 && info != null){

		                 token=((Long)info.get("uid"))+"";
		                 userName=(String) info.get("screen_name");
		                 icon=(String) info.get("profile_image_url");
		                getUserByWeiBoToken();
		            }else{
		              
		           }
		        }
		
		});
	}
	
	/**
	 * 根据微博口令判断用户是否存在
	 */
	private void getUserByWeiBoToken()
	{
		HttpApi.getUserByWeiBoToken(token, new RequestCallBack<GeneralResponse<User>>() {
			@Override
			public void onSuccess(GeneralResponse<User> responseData, boolean isFromCache) {
				if (responseData.isSuccess()) {
					// 直接登录
					user = responseData.getData();
					cn.andrewlu.community.service.UserManager.getInstance().setLoginUser(user);
					App.getSharedPreferences().edit().putString("token", user.getToken()).commit();
					RongYunUtils.ConnectRongYun(ActivityLogin.this, user.getToken());
					finish();
				}

				else {
					// 绑定用户
					Intent intent = new Intent(ActivityLogin.this, ValidPhone.class);
					intent.putExtra("icon", icon);
					intent.putExtra("userName", userName);
					intent.putExtra("token", token);
					intent.putExtra("type", Constant.WEIBO);
					startActivity(intent);
					finish();
				}
			}

			@Override
			public GeneralResponse<User> onParseData(String responseData) {
				GeneralResponse<User> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<User>>() {
				}.getType());
				return response;
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    /**使用SSO授权必须添加如下代码 */  
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
}
