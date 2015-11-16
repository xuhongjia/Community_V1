package cn.andrewlu.community.ui.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nineoldandroids.view.ImageViewPagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.BitMaoUtils;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.entity.ShopCat;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.CartManager;
import io.rong.imkit.RongIM;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * Created by lenovo on 2015/9/7.
 */
public class Commoditydetail extends BaseActivity {
	@ViewInject(id = R.id.detail_back, click = "onClick")
	private View detail_back;
	@ViewInject(id = R.id.detail_love, click = "onClick")
	private View detail_love;
	// @ViewInject(id = R.id.img_detail,click = "onClick")
	// private View img_detail;
	@ViewInject(id = R.id.tv_detail_title)
	private View tv_detail_title;
	@ViewInject(id = R.id.tv_detail_time)
	private View tv_detail_time;
	@ViewInject(id = R.id.tv_detail_up, click = "onClick")
	private View tv_detail_up;
	@ViewInject(id = R.id.tv_detail_down, click = "onClick")
	private View tv_detail_down;
	@ViewInject(id = R.id.tv_detail_price)
	private View tv_detail_price;
	@ViewInject(id = R.id.tv_detail_name)
	private View tv_detail_name;
	@ViewInject(id = R.id.tv_detail_use_time)
	private View tv_detail_use_time;
	@ViewInject(id = R.id.tv_detail_describe)
	private View tv_detail_describe;
	@ViewInject(id = R.id.tv_detail_sales)
	private View tv_detail_sales;
	@ViewInject(id = R.id.rtb_detail_star)
	private View rtb_detail_star;
	@ViewInject(id = R.id.tv_detail_score)
	private View tv_detail_score;
	@ViewInject(id = R.id.tv_detail_evaluation)
	private View tv_detail_evaluation;
	@ViewInject(id = R.id.tv_detail_username)
	private View tv_detail_username;
	@ViewInject(id = R.id.tv_detail_user_type)
	private View tv_detail_user_type;
	@ViewInject(id = R.id.tv_detail_user_phone_number)
	private View tv_detail_user_phone_number;
	@ViewInject(id = R.id.btn_detail_cart, click = "onClick")
	private View btn_detail_cart;
	@ViewInject(id = R.id.btn_detail_seller, click = "onClick")
	private View btn_detail_seller;

	@ViewInject(id = R.id.gotoPayBtn, click = "onClick")
	private View gotoPayBtn;

