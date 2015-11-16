/**
 * 
 */
package cn.andrewlu.community.ui;

import android.app.Activity;
import cn.andrewlu.community.service.UserManager;

/**
 * @author andrewlu 处理需要登录型的界面. 流程为: 进入界面时判断是否需要登录,如果是,则需要做登录逻辑.
 *         如果登录成功,则判断是否是第一次登录成功,如果是,则需要用户初始化内容,如果是登录成功后第二次进入界面, 则不做任何事情.
 * 
 */
public class LoginSuccessFragment extends BaseFragment {

	private boolean isFirstLoginSuccess = true;

	public void onActive() {
		if (UserManager.isUserLogin()) {
			if (isFirstLoginSuccess) {
				isFirstLoginSuccess = false;//
				onLoginSuccess();
			}
		} else {
			isFirstLoginSuccess = true;
			onLoginFailed();
		}
	}

	// 如果需要登录成功逻辑则重写此函数.
	// 后续可以改变返回值,返回是否处理登录成功事件.
	public void onLoginSuccess() {

	}

	public void onLoginFailed() {

	}
}
