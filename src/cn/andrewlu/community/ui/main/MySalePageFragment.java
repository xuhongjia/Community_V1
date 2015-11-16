package cn.andrewlu.community.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.LoginSuccessFragment;
import cn.andrewlu.community.ui.goods.ActivityMyGoods;
import cn.andrewlu.community.ui.goods.ActivityOrderMananger;
import cn.andrewlu.community.ui.goods.ActivityReleaseGoods;
import cn.andrewlu.community.ui.goods.ActivityServiceHall;
import cn.andrewlu.community.ui.goods.ActivityShoppingCart;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 买卖页面.
 */
public class MySalePageFragment extends LoginSuccessFragment{
	/*@ViewInject(value=R.id.tv_business_commoditymanagement)
	private View commoditymanagement;
	@ViewInject(value=R.id.tv_business_gouwuche)
	private View gouwuche;
	@ViewInject(value=R.id.tv_business_marketingpromotion)
	private View marketingpromotion;
	@ViewInject(value=R.id.tv_business_order)
	private View order;
	@ViewInject(value=R.id.tv_business_releaseshop)
	private View releaseshop;
	@ViewInject(value=R.id.tv_business_servicehall)
	private View servicehall;
	@ViewInject(value=R.id.tv_business_shopdecoration)
	private View shopdecoration;
	@ViewInject(value=R.id.tv_business_shopsetting)
	private View shopsetting;
	@ViewInject(value=R.id.tv_business_tongji)
	private View tongji;*/
	User u;
	public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle b) {	
		return inflater.inflate(R.layout.my_sale_page, null);	

	}
	public void onLoginSuccess(){
		ViewUtils.inject(this,getView());//注入view和事件
		UserManager userManager = UserManager.getInstance();
		u=userManager.getLoginUser();	
	}

	@OnClick(value={R.id.tv_business_commoditymanagement,R.id.tv_business_gouwuche,R.id.tv_business_order,R.id.tv_business_releaseshop,
			R.id.tv_business_servicehall,R.id.tv_business_shopdecoration,R.id.tv_business_shopsetting,R.id.tv_business_tongji})
	public void OnClick(View view){
		Intent intent;
		switch(view.getId()){
		case R.id.tv_business_commoditymanagement:{
			intent = new Intent(getActivity(), ActivityMyGoods.class);
			intent.putExtra("uId", u.getId());
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			break;
		}
		case R.id.tv_business_gouwuche:{
			intent = new Intent(getActivity(), ActivityShoppingCart.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

			break;
		}
		case R.id.tv_business_order:{
			intent = new Intent(getActivity(), ActivityOrderMananger.class);
			/*intent.putExtra("uId", u.getId());*/
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			break;
		}
		case R.id.tv_business_releaseshop:{			
			intent = new Intent(getActivity(), ActivityReleaseGoods.class);
			intent.putExtra("uId", u.getId());
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			break;
		}
		case R.id.tv_business_servicehall:{
			Intent i=new Intent(mContext,ActivityServiceHall.class);
            startActivity(i);
			break;
		}
		case R.id.tv_business_shopdecoration:{

			break;
		}
		case R.id.tv_business_shopsetting:{

			break;
		}
		case R.id.tv_business_tongji:{

			break;
		}
		}
	}

}


