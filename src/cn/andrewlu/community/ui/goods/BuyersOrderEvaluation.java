package cn.andrewlu.community.ui.goods;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.OrderAllGoods;
import cn.andrewlu.community.entity.OrderGoods;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;

public class BuyersOrderEvaluation extends BaseActivity {
	
	private String oid;
	private List<OrderAllGoods> goodsData = new ArrayList<OrderAllGoods>();
	private CommonAdapter<OrderAllGoods> adapter_orderlist;
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);	
		setContentView(R.layout.layout_order_evaluation);
		ViewUtils.inject(this); //注入view和事件
		oid = (String) getIntent().getSerializableExtra("oid"); 
		goodsData.addAll((ArrayList<OrderAllGoods>) getIntent().getSerializableExtra("goods"));
		ListView listView=(ListView)findViewById(R.id.lv_evaluation_childitem);	
		adapter_orderlist = new CommonAdapter<OrderAllGoods>(this, goodsData,
				R.layout.layout_evaluation_item){
			@Override
			public void convert(ViewHolder helper, OrderAllGoods item) {			
				
				helper.setText(R.id.tv_evaluation_item_title, item.getTitle());				
				/*helper.setText(R.id.tv_evaluation_item_state);*/			
				helper.setImageByView(R.id.img_evaluation_item_img, item.getImgs().split(";")[0]);

			}
		};
		listView.setAdapter(adapter_orderlist);
	}
	@OnClick(value={R.id.btn_evaluation_submit,R.id.back})
	public void Onclick(View view){
		switch(view.getId()){
		case R.id.back:{
			finish();
			break;
		}
		case R.id.btn_evaluation_submit:{

			break;
		}
		}
	}
}
