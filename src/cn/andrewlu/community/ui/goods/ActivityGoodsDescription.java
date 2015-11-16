package cn.andrewlu.community.ui.goods;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;

public class ActivityGoodsDescription extends BaseActivity{
	@ViewInject(value = R.id.goodsdescribe_back)
    private View goodsdescribe_back;
	@ViewInject(value = R.id.goodsdescribe_button)
    private View goodsdescribe_button;
	@ViewInject(value = R.id.goodsdescribe_describe)
    private EditText goodsdescribe_describe;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goodsdescribe);
		ViewUtils.inject(this);
		Intent intent= getIntent();
		if (intent.getStringExtra("description")!=null) {
			goodsdescribe_describe.setText(intent.getStringExtra("description"));
		}		
	}
	@OnClick({R.id.goodsdescribe_back,R.id.goodsdescribe_button})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.goodsdescribe_back:
			finish();
			break;
	    case R.id.goodsdescribe_button:
	    	result();
			break;
		default:
			break;
		}
	}
	public void result(){
		if(goodsdescribe_describe.getText().toString().trim().length()!=0){
    		Intent data = new Intent();
			String text = goodsdescribe_describe.getText().toString().trim();
			data.putExtra("description", text);
			setResult(2, data);
			finish();
    	}
    	else{
    		showToast("内容不能为空，请输入信息");
    	}
	}
}
