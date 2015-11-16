package cn.andrewlu.community.widget;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class MyOnPageChangeListener implements OnPageChangeListener {
	private boolean flag = false;
	private ViewPager mViewPager;

	public MyOnPageChangeListener(ViewPager pager) {
		mViewPager = pager;
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOffscreenPageLimit(3);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		switch (state) {
		case ViewPager.SCROLL_STATE_DRAGGING:
			flag = false;

			break;
		case ViewPager.SCROLL_STATE_SETTLING:
			flag = true;
			break;
		case ViewPager.SCROLL_STATE_IDLE:
			if (mViewPager.getCurrentItem() == mViewPager.getAdapter()
					.getCount() - 1 && !flag) {
				onNextLastPage();
			}
			flag = true;
			break;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		System.out.println(String.format("%d,%f,%d", arg0, arg1, arg2));
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

	// ���������һ��ͼƬ������󻬶���.
	public void onNextLastPage() {
		System.out.println("�Ѿ������һ����.");
	};

	// ��������һ��ͼƬ����ǰ����.
	public void onPrevFirstPage() {

	}
}

