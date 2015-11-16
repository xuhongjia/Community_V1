package cn.andrewlu.community.ui.user;

import com.amap.api.location.AMapLocation;
import com.lidroid.xutils.http.callback.RequestCallBack;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.GdMapApi;
import cn.andrewlu.community.api.GdMapListener;
import cn.andrewlu.community.api.UserApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;

public class AddressSetting extends BaseActivity implements OnClickListener {
	private EditText address_edit;
	private TextView address_but;
	private ImageView back;
	private TextView title;
	private TextView map;
	private LinearLayout location_address;
	private TextView location_address_display;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.info_change_setting);
		back = (ImageView) findViewById(R.id.back_findpw);
		title = (TextView) findViewById(R.id.title_findpw);
		location_address=(LinearLayout) findViewById(R.id.location_address);
		map=(TextView) findViewById(R.id.map_point);
		location_address.setVisibility(View.VISIBLE);
		map.setVisibility(View.VISIBLE);
		title.setText("地址");
		address_edit = (EditText) findViewById(R.id.change_edit);
		address_but = (TextView) findViewById(R.id.password_complete);
		location_address_display=(TextView) findViewById(R.id.location_address_display);
		Intent intent1 = getIntent();
		address_edit.setText(intent1.getStringExtra("address"));
		address_but.setOnClickListener(this);
		location_address.setOnClickListener(this);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(address_edit.getWindowToken(), 0);
				finish();
			}
		});
		map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(AddressSetting.this,AddressMapPonit.class);
				startActivityForResult(i, 1);
			}
		});
		location();
	}
	//回传修改值
	private void location() {
		GdMapApi.getInstance(this).setOnLocationListener(new GdMapListener() {
			public void onLocationChanged(AMapLocation location) {
				if (location != null && location.getAddress() != null) {
					location_address_display.setText(location.getAddress());
				} else {
					location_address_display.setText("定位失败,你已失联");
				}
			}
		});
		GdMapApi.getInstance(this).requestLocation();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == 1) {
			String address = data.getStringExtra("address");
			address_edit.setText(address);
		}

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.password_complete:{
			Intent data = new Intent();
			String text = address_edit.getText().toString();
			data.putExtra("address", text);
			setResult(2, data);
			//关闭软键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(address_edit.getWindowToken(), 0);
			//提交修改
			UserManager userManager = UserManager.getInstance();
			User u=userManager.getLoginUser();
			u.setAddress(text);
			UserApi.setUserInfo(u,new RequestCallBack<GeneralResponse<User>>(){});
			finish();
			break;
		}
		case R.id.location_address:{
			address_edit.setText(location_address_display.getText());
		}
		}
	}

}