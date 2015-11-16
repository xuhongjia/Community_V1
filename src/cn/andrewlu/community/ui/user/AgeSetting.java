package cn.andrewlu.community.ui.user;


import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.UserApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;

public class AgeSetting extends BaseActivity{
	private EditText age_edit;
	private TextView age_but;
	private ImageView back;
	private TextView title;
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.info_change_setting);
		back=(ImageView) findViewById(R.id.back_findpw);
		title=(TextView) findViewById(R.id.title_findpw);
		title.setText("年龄");
		age_edit=(EditText) findViewById(R.id.change_edit);
		age_but=(TextView) findViewById(R.id.password_complete);
		age_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
		age_edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
		Intent intent=getIntent();
		age_edit.setText(intent.getStringExtra("age"));
		age_but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data = new Intent();  
				String text = age_edit.getText().toString();  
				data.putExtra("age", text);  
				setResult(1,data);  
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(age_edit.getWindowToken(), 0);
				UserManager userManager = UserManager.getInstance();
				User u=userManager.getLoginUser();
				u.setAge(Integer.valueOf(text));
				UserApi.setUserInfo(u,new RequestCallBack<GeneralResponse<User>>(){});
				finish();
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(age_edit.getWindowToken(), 0);
				finish();
			}
		});
	}

}
