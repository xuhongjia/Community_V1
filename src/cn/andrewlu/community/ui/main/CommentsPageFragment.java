package cn.andrewlu.community.ui.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.andrewlu.community.R;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.LoginSuccessFragment;
import cn.andrewlu.community.ui.moments.ActivityMomentWrite;
import cn.andrewlu.community.ui.moments.ContactsFragment;
import cn.andrewlu.community.ui.moments.MomentsFragment;
import cn.andrewlu.community.ui.rongyun.CommentFragment;
import cn.andrewlu.community.widget.CommonFragmentPagerAdapter;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 首页杂货铺子页面.
 */

public class CommentsPageFragment extends LoginSuccessFragment implements
RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

	private ImageView imageView;
	private RadioGroup group;
	private ViewPager pager;
	private final int rbtID[] = new int[] { R.id.rbtnMsg ,R.id.rbtnContact, R.id.rbtnMoment};
	private RadioButton rbGroup[];

	public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle b) {
		View view = inflater.inflate(R.layout.activity_community_circle, null);
		return view;
	}

	public void onInitView(View view) {
		pager = (ViewPager) getView().findViewById(R.id.Comments_ViewPager);
		group = (RadioGroup) getView().findViewById(R.id.community_rbtnGrounp);
		imageView = (ImageView) view.findViewById(R.id.Comments_conversation);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!UserManager.isUserLogin()) {
					UserManager.getInstance().goToLogin();
				} else {
					if(pager.getCurrentItem()==0)
					{
						RongIM.getInstance().startPrivateChat(getActivity(),
								"15766228139", "");
					}else if(pager.getCurrentItem()==1)
					{
						
					}else if(pager.getCurrentItem()==2)
					{
						Intent intent = new Intent(getActivity(),ActivityMomentWrite.class);
						startActivity(intent);
					}
					
				}
			}
		});
	}

	public void onLoginFailed() {
		pager.setAdapter(null);
	}

	// 重写父类的此函数,用于登录成功时初始化界面.
	public void onLoginSuccess() {
		List<Fragment> list = new ArrayList<Fragment>();
		list.add(instanceChartListFragment());
		list.add(new ContactsFragment());
		list.add(new MomentsFragment());
		CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(
				getChildFragmentManager(), list);
		pager.setAdapter(adapter);
		group.setOnCheckedChangeListener(this);
		pager.setOnPageChangeListener(this);
		rbGroup = new RadioButton[rbtID.length];
		for (int i = 0; i < rbtID.length; ++i) {
			rbGroup[i] = (RadioButton) getView().findViewById(rbtID[i]);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rbtnMsg:
			pager.setCurrentItem(0,false);
			break;
		case R.id.rbtnContact:
			pager.setCurrentItem(1,false);
			break;
		case R.id.rbtnMoment:
			pager.setCurrentItem(2,false);
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		rbGroup[arg0].setChecked(true);
	}

	private ConversationListFragment instanceChartListFragment() {

		ConversationListFragment fragment = new ConversationListFragment();

		Uri uri = Uri
				.parse("rong://"
						+ getActivity().getApplicationInfo().packageName)
						.buildUpon()
						.appendPath("conversationlist")
						.appendQueryParameter(
								Conversation.ConversationType.PRIVATE.getName(),
								"false") // 设置私聊会话非聚合显示
								.appendQueryParameter(
										Conversation.ConversationType.GROUP.getName(), "true")// 设置群组会话聚合显示
										.appendQueryParameter(
												Conversation.ConversationType.DISCUSSION.getName(),
												"false")// 设置讨论组会话非聚合显示
												.appendQueryParameter(
														Conversation.ConversationType.SYSTEM.getName(), "false")// 设置系统会话非聚合显示
														.build();

		fragment.setUri(uri);
		return fragment;
	}
	
	
	
}
