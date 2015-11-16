package cn.andrewlu.community.ui.rongyun;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.User;

public class CommentFriendsActivity extends Activity
{
	private ListView listView;
	private CommentFriendAdapter adapter;
	private List<User> list;
	private String response;
	private Gson gson=new Gson();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("sss","lis");
		setContentView(R.layout.comment_friend_layout);
		listView=(ListView)findViewById(R.id.comment_friend_list);
		Log.e("sss","list");
//		HttpApi.getAllUser(new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(String responseInfo, boolean isFromCache) {
//				// TODO Auto-generated method stub
//				response=responseInfo;
//				Log.e("sss",response);
//				list=gson.fromJson(response, new TypeToken<List<User>>(){}.getType());
//				Log.e("sss",list.size()+"");
//				adapter=new CommentFriendAdapter(CommentFriendsActivity.this, list);
//				listView.setAdapter(adapter);
//				adapter.notifyDataSetChanged();
//			}
//			@Override
//			public void onFailure(HttpException error, String msg) {
//				// TODO Auto-generated method stub
//				Toast.makeText(CommentFriendsActivity.this, "请检查网络"+msg, Toast.LENGTH_SHORT).show();
//				
//			}
//			@Override
//			public void onLoading(long total, long current, boolean isUploading) {
//
//				Log.e("sss","q");
//			}
//		});
//		
	
	}
	
	
}
