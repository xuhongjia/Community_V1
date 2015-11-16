package cn.andrewlu.community.ui.user;


import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import android.content.Context;
import cn.andrewlu.community.entity.Friend;
import cn.andrewlu.community.entity.User;

public class RongYunFrindsManager {

	private static DbUtils utils;


	public static void getInstance(Context c) {
		if (utils == null)
			utils = DbUtils.create(c, "frinds.db");
		try {
			utils.createTableIfNotExist(Friend.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void saveFriends(List<User> userList) {
		try {
			// TODO Auto-generated method stub
			if(userList!=null)
			{
				ArrayList<Friend>list=new ArrayList<Friend>();
				for(User user:userList)
				{
					Friend friend=new Friend();
					friend.setId(user.getId());
					friend.setName(user.getName());
					friend.setPhone(user.getAccount());
					friend.setImgs(user.getImg());
					list.add(friend);
				}
				utils.saveAll(list);
			}
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Friend> getAllFriends() {
		List<Friend> list;
		try {
			return list = utils.findAll(Friend.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
