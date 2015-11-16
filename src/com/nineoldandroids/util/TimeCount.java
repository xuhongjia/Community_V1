package com.nineoldandroids.util;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

public class TimeCount extends CountDownTimer {
	View view;
	public TimeCount(long millisInFuture, long countDownInterval ,View view) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		this.view=view;
	}
	@Override
	public void onFinish() {//计时完毕时触发
		((Button) view).setText("重新验证");
		view.setEnabled(true);
	}
	@Override
	public void onTick(long millisUntilFinished){//计时过程显示
		view.setEnabled(false);
		((Button) view).setText(millisUntilFinished /1000+"秒");
	}
}