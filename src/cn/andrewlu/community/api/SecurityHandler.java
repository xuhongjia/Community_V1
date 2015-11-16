/**
 * 
 */
package cn.andrewlu.community.api;

import java.util.List;

import org.apache.http.NameValuePair;

import com.lidroid.xutils.http.ISecureInterceptor;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sea_monster.common.Md5;

import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;

/**
 * @author andrewlu
 * 
 */
public class SecurityHandler implements ISecureInterceptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lidroid.xutils.http.ISecureInterceptor#preSend(com.lidroid.xutils
	 * .http.client.HttpRequest, com.lidroid.xutils.http.RequestParams)
	 */
	@Override
	public RequestParams preSend(HttpRequest request, RequestParams params) {
		String ex = "xe";
		if (params == null) {
			params = new RequestParams();
		}
		boolean flag = false;
		String token = "";
		if (UserManager.isUserLogin()) {
			User u = UserManager.getInstance().getLoginUser();
			flag = true;
			token = u.getToken();
		}
		// 1. 对所有参数进行加密.
		List<NameValuePair> bodysList = params.getBodys();
		StringBuffer signKeys = new StringBuffer();
		StringBuffer signValues = new StringBuffer();
		StringBuffer temp = new StringBuffer();
		if (bodysList != null) {
			for (NameValuePair body : bodysList) {
				if (body.getValue() != null) {
					signKeys.append(body.getName());
					signKeys.append(";");
					signValues.append(body.getValue());
					signValues.append("e");
				}
			}
		}
		char[] charBody = signKeys.toString().toCharArray();
		for(int i = 0; i<charBody.length;i++)
		{
			if(i%2==0)
			{
				charBody[i]--;
			}else 
			{
				charBody[i]++;
			}
		}
		temp.append(String.valueOf(charBody));
		long date = System.currentTimeMillis();
		params.addHeader("sk__", temp.toString());
		signValues.append(flag).append(token).append(signKeys)
		.append(date);
		params.addHeader("sg__", Md5.encode(signValues.toString()));
		params.addHeader("d__", String.valueOf(date));
		params.addHeader("f__", String.valueOf(flag));
		params.addHeader("token",token);
		return params;
	}

}
