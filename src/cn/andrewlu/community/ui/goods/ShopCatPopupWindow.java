package cn.andrewlu.community.ui.goods;

import java.util.ArrayList;
import java.util.List;

import com.aps.ad;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.entity.ShopCat;
import net.tsz.afinal.bitmap.core.BitmapDisplayConfig.AnimationType;

public class ShopCatPopupWindow extends PopupWindow
{
	private ListView listView;
	private ShopCatAdapter adapter;
	private TextView tvClearShopcat;
	public ShopCatPopupWindow(int width,int height,final Context c,ArrayList<ShopCat> list)
	{
		super(c);
		
		LayoutInflater inflater=LayoutInflater.from(c);
		View view= inflater.inflate(R.layout.shopcat_list, null);
		listView=(ListView)view.findViewById(R.id.list);
		tvClearShopcat=(TextView)view.findViewById(R.id.clearShopCat);
		tvClearShopcat.setClickable(true);
		tvClearShopcat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShopCatManager.getInstantce(c);
				ShopCatManager.clearShopCat();
				if(ShopCatPopupWindow.this.isShowing())
					ShopCatPopupWindow.this.dismiss();
				Toast.makeText(c, "清除购物车成功", Toast.LENGTH_SHORT).show();
			}
		});
		if(list==null)
			list=new ArrayList<ShopCat>();
		adapter=new ShopCatAdapter(c, list);
		listView.setAdapter(adapter);
		setContentView(view);
		setAnimationStyle(AnimationType.userDefined);
		setWidth(width);
		setHeight(LayoutParams.WRAP_CONTENT);
		
		this.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if(keyCode==KeyEvent.KEYCODE_MENU&&ShopCatPopupWindow.this.isShowing())
				{
					ShopCatPopupWindow.this.dismiss();
					return true;
				}
				return false;
			}
		});
	}
	
	public  void update()
	{
		adapter.notifyDataSetChanged();
	}
}
