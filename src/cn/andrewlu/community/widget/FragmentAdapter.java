package cn.andrewlu.community.widget;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class FragmentAdapter extends FragmentPagerAdapter {
	private List<Fragment> pages = new ArrayList<Fragment>();

	public FragmentAdapter(FragmentActivity fm, int[] imgRes) {
		super(fm.getSupportFragmentManager());
		for (int id : imgRes) {
			ImageFragment fragment = new ImageFragment(id);
			pages.add(fragment);
		}
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

class ImageFragment extends Fragment {
	private int resId;

	public ImageFragment(int id) {
		this.resId = id;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle bundle) {

		ImageView imageView = new ImageView(parent.getContext());
		imageView.setImageResource(resId);
		imageView.setScaleType(ScaleType.CENTER_CROP);

		return imageView;
	}
}

