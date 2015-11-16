package cn.andrewlu.community.ui.goods;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.widget.OrdermanagerPagerAdpter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ActivityOrderMananger extends BaseActivity implements
RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener{
	
	private RadioGroup group;
	private ViewPager pager;
	private final int rbtID[] = new int[] { R.id.rbtn_ordermanager_buyers, R.id.rbtn_ordermanager_shops };
	private RadioButton rbGroup[];
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_ordermanager);
		ViewUtils.inject(this);//注入view和事件
		pager = (ViewPager) findViewById(R.id.business_ViewPager);
		group = (RadioGroup) findViewById(R.id.rgrout_business_rgrout);
		List<Fragment> list = new ArrayList<Fragment>();
		list.add(new BuyesOrderFragment());
		list.add(new ShopsOrderFragment());		
		OrdermanagerPagerAdpter adapter = new OrdermanagerPagerAdpter(
				getSupportFragmentManager(), list);
		pager.setAdapter(adapter);
		// 控制预加载的页面个数.缓存页面的个数.
		pager.setOffscreenPageLimit(2);
		group.setOnCheckedChangeListener(this);
		pager.setOnPageChangeListener(this);
		rbGroup = new RadioButton[rbtID.length];
		for (int i = 0; i < rbtID.length; ++i) {
			rbGroup[i] = (RadioButton) findViewById(rbtID[i]);
		}	
	}
	/*@ViewInject(value=R.id.back)
	private View back;
	@ViewInject(value=R.id.btn_ordermanager_update)
	private View update;*/
	@OnClick(value={R.id.back,R.id.btn_ordermanager_update})
	public void OnClick(View view){	
		switch(view.getId()){
		case R.id.back:{		
			finish();
			break;
		}
		case R.id.btn_ordermanager_update:{
			
			break;
		}
		}
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rbtn_ordermanager_buyers:
			pager.setCurrentItem(0, true);
			break;
		case R.id.rbtn_ordermanager_shops:
			pager.setCurrentItem(1, true);
		default:
			pager.setCurrentItem(1, true);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		rbGroup[arg0].setChecked(true);
	}

	
}
