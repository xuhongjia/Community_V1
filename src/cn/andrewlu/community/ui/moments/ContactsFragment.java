package cn.andrewlu.community.ui.moments;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.BitMaoUtils;
import cn.andrewlu.community.api.UserApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.BaseFragment;
import cn.andrewlu.community.ui.goods.Commoditydetail;
import cn.andrewlu.community.ui.user.RongYunFrindsManager;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;
import io.rong.imkit.RongIM;

public class ContactsFragment extends BaseFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_contacts, null);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@ViewInject(R.id.contancts_list)
	private ListView contancts_list;
	
	private List<User> userData = new ArrayList<User>();
	private CommonAdapter<User> adapter;
	
	public void onViewCreated(View v, Bundle b) {
		super.onViewCreated(v, b);
		getData();
		adapter = new CommonAdapter<User>(getActivity(),userData,R.layout.contacts_list_item) {		
			@Override
			public void convert(ViewHolder helper, User item) {
				helper.setText(R.id.contact_name, item.getName());
				BitMaoUtils.downLoadImage((ImageView) helper.getView(R.id.contact_img), item.getImg());
				
			}
		};
		contancts_list.setAdapter(adapter);
		initItemClickListner();
	}
	public void getData(){
		UserApi.getAllFriends(UserManager.getInstance().getLoginUser().getId(),new RequestCallBack<GeneralResponse<List<User>>>() {
			@Override
			public void onSuccess(GeneralResponse<List<User>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if(responseData.isSuccess()) {
					userData.clear();
					userData.addAll(responseData.getData());
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public GeneralResponse<List<User>> onParseData(String responseData) {
				GeneralResponse<List<User>> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<List<User>>>() {
						}.getType());
				return response;
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
			}
		});
	}
	
	private void initItemClickListner()
	{
		contancts_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				RongIM.getInstance().startPrivateChat(getActivity(),
						userData.get(position).getAccount(), userData.get(position).getName());
			}
		});
	}
}
