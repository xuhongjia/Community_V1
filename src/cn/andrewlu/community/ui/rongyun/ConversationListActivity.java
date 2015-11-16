package cn.andrewlu.community.ui.rongyun;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.andrewlu.community.R;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.BaseFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * HY 会话列表
 * 
 * @author Administrator
 *
 */
public class ConversationListActivity extends Fragment {
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//用户没有登录前显示默认界面
		View view = inflater.inflate(R.layout.rongyun_conversation_list, null);
		//登陆了就显示消息记录
//		if(UserManager.isUserLogin()){
//        }
		//隐藏默认内容
		LinearLayout layout=(LinearLayout)view.findViewById(R.id.comment_friend_list_Layout);
		layout.setVisibility(View.GONE);
		//动态添加消息列表碎片
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        ConversationListFragment  fragment=enterFragment();
        transaction.replace(R.id.comment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
		return view;
	}


	/**
	 * 加载 会话列表 ConversationListFragment
	 */
	private ConversationListFragment enterFragment() {

//		ConversationListFragment fragment = (ConversationListFragment)getActivity().getSupportFragmentManager()
//				.findFragmentById(R.id.Rongyun_conversationList);

		ConversationListFragment fragment=new ConversationListFragment();
		Uri uri = Uri.parse("rong://" +getActivity(). getApplicationInfo().packageName).buildUpon().appendPath("conversationlist")
				.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") // 设置私聊会话非聚合显示
				.appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")// 设置群组会话聚合显示
				.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")// 设置讨论组会话非聚合显示
				.appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")// 设置系统会话非聚合显示
				.build();

		fragment.setUri(uri);
		return fragment;
	}
}
