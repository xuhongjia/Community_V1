package cn.andrewlu.community.api;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import android.util.Log;

import cn.andrewlu.community.App;
import cn.andrewlu.community.entity.Friend;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Moments;
import cn.andrewlu.community.entity.Token;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class HttpApi extends API {

	public static void login(String username, String password,
			final RequestCallBack<GeneralResponse<User>> callBack) {
		// 从本地获取一个token值.提交后台进行查询,如果有此token的用户,则直接返回相应信息.
		// 融云拿到token可 以直接进行连接.
		String token = App.getSharedPreferences().getString("token", "");
		RequestParams params = new RequestParams("account", username,
				"password", password, "token", token);
		String url = URL.concat("/action/user/login");
		http.send(HttpMethod.POST, url, params,
				new RequestCallBack<GeneralResponse<User>>() {
			public GeneralResponse<User> onParseData(String response) {
				return new Gson().fromJson(response,
						new TypeToken<GeneralResponse<User>>() {
				}.getType());
			}

			public void onSuccess(final GeneralResponse<User> data,
					boolean isFromCache) {
				if (data.isSuccess()) {
					loginRY(data.getData(), new ConnectCallback() {

						@Override
						public void onSuccess(String arg0) {
							UserManager userManager = UserManager
									.getInstance();
							userManager.setLoginUser(data.getData());

							callBack.onSuccess(data, false);
							App.getSharedPreferences()
							.edit()
							.putString("token",
									data.getData().getToken())
									.commit();

							callBack.onFinished();
						}

						@Override
						public void onError(ErrorCode arg0) {
							// TODO Auto-generated method stub
							callBack.onFailure(
									new HttpException(arg0.getValue()),
									arg0.getMessage());
							callBack.onFinished();
						}

						@Override
						public void onTokenIncorrect() {
							// TODO Auto-generated method stub
							callBack.onFailure(new HttpException(
									"用户信息已过期,请重新登录."), "用户信息已过期,请重新登录.");
							callBack.onFinished();
						}
					});
				}
				else
				{
					callBack.onSuccess(data, isFromCache);
					callBack.onFinished();
				}
			}

			public void onFailure(HttpException e, String msg) {
				callBack.onFailure(e, msg);
				callBack.onFinished();
			}

		});
	}

	private static void loginRY(User user, ConnectCallback callback) {
		RongIM.connect(user.getToken(), callback);
	}

	/**
	 * HY联系人
	 * 
	 * @param callBack
	 */
	public static void getAllUser(RequestCallBack<List<User>> callBack) {
		String url = URL.concat("/action/user/getAllUser");
		http.send(HttpMethod.POST, url, callBack);
	}

	/**
	 * HY获取店主信息
	 * 
	 * @param callBack
	 */
	public static void getUserById(int id,RequestCallBack<GeneralResponse<User>> callBack) {
		String url = URL.concat("/action/user/view_userInfo");
		RequestParams params=new RequestParams("uid",id);
		http.send(HttpMethod.POST, url,params, callBack);
	}

	/**
	 * HY获取TOken用于登录融云客户端
	 * 
	 * @param callBack
	 */
	public static void getToenk(String phone, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams("phone", phone);
		String url = URL.concat("/action/rongYun/getToken");
		http.send(HttpMethod.POST, url, params, callBack);
		Log.e("sss", "t");
	}

	public static void getMoments(
			RequestCallBack<GeneralResponse<List<String[]>>> requestCallBack) {
		RequestParams params = new RequestParams("uid", UserManager.getInstance().getLoginUser().getId());
		String url = URL.concat("/action/moments/queryMoments");
		http.send(HttpRequest.HttpMethod.POST, url, params,requestCallBack);

	}
	/**
	 * 上传头像
	 * @param file
	 * @param requestCallBack
	 */
	public static void upLoadUserImg(File file ,RequestCallBack<GeneralResponse<String>> requestCallBack){
		String url = URL.concat("/action/upload/filesUpload");
		RequestParams params = new RequestParams();
		params.addBodyParameter("pics", file);
		http.send(HttpMethod.POST,url,params,requestCallBack);
		Log.e("upload", "t");
	}
	/**
	 * 用户注册
	 * @param user
	 * @param requestCallBack
	 */
	public static void registerUser(User user,RequestCallBack<GeneralResponse<String>> requestCallBack)
	{
		RequestParams params = new RequestParams(user);
		String url = URL.concat("/action/user/register");
		http.send(HttpMethod.POST, url,params, requestCallBack);
		Log.e("register", "t");
	}

	public static void privacySeettings(int uId,boolean showPersonalInfo,boolean showAddress,boolean showAddFriend,RequestCallBack<GeneralResponse<String>> requestCallBack){
		RequestParams params = new RequestParams("uId",uId,"showPersonalInfo",showPersonalInfo,"showAddress",showAddress,"showAddFriend",showAddFriend);
		String url = URL.concat("/action/user/privateSettings");
		http.send(HttpMethod.POST, url,params, requestCallBack);
		Log.e("register", "t");
	}

	public static void getUserByPhone(String phone,RequestCallBack<GeneralResponse<User>> callBack)
	{
		String url = URL.concat("/action/user/getUserByPhone");
		RequestParams params=new RequestParams("phone",phone);
		http.send(HttpMethod.POST, url,params, callBack);
	}

	public static void insertToken(Token treeToken,RequestCallBack<GeneralResponse<String>> callBack)
	{
		String url = URL.concat("/action/token/insertToken");
		RequestParams params=new RequestParams(treeToken);
		http.send(HttpMethod.POST, url,params, callBack);
	}

	public static void insertWeiBoToken(Token treeToken,RequestCallBack<GeneralResponse<String>> callBack)
	{
		String url = URL.concat("/action/token/insertWeiBoToken");
		RequestParams params=new RequestParams(treeToken);
		http.send(HttpMethod.POST, url,params, callBack);
	}
	
	public static void insertUser(User user,RequestCallBack<GeneralResponse<String>> callBack)
	{
		String url = URL.concat("/action/user/register");
		RequestParams params=new RequestParams(user);
		http.send(HttpMethod.POST, url,params, callBack);
	}

	public static void updateUser(User user,RequestCallBack<GeneralResponse<String>> callBack)
	{
		String url = URL.concat("/action/user/update_userInfo");
		RequestParams params=new RequestParams(user);
		http.send(HttpMethod.POST, url,params, callBack);
	}

	public static void getUserByToken(String token,RequestCallBack<GeneralResponse<User>> callback)
	{
		String url=URL.concat("/action/token/getUserByToken");
		RequestParams params=new RequestParams("treeToken",token);
		http.send(HttpMethod.POST, url, params,callback);
	}

	public static void getUserByWeiBoToken(String token,RequestCallBack<GeneralResponse<User>> callback)
	{
		String url=URL.concat("/action/token/getUserByWeiBoToken");
		RequestParams params=new RequestParams("treeToken",token);
		http.send(HttpMethod.POST, url, params,callback);
	}
	public static  void upLoadGoodsImg(File file ,RequestCallBack<GeneralResponse<String>> requestCallBack){
		String url = URL.concat("/action/upload/filesUpload");
		RequestParams params = new RequestParams();
		params.addBodyParameter("pics", file);
		http.send(HttpMethod.POST,url,params,requestCallBack);
	}
}
