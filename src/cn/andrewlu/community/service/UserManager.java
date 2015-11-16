package cn.andrewlu.community.service;

import io.rong.imkit.RongIM;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import cn.andrewlu.community.App;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.ui.user.ActivityLogin;
import cn.andrewlu.community.widget.ConformDialog;

/**
 * Created by andrewlu on 2015/9/6.
 */
public class UserManager {
	private static UserManager _manager = new UserManager();

	private User loginUser = null;

	public static UserManager getInstance() {
		return _manager;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User u) {
		this.loginUser = u;
	}

	public static boolean isUserLogin() {
		return getInstance().getLoginUser() != null;
	}

	public void goToLogin() {
//		Activity currentActivity = ActivityManager.topActivity();
//		ConformDialog.createDialog(currentActivity, false)
//				.setMessage("你还未登录,请登录后再进行操作")
//				.setOkListener(new ConformDialog.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, View v) {
//						dialog.dismiss();
//						showLoginActivity();
//					}
//				}).show();
		showLoginActivity();
	}

	public void logout() {
		App.getSharedPreferences().edit().putString("token", "").commit();
		RongIM.getInstance().logout();
		setLoginUser(null);
	}

	public void showLoginActivity() {
		Intent intent = new Intent(ActivityManager.topActivity(),
				ActivityLogin.class);
		ActivityManager.topActivity().startActivity(intent);
	}
}
