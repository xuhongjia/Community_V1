package cn.andrewlu.community;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation.ConversationType;

import java.util.ArrayList;
import java.util.List;

import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.NotifyStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.callback.RequestCallBack;

import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.api.UserApi;
import cn.andrewlu.community.entity.Friend;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.ActivityManager;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.BaseFragment;
import cn.andrewlu.community.ui.main.CommentsPageFragment;
import cn.andrewlu.community.ui.main.MainPageFragment;
import cn.andrewlu.community.ui.main.MySalePageFragment;
import cn.andrewlu.community.ui.main.PersonalPageFragement;
import cn.andrewlu.community.ui.rongyun.RongYunUtils;
import cn.andrewlu.community.ui.user.RongYunFrindsManager;
import cn.andrewlu.community.widget.AlphaTransformer;
import cn.andrewlu.community.widget.CommonFragmentPagerAdapter;
import cn.andrewlu.community.widget.CubeTransformer;
import cn.andrewlu.community.widget.FixedSpeedScroller;
import cn.andrewlu.community.widget.TipRadioButton;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imlib.model.UserInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.app.Activity;
import android.net.Uri;

public class MainActivity extends BaseActivity implements UserInfoProvider {

	// 用RadioGroup来实现底部导航菜单。
	private RadioGroup menuBar;

	// 用ViewPager来实现页面的切换。
	private ViewPager pager;

	// 菜单组按钮组。
	TipRadioButton[] menuButtons;

	private static List<User> userList;
	private static List<Friend> list;
	private UserInfo info;
	private final int[] menuIds = { R.id.tab_shops, R.id.tab_sale,
			R.id.tab_comments, R.id.tab_personal };
	private List<Fragment> mPagesFragments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// init views
		menuBar = (RadioGroup) findViewById(R.id.menu_bar);
		menuBar.setOnCheckedChangeListener(menuBarListener);
		pager = (ViewPager) findViewById(R.id.pager);
		// 初始化页面.
		mPagesFragments = new ArrayList<Fragment>();
		mPagesFragments.add(new MainPageFragment());
		mPagesFragments.add(new MySalePageFragment());
		mPagesFragments.add(new CommentsPageFragment());
		mPagesFragments.add(new PersonalPageFragement());
		CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(
				getSupportFragmentManager(), mPagesFragments);
		pager.setAdapter(adapter);

		pager.setPageTransformer(false, new AlphaTransformer());
		// pager.setPageTransformer(false, new CubeTransformer());
		// FixedSpeedScroller.fixedScroll(pager);
		// 控制预加载的页面个数.缓存页面的个数.
		pager.setOffscreenPageLimit(4);
		// 设置界面切换监听事件.
		pager.setOnPageChangeListener(pageChangeListener);

		// 初始化四个菜单项
		menuButtons = new TipRadioButton[menuIds.length];
		int i = 0;
		for (int id : menuIds) {
			menuButtons[i++] = (TipRadioButton) findViewById(id);
		}

		//设置用户头像和昵称


		// 获取好友列表信息.

		if (UserManager.isUserLogin()) {
			RongYunFrindsManager.getInstance(this);
			UserApi.queryAllUser();
			RongIM.setUserInfoProvider(this, true);

			// 未读消息数量监听.用于在菜单上显示未读消息 数量.
			RongIM.getInstance().setOnReceiveUnreadCountChangedListener(
					mMessageCountListener, ConversationType.PRIVATE);
		}
	}

	private RongIM.OnReceiveUnreadCountChangedListener mMessageCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
		@Override
		public void onMessageIncreased(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 == 0) {
				menuButtons[2].setTip(null);
			} else {
				menuButtons[2].setTip(arg0 + "");
			}
		}
	};

	public void onResume() {
		super.onResume();
		if (!UserManager.isUserLogin()) {
			pager.setCurrentItem(0, false);
		} else {
			// 根据当前显示的页面进行刷新操作.
			Fragment f = mPagesFragments.get(pager.getCurrentItem());
			if (f instanceof BaseFragment) {
				((BaseFragment) f).onActive();
			}
		}
	}

	// 菜单切换功能.
	private final OnCheckedChangeListener menuBarListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			for (int i = 0; i < menuIds.length; i++) {
				if (arg1 == menuIds[i]) {
					pager.setCurrentItem(i, false);
					// 根据当前显示的页面进行刷新操作.
					Fragment f = mPagesFragments.get(i);
					if (f instanceof BaseFragment) {
						((BaseFragment) f).onActive();
					}
				if(i!=0)
					MainPageFragment.hiddenSubView();
				}
			}
		}
	};

	// ViewPager的切换事件.
	private final OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			menuButtons[arg0].setChecked(true);
			if (arg0 > 0) {
				if (!UserManager.isUserLogin()) {
					UserManager.getInstance().goToLogin();
					// pager.setCurrentItem(0);
				}
				
			}
			//隐藏悬浮窗
			if(arg0!=0)
				MainPageFragment.hiddenSubView();

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			System.out.println(arg0 + "," + arg1 + "," + arg2);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	private long lastBackPressed = 0L;

	public void back() {
		if (System.currentTimeMillis() - lastBackPressed >= 1000) {
			showToast("再按一次退出程序.");
			lastBackPressed = System.currentTimeMillis();
			return;
		} else {
			ActivityManager.getInstance().exitApp();
		}

	}

	/**
	 * 信息提供者，提供给融云
	 */
	@Override
	public UserInfo getUserInfo(String uId) {
		if (UserManager.isUserLogin()) {
			List<Friend> list = RongYunFrindsManager.getAllFriends();
			if (list.size() > 0) {
				//查找特定头像
				for (Friend friend : list) {
					if (friend.getPhone().equals(uId)) {
						info = new UserInfo(uId, friend.getName(),
								Uri.parse(friend.getImgs()));
						return info;
					}
				}
			}
			//不存在在指定为当前用户头像
			return info = new UserInfo(uId, UserManager.getInstance()
					.getLoginUser().getName(), Uri.parse(UserManager
					.getInstance().getLoginUser().getImg()));
		}
		return null;
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
		MainPageFragment.hiddenSubView();
		super.onPause();
	}
	
}
