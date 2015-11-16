package cn.andrewlu.community.ui.goods;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.andrewlu.community.R;

public class ActivityServiceHall extends Activity {
	private TextView title;
	private CheckBox drop_down_list;
	private LinearLayout big_icon;
	private LinearLayout small_icon;
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.service_hall);
		init();
	}
	private void init() {
		title=(TextView) findViewById(R.id.title);
		drop_down_list=(CheckBox) findViewById(R.id.drop_down_list);
		big_icon=(LinearLayout) findViewById(R.id.big_icon);
		small_icon=(LinearLayout) findViewById(R.id.small_icon);
		title.setText("服务大厅");
		drop_down_list.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					big_icon.setVisibility(View.GONE);
					small_icon.setVisibility(View.VISIBLE);
					drop_down_list.setText("收起");
				}
				else{
					big_icon.setVisibility(View.VISIBLE);
					small_icon.setVisibility(View.GONE);
					drop_down_list.setText("下拉");
				}
			}
		});
		
	}

}
