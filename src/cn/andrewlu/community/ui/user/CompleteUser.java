package cn.andrewlu.community.ui.user;

import com.nineoldandroids.util.TimeCount;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.os.Handler.Callback;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.Token;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.widget.ProgressDialogDIY;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class CompleteUser extends BaseActivity implements Callback {
	private Token token;
	private User user;
	private static String APPKEY = "a656aaebb7bc";
	private static String APPSECRET = "4d3d859f42559cff2c64862862f1aa61";
	private TimeCount time;
	private Button btnValid;
	private EditText btCode;
	private boolean isExitUser = false;
	private String phone;
	private Dialog pd;
	private boolean isSuccess=false;
	private String password;
	private EditText tvPsw;
	private ImageView btnPsw;
	private ImageView btnClear;
	private boolean isShowPsw=false;
	private int type=0;
	private ImageButton btBack;
	private ImageView typeView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_qq_register_complete);
		typeView=(ImageView)findViewById(R.id.typeImage);
		token = (Token) getIntent().getSerializableExtra("token");
		isExitUser = getIntent().getBooleanExtra("isExitUser", false);
		user = (User) getIntent().getSerializableExtra("user");
		type=getIntent().getIntExtra("type", 0);
		if(type==Constant.QQ)
			typeView.setBackgroundResource(R.drawable.qq_icon);
		if(type==Constant.WEIBO)
			typeView.setBackgroundResource(R.drawable.drawable_login_weibo);

		phone = user.getAccount();
		btnValid = (Button) findViewById(R.id.login_qq_sendcode);
		btCode = (EditText) findViewById(R.id.login_qq_code);
		tvPsw=(EditText)findViewById(R.id.login_qq_password);
		btnPsw=(ImageView)findViewById(R.id.btn_psw);
		btBack=(ImageButton)findViewById(R.id.login_qq_back);
		btnClear=(ImageView)findViewById(R.id.login_qq_clear);
		time = new TimeCount(60000, 1000, btnValid);// 构造CountDownTimer对象
		btnPsw.setClickable(true);
		btnPsw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isShowPsw=!isShowPsw;
				if(isShowPsw){
					//设置为明文显示
					tvPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
				else {
					//设置为秘闻显示
					tvPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		btnClear.setClickable(true);
		btnClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btCode.setText("");
			}
		});
		btBack.setClickable(true);
		btBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		initSDK();
	}

	// mob获取验证码返回信息
	private void initSDK() {
		// 初始化SDK
		final Handler handler = new Handler(this);
		SMSSDK.initSDK(CompleteUser.this, APPKEY, APPSECRET);
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
		// 注册监听器
		SMSSDK.registerEventHandler(eventHandler);
	}

	@Override
	public boolean handleMessage(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (result == SMSSDK.RESULT_COMPLETE) {

			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
				// 验证成功
				Toast.makeText(CompleteUser.this, "验证成功", Toast.LENGTH_SHORT).show();
				isSuccess = true;
			} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
				// 获取成功
				Toast.makeText(CompleteUser.this, "获取成功", Toast.LENGTH_SHORT).show();
			} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
				// 获取国家信息
			}
		} else {
			((Throwable) data).printStackTrace();
		}
		return false;
	}

	public void getCheckCode(View view) {
		SMSSDK.getVerificationCode("86", phone);
		time.start();
	}

	public void next(View view) {
		password=tvPsw.getText().toString();
//		if(btCode.getText().toString().trim().equals("")||password.equals("")||password.length()<6)
//		{
			TranslateAnimation animation=new TranslateAnimation(0, 20, 0, 0);
			animation.setDuration(50);
			animation.setRepeatCount(20);
			animation.setRepeatMode(Animation.REVERSE);
			btnPsw.startAnimation(animation);
			btCode.startAnimation(animation);
			showToast("请输入密码或者验证码");
//		}else
//		{
//			SMSSDK.submitVerificationCode("86", phone, btCode.getText().toString().trim());
			pd = ProgressDialogDIY.createLoadingDialog(this, "验证中请稍后....");
			pd.show();
//			final Handler handler = new Handler() {
//				@Override
//				public void handleMessage(Message msg) {
//					pd.dismiss();
//					if (isSuccess) {
						//获取用户密码并作为用户的新密码保存
						Intent intent = new Intent(CompleteUser.this, FinishUser.class);
						intent.putExtra("token", token);
						user.setPassword(password);
						intent.putExtra("user", user);
						intent.putExtra("type", type);
						intent.putExtra("isExitUser", isExitUser);
						startActivity(intent);
						finish();
//					} else {
//						showToast("验证码有误或手机号有误，请重新输入");
//					}
//				}
//			};
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						Thread.sleep(2000);
//						handler.sendEmptyMessage(0);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}).start();
//		}
		
	}

}
