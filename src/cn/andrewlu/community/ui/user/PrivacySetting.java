package cn.andrewlu.community.ui.user;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PrivacySetting extends BaseActivity {
	@ViewInject(value=R.id.title)
	private TextView title;
	@ViewInject(value=R.id.cbox_privacySetting_personalIfo)
	private CheckBox cbox_personalIfo;
	@ViewInject(value=R.id.cbox_privacySetting_addFriend)
	private CheckBox cbox_addFriend;
	@ViewInject(value=R.id.cbox_privacySetting_doorplate)
	private CheckBox cbox_doorplate;
	@ViewInject(value=R.id.back)
	private View btn_back;
	@OnClick(value={R.id.cbox_privacySetting_personalIfo,R.id.cbox_privacySetting_addFriend,R.id.cbox_privacySetting_doorplate,R.id.back})
	public void OnClick(View view){
		boolean personalIfo=cbox_personalIfo.isChecked();
		boolean addFriend=cbox_addFriend.isChecked();
		boolean doorplate=cbox_doorplate.isChecked();
		int uid=1;    //要获取用户id，还没做
		switch(view.getId()){
		case R.id.cbox_privacySetting_personalIfo:{
			setData_privacy(uid,personalIfo,addFriend,doorplate);
			break;
		}
		case R.id.cbox_privacySetting_addFriend:{
			setData_privacy(uid,personalIfo,addFriend,doorplate);
			break;
		}
		case R.id.cbox_privacySetting_doorplate:{
			setData_privacy(uid,personalIfo,addFriend,doorplate);
			break;
		}
		case R.id.back:{			
			finish();
			break;
		}
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacysetting);	
		ViewUtils.inject(this); //注入view和事件
		title.setText("个人隐私设置");
	}
	private void setData_privacy(int uId,boolean showPersonalInfo,boolean showAddress,boolean showAddFriend){
		HttpApi.privacySeettings(uId, showPersonalInfo, showAddress, showAddFriend, new RequestCallBack<GeneralResponse<String>>(){

			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
				Log.e("register", msg);
			}
		});
	}
}
