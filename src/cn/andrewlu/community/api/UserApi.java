package cn.andrewlu.community.api;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;

import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.ui.user.RongYunFrindsManager;

public class UserApi extends API {
	public static void setUserInfo(User user, RequestCallBack<GeneralResponse<User>> requestCallBack)
	{
		RequestParams params = new RequestParams(user);
		String url = URL.concat("/action/user/update_userInfo");
		http.send(HttpMethod.POST, url,params, new RequestCallBack<GeneralResponse<User>>(){
			@Override
			public void onSuccess(ResponseInfo<Object> data,
					boolean isFromCache) {
				GeneralResponse<User> res = new Gson().fromJson(String.valueOf(data.result), new TypeToken<GeneralResponse<User>>(){}.getType());
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				System.out.print(msg);
			}
		});
	}
	public static void updatepwd(String account,String password ,RequestCallBack<GeneralResponse<String>> requestCallBack)
	{
		RequestParams params = new RequestParams();
		params.addBodyParameter("account", account);
		params.addBodyParameter("password", password);
		String url = URL.concat("/action/user/update_pwd");
		http.send(HttpMethod.POST, url,params, requestCallBack);
	}
	
	public static void queryAllUser()
	{
		HttpApi.getAllUser(new RequestCallBack<List<User>>() {
			@Override
			public void onSuccess(List<User> responseData, boolean isFromCache) {
				List<User> list=responseData;
				RongYunFrindsManager.saveFriends(list);
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				Log.i(msg);
			}
			@Override
			public List<User> onParseData(String responseData) {
				List<User> response=new Gson().fromJson(responseData,
						new TypeToken<List<User>>(){}.getType());
				
				return response;
			}
		});
	}
	
	//联系人
	public static void getAllFriends(int uid , RequestCallBack<GeneralResponse<List<User>>> requestCallBack){
		RequestParams params = new RequestParams("uid",uid);
		String url = URL.concat("/action/friends/getAllFriends");
		http.send(HttpMethod.POST, url,params, requestCallBack);
	}
}

