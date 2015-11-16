package cn.andrewlu.community.api;

import java.io.File;
import java.util.List;

import cn.andrewlu.community.entity.Comment;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Moments;
import cn.andrewlu.community.entity.User;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class CommentApi extends API {
	public static void insertComment(Comment comment , RequestCallBack<GeneralResponse<Integer>> requestCallBack){
		RequestParams params = new RequestParams(comment);
		String url = URL.concat("/action/comment/insertComment");
		http.send(HttpMethod.POST, url,params, requestCallBack);
	}
	
	public static void uploadImgs(File[] files ,  RequestCallBack<GeneralResponse<String>> requestCallBack){
		RequestParams params = new RequestParams();
		for(int i = 0; i<files.length; i++)
		{
			params.addBodyParameter("pics", files[i]);
		}
		String url = URL.concat("/action/upload/filesUpload");
		http.send(HttpMethod.POST, url,params, requestCallBack);
	}
	
	public static void insertMoment(Moments moments , RequestCallBack<GeneralResponse<Integer>> requestCallBack)
	{
		RequestParams params = new RequestParams(moments);
		String url = URL.concat("/action/moments/insertMoments");
		http.send(HttpMethod.POST, url,params, requestCallBack);
	}
}
