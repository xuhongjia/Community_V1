package cn.andrewlu.community.widget;

import com.lidroid.xutils.view.annotation.ViewInject;

import net.tsz.afinal.FinalActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;

public class ImageDetial extends BaseActivity {

	@ViewInject(R.id.igv_iamge_detial)
	private ViewPager igv_iamge_detial;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.image_detial);
		FinalActivity.initInjectedView(this);

		Intent intent = getIntent();
		String url = intent.getStringExtra("imagesrc");

	}
}
