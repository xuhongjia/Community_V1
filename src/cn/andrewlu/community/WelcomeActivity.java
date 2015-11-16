package cn.andrewlu.community;

import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.ImageView;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.widget.DepthPageTransformer;
import cn.andrewlu.community.widget.FragmentAdapter;
import cn.andrewlu.community.widget.MyOnPageChangeListener;

public class WelcomeActivity extends BaseActivity {
	private int[] mImgIds = new int[] { R.drawable.guide_image1,
			R.drawable.guide_image2, R.drawable.guide_image3 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		SharedPreferences preferences = App.getSharedPreferences();
		boolean isFirstLogin = preferences.getBoolean("firstLogin", true);
		if (!isFirstLogin) {
			// 显示一张默认的欢迎界面.并且计时2秒后自动跳转转.
//			ImageView imgView = (ImageView) findViewById(R.id.bg);
//			imgView.setImageResource(R.drawable.guide_image1);

			autoLogin();
		} else {
			preferences.edit().putBoolean("firstLogin", false).commit();

			ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_viewpager);

			// 应该先判断是否是第一次运行,如果不是,就不用加载viewpager图片.而是显示一张默认的欢迎界面.

			// 为viewpage添加动画3.0以上有效
			mViewPager.setPageTransformer(true, new DepthPageTransformer());
			mViewPager.setAdapter(new FragmentAdapter(this, mImgIds));

			new MyOnPageChangeListener(mViewPager) {
				public void onNextLastPage() {
					gotoMain();
				}
			};
		}
	}

	private void gotoMain() {
		Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	}

	private void autoLogin() {
		String token = App.getSharedPreferences().getString("token", "");
		if ("".equals(token)) {
			// token 为空,不能自动登录.

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					gotoMain();
				}
			}, 1000);
			return;
		}
		HttpApi.login("", "", new RequestCallBack<GeneralResponse<User>>() {
			public void onSuccess(GeneralResponse<User> result,
					boolean isFromCache) {
				if (result.isSuccess()) {
				} else {
					showToast("用名或密码错误");
				}
			}

			public void onFinished() {
				gotoMain();
			}
		});
	}

}