	private ViewPager viewPager;
	private Goods goods;
	private User user; // 店主信息
	private Handler handler;
	private String[] imgUrlGroup;
	private List<ImageView> viewGroup;
	private int currentIndex = 0;
	private boolean isReverse = false;
	private TextView popupShopCat;
	private ShopCatPopupWindow win;
	private boolean isShow = false;
	private ArrayList<ShopCat> list;
	private boolean isExitShopCat = false;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.commoditydetail_hy);
		FinalActivity.initInjectedView(this);
		goods = (Goods) getIntent().getSerializableExtra("good");
		if (goods != null) {
			imgUrlGroup = goods.getImageUrls();
		}
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		final int width = manager.getDefaultDisplay().getWidth();
		final int height = manager.getDefaultDisplay().getHeight();
		// 初始化购物车管理员
		ShopCatManager.getInstantce(this);

		popupShopCat = (TextView) findViewById(R.id.popupShopCat);
		popupShopCat.setClickable(true);
		popupShopCat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isShow = !isShow;

				if (isShow) {
					list = (ArrayList<ShopCat>) ShopCatManager.queryShopCat();
					win = new ShopCatPopupWindow(width, height / 2,
							Commoditydetail.this, list);
					win.showAsDropDown(popupShopCat, 0, 0);

				} else {

					win.dismiss();

				}
			}
		});

		initOwnerInfo();
		initText();
		initViewPager();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.detail_back: {
			finish();
			break;
		}
		case R.id.detail_love: {
			// 收藏
			final ToggleButton bt = (ToggleButton) detail_love;

			if (bt.isChecked()) {
				showToast("已添加至收藏列表");
			} else {
				showToast("已从收藏列表删除");
			}

			break;
		}
		// case R.id.tv_detail_up: {
		//
		// break;
		// }
		// case R.id.tv_detail_down: {
		//
		// break;
		// }

		case R.id.btn_detail_cart: {
			isExitShopCat = false;
			List<ShopCat> list = ShopCatManager.queryShopCat();
			for (ShopCat shopcat : list) {
				if (shopcat.getgId() == goods.getId()) {
					isExitShopCat = true;
					break;
				}
			}
			if (!isExitShopCat) {
				ShopCat shopcat = new ShopCat();
				shopcat.setgId(goods.getId());
				shopcat.setPrice(goods.getPrice());
				shopcat.setTitle(goods.getTitle());
				shopcat.setCount(1);
				ShopCatManager.addShopCat(shopcat);
				showToast("添加购物车成功");
			} else {
				showToast("该商品已存在购物车");
			}

			break;
		}
		case R.id.btn_detail_seller: {
			// 联系卖家
			if (cn.andrewlu.community.service.UserManager.isUserLogin()) {
				RongIM.getInstance().startPrivateChat(Commoditydetail.this,
						user.getAccount(), user.getName());
				break;
			} else {
				cn.andrewlu.community.service.UserManager.getInstance()
						.goToLogin();
				break;
			}
		}
		case R.id.gotoPayBtn: {
			Intent intent = new Intent(this, PaymentActivity.class);
			intent.putExtra("good", goods);
			startActivity(intent);
			overridePendingTransition(R.anim.enteranim,R.anim.exitanima);
			break;
		}
		}
	}

	private void goToShopCat() {
		CartManager.getInstance().addGoods(goods);

		Intent intent = new Intent(this, ActivityShoppingCart.class);
		startActivity(intent);

	}

	/**
	 * 初始化数据
	 */
	private void initText() {
		((TextView) tv_detail_title).setText(goods.getTitle());
		// ((TextView) tv_detail_time).setText(goods.getPublishTime());
		((TextView) tv_detail_price).setText("￥" + goods.getPrice());
		((TextView) tv_detail_describe).setText(goods.getDescription());
		// ((TextView) tv_detail_name).setText(goods.getClassify() + "");
		((TextView) tv_detail_use_time).setText(goods.getCount() + "");

	}

	/**
	 * 初始化店主信息
	 */
	private void initOwnerInfo() {

		HttpApi.getUserById(goods.getuId(),
				new RequestCallBack<GeneralResponse<User>>() {
					@Override
					public void onSuccess(GeneralResponse<User> responseData,
							boolean isFromCache) {
						user = responseData.getData();
						if (user != null) {
							((TextView) tv_detail_username).setText(user
									.getName() + "");
							((TextView) tv_detail_user_phone_number)
									.setText(user.getAccount() + "");
						} else {
							return;
						}
					}

					@Override
					public GeneralResponse<User> onParseData(String responseData) {
						GeneralResponse<User> result = new Gson().fromJson(
								responseData,
								new TypeToken<GeneralResponse<User>>() {
								}.getType());
						return result;
					}
				});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.adsPagerView);
		initImageViewGroup();
		ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(this,
				viewGroup);
		viewPager.setAdapter(adapter);
		currentIndex = 0;
		isReverse = false;
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					viewPager.setCurrentItem(currentIndex);

					if (!isReverse) {
						currentIndex++;
						if (currentIndex == 4) {
							currentIndex = 3;
							isReverse = true;
						}
					}

					else if (isReverse) {
						currentIndex--;
						if (currentIndex == 0) {
							isReverse = false;
						}
					}
					break;
				}
			}
		};
		tranImage();

	}

	private void initImageViewGroup() {

		viewGroup = new ArrayList<ImageView>();

		for (String uri : imgUrlGroup) {
			ImageView view = new ImageView(this);
			view.setScaleType(ScaleType.CENTER_CROP);
			BitMaoUtils.downLoadImage(view, uri);
			viewGroup.add(view);
		}
	}

	private void tranImage() {

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);

			}
		};
		timer.schedule(task, 0, 2000);
	}

}