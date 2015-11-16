package cn.andrewlu.community.ui.goods;


import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.andrewlu.community.ui.goods.ExpandTabView;
import cn.andrewlu.community.ui.goods.ViewMiddle;
import cn.andrewlu.community.ui.goods.ViewLeft;
import cn.andrewlu.community.ui.goods.ViewRight;
import cn.andrewlu.community.R;

public class ActivityRent extends Activity {
	
	@ViewInject(value= R.id.pet_back)
	private View pet_back;

	private ExpandTabView expandTabView;
	private ArrayList<View> mViewArray = new ArrayList<View>();
	private ViewMiddle viewMiddle;
	private RentViewLeft viewLeft;
	private ViewRight viewRight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rent);
		ViewUtils.inject(this);
		initView();
		initVaule();
		initListener();
		
	}

	private void initView() {
		
		expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
		viewMiddle = new ViewMiddle(this);
		viewLeft = new RentViewLeft(this);
		viewRight = new ViewRight(this);
		
	}

	private void initVaule() {
		
		mViewArray.add(viewLeft);
		mViewArray.add(viewMiddle);
		mViewArray.add(viewRight);
		ArrayList<String> mTextArray = new ArrayList<String>();
		mTextArray.add("距离");
		mTextArray.add("区域");
		mTextArray.add("距离");
		expandTabView.setValue(mTextArray, mViewArray);
		expandTabView.setTitle(viewLeft.getShowText(), 0);
		expandTabView.setTitle(viewMiddle.getShowText(), 1);
		expandTabView.setTitle(viewRight.getShowText(), 2);
		
	}

	private void initListener() {
		
		viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(viewMiddle, showText);
			}
		});
		
		viewLeft.setOnSelectListener(new RentViewLeft.OnSelectListener() {
			
			@Override
			public void getValue(String showText) {
				
				onRefresh(viewLeft,showText);
				
			}
		});
		
		viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(viewRight, showText);
			}
		});
		
	}
	
	private void onRefresh(View view, String showText) {
		
		expandTabView.onPressBack();
		int position = getPositon(view);
		if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
			expandTabView.setTitle(showText, position);
		}
		Toast.makeText(ActivityRent.this, showText, Toast.LENGTH_SHORT).show();

	}
	
	private int getPositon(View tView) {
		for (int i = 0; i < mViewArray.size(); i++) {
			if (mViewArray.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void onBackPressed() {
		
		if (!expandTabView.onPressBack()) {
			finish();
		}
		
	}
	
	@OnClick({R.id.rent_back})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rent_back: {
			finish();
			break;
		}
	  }
	}

}
