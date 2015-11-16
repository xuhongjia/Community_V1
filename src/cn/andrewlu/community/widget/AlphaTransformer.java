package cn.andrewlu.community.widget;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class AlphaTransformer implements PageTransformer {

	// 些函数每次滑动一个距离会被执行两次， 分别是左边的那个View的当前位置比例。和右边那个View的比例。
	// 左边View如果超出左边界比例值为负。 初始时候值为0.
	@Override
	public void transformPage(View arg0, float position) {
		// TODO Auto-generated method stub
		// System.out.println("v:" + position);

		float p = 1 - Math.abs(position);
		ViewHelper.setAlpha(arg0, p);

		// if (position <= 0) {
		// // 从右向左滑动为当前View
		// System.out.println("v1:" + p);
		// ViewHelper.setAlpha(arg0, p);
		//
		// } else if (position <= 1) {
		// // 从左向右滑动为当前View
		// System.out.println("v2:" + p);
		// ViewHelper.setAlpha(arg0, p);
		//
		// }
	}

}
