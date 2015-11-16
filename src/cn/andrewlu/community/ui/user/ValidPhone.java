package cn.andrewlu.community.ui.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Token;
import cn.andrewlu.community.entity.User;

public class ValidPhone extends BaseActivity {
	private String icon;
	private String userName;
	private String sToken;
	private String phone;
	private EditText tvPhone;
	private boolean isExitUser = false;
	private User user;
	private ImageView btClear;
	private ImageButton btBack;
	private ImageView typeView;
	private int type;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_qq_register);
		icon = getIntent().getStringExtra("icon");
		userName = getIntent().getStringExtra("userName");
		sToken = getIntent().getStringExtra("token");
		typeView=(ImageView)findViewById(R.id.typeImage);
		type=getIntent().getIntExtra("type", 0);
		if(type==Constant.QQ)
			typeView.setBackgroundResource(R.drawable.qq_icon);
		if(type==Constant.WEIBO)
			typeView.setBackgroundResource(R.drawable.drawable_login_weibo);
		tvPhone = (EditText) findViewById(R.id.login_qq_phoneNumber);
		phone = tvPhone.getText().toString();
		btClear = (ImageView) findViewById(R.id.login_qq_clear);
		btBack = (ImageButton) findViewById(R.id.login_qq_back);
		btClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvPhone.setText("");
			}
		});
		btBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * 确认用户是否存在
	 */
	public void isUserExits(View view) {
		phone = tvPhone.getText().toString();
		if (phone.equals("") || phone.length() != 11) {
			TranslateAnimation animation = new TranslateAnimation(0, 20, 0, 0);
			animation.setDuration(50);
			animation.setRepeatCount(20);
			animation.setRepeatMode(Animation.REVERSE);
			tvPhone.startAnimation(animation);
			showToast("请输入正确手机号码");
		} else {
			//根据电话号码判断用户是否存在，存在则为老用户，调用用户原来的记录，不存在则新建用户，其中新用户的的头像和名字用
			//qq的昵称和头像替代
			HttpApi.getUserByPhone(phone, new RequestCallBack<GeneralResponse<User>>() {
				@Override
				public void onSuccess(GeneralResponse<User> responseData, boolean isFromCache) {
					if (responseData.isSuccess()) {
						isExitUser = true;
						user = responseData.getData();
					} else {
						isExitUser = false;
						user = new User();
						user.setId(-1);
						user.setAccount(phone);
						user.setImg(icon);
						user.setName(userName);

					}
					goToSure();
				}

				@Override
				public GeneralResponse<User> onParseData(String responseData) {
					// TODO Auto-generated method stub
					GeneralResponse<User> response = new Gson().fromJson(responseData,
							new TypeToken<GeneralResponse<User>>() {
					}.getType());

					return response;
				}
			});
		}

	}

	/**
	 * 跳转确认用户界面
	 */
	private void goToSure() {
		Token token = new Token();
		token.setPhone(phone);
		token.setId(-1);
		if(type==Constant.QQ)
			token.setToken(sToken);
		if(type==Constant.WEIBO)
			token.setWeiBoToken(sToken);
		Intent intent = new Intent(ValidPhone.this, CompleteUser.class);
		intent.putExtra("token", token);
		intent.putExtra("user", user);
		intent.putExtra("type", type);
		intent.putExtra("isExitUser", isExitUser);
		startActivity(intent);
		finish();

	}
}
