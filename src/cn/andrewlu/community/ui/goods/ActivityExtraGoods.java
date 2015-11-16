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

public class ActivityExtraGoods extends BaseActivity{
	@ViewInject(value = R.id.goodsextra_back)
    private View goodsextra_back;
	@ViewInject(value = R.id.goodsextra_button)
    private View goodsextra_button;
	@ViewInject(value = R.id.goods_extra)
    private EditText goods_extra;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_extra);
		ViewUtils.inject(this);
		Intent intent= getIntent();
		if (intent.getStringExtra("extra")!=null) {
			goods_extra.setText(intent.getStringExtra("extra"));
		}		
	}
	@OnClick({R.id.goodsextra_back,R.id.goodsextra_button})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.goodsextra_back:
			finish();
			break;
	    case R.id.goodsextra_button:
	    	result();
			break;
		default:
			break;
		}
	}
	public void result(){
    if(goods_extra.getText().toString().trim().length()!=0){
		Intent data = new Intent();
		String text = goods_extra.getText().toString().trim();
		data.putExtra("extra", text);
		setResult(1, data);
		finish();
	}
	else{
		showToast("内容不能为空，请输入信息");
	}
	}
}
