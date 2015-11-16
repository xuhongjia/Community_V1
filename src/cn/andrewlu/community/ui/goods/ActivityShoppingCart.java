package cn.andrewlu.community.ui.goods;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.ui.collection.MyCollection;
import cn.andrewlu.community.ui.collection.MyCollectionAdapter;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * Created by lenovo on 2015/9/10.
 */

public class ActivityShoppingCart extends BaseActivity {

	@ViewInject(id = R.id.shoppingcart_BtnClean, click = "onClick")
	private View shoppingcart_BtnClean;

	@ViewInject(id = R.id.shoppingcart_back, click = "onClick")
	private View shoppingcart_back;

	@ViewInject(id = R.id.shoppingcart_button, click = "onClick")
	private View shoppingcart_button;// 确认下单按钮

	@ViewInject(id = R.id.myshoppingcart_list)
	private ListView list;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_shoppingcart);
		FinalActivity.initInjectedView(this);
	}



	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.shoppingcart_BtnClean: {

			break;
		}
		case R.id.shoppingcart_back: {
			finish();
			break;
		}

		}
	}
}
