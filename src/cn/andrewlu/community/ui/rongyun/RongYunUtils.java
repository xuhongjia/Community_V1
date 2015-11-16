package cn.andrewlu.community.ui.rongyun;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import cn.andrewlu.community.App;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.Friend;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.user.RongYunFrindsManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * HY
 * 融云助手
 * @author Administrator
 *
 */
public class RongYunUtils implements UserInfoProvider
{
	private static List<User> userList;
	private static List<Friend> list;
	private UserInfo info;
	/**
	 * 连接融云服务器
	 * @param c 
	 * @param token  用户令牌
	 */
	public static void ConnectRongYun(final Context c,String token)
	{
		RongYunFrindsManager.getInstance(c);
		if(c.getApplicationInfo().packageName.equals(App.getCurrentPidName(c.getApplicationContext())))
		{
			RongIM.connect(token, new ConnectCallback ()
			{

				@Override
				public void onTokenIncorrect() {
					// TODO Auto-generated method stub
					Toast.makeText(c.getApplicationContext(), "获取令牌失败", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onError(ErrorCode arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(c.getApplicationContext(), "服务器故障,请稍候重试", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(String arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(c.getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				
				}
		
			});
		}
		
		
	}
	/**
	 * 展示会话列表
	 * @param c
	 */
	public static void startConversationList(Context c)
	{
		RongIM.getInstance().startConversationList(c);

		
	}
	
	public void getFriendList()
	{
		RongIM.setUserInfoProvider(this, false);  
	}
	
	/**
	 * 进入私聊页面
	 * @param c
	 * @param targetId
	 * @param title
	 */
	public static void startConversation(Context c,String targetId,String title)
	{
		final Uri uri=Uri.parse("");
		RongIM.getInstance().startConversation(c, Conversation.ConversationType.PRIVATE, "Harden", "聊天标题");
	}
	
	@Override
	public UserInfo getUserInfo(final String uId) {
		
		
		
		list=RongYunFrindsManager.getAllFriends();
		if(list==null)
		{
			
		}
		else
		{
			for(Friend friend:list)
			{
				if(friend.getPhone()==uId)
				{
					info=new UserInfo(friend.getPhone(), friend.getName(), Uri.parse(friend.getImgs()));
					
				}
			}
		}
		return info;
	}
	protected static void tranFriend() {
		// TODO Auto-generated method stub
		if(userList!=null)
		{
			list=new ArrayList<Friend>();
			for(User user:userList)
			{
				Friend friend=new Friend();
				friend.setId(user.getId());
				friend.setName(user.getName());
				friend.setPhone(user.getAccount());
				friend.setImgs(user.getImg());
				list.add(friend);
			}
		}
		
	
	
	}

	
	
	
}
