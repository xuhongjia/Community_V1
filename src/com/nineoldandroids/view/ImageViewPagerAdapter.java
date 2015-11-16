package com.nineoldandroids.view;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageViewPagerAdapter extends PagerAdapter
{
	private List<ImageView> imageViewList;
	
	
	 public ImageViewPagerAdapter(Context context, List<ImageView> imageViewList) {
		// TODO Auto-generated constructor stub
		 this.imageViewList=imageViewList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageViewList==null?0:imageViewList.size();
	}

	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(imageViewList.get(position));
	}
	
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(imageViewList.get(position));
		return imageViewList.get(position);
	}
	
}
