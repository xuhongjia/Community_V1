package cn.andrewlu.community.ui;

import cn.andrewlu.community.BaseActivity;
import net.tsz.afinal.FinalActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {
	protected BaseActivity mContext = null;

	public void onViewCreated(View view, Bundle b) {
		mContext = (BaseActivity) getActivity();
		FinalActivity.initInjectedView(this, view);
		onInitView(view);
	}

	// 界面重新显示出来.
	public void onActive() {

	}

	public void onInActive() {
	}

	protected void onInitView(View v) {

	};

	public void showToast(String msg) {
		mContext.showToast(msg);
	}

	public void showToast(int resID) {
		mContext.showToast(resID);
	}
}
