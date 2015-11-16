package cn.andrewlu.community.widget;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class OrdermanagerPagerAdpter extends android.support.v4.app.FragmentPagerAdapter {
	
	List<Fragment> pages;
	
	public OrdermanagerPagerAdpter(FragmentManager fm, List<Fragment> pages) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.pages = pages;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return pages.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pages.size();
	}

}
