package cn.andrewlu.community.ui.moments;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.andrewlu.community.App;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.BitMaoUtils;
import cn.andrewlu.community.api.CommentApi;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.Comment;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Image;
import cn.andrewlu.community.entity.UserComments;
import cn.andrewlu.community.entity.UserMoments;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.BaseFragment;
import cn.andrewlu.community.utils.RefreshableView;
import cn.andrewlu.community.utils.RefreshableView.PullToRefreshListener;
import cn.andrewlu.community.widget.CustomImageView;
import cn.andrewlu.community.widget.NineGridlayout;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.CommonAdapterWithTwoData;
import cn.andrewlu.community.widget.common.ViewHolder;

public class MomentsFragment extends BaseFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.moments_fragment, null);
		ViewUtils.inject(this, view);
		return view;
	}
	@ViewInject(R.id.lv_circle_moments)
	private ListView lv_circle_moments;

	@ViewInject(R.id.refreshable_view)
	private RefreshableView refreshable_view;

	@ViewInject(R.id.comments_editText)
	private LinearLayout comments_editText;

	@ViewInject(R.id.comments_edit)
	private EditText comments_edit;
	@ViewInject(R.id.comments_btn)
	private Button comments_btn;
	private int mId;
	private int parentId=-1;
	private List<UserMoments> moments = new ArrayList<UserMoments>();
	private List<List<UserComments>> coments = new ArrayList<List<UserComments>>();
	private CommonAdapterWithTwoData<UserMoments,UserComments> Madapter;
	private boolean referss=false;
	private static final Gson gson = new Gson();
	public void onViewCreated(View v, Bundle b) {
		super.onViewCreated(v, b);
		refreshable_view.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					getMomentsList();
					Thread.sleep(1000);
					refreshable_view.finishRefreshing();
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}, 0);
		getMomentsList();
		initAdapter();
		lv_circle_moments.setAdapter(Madapter);
		comments_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				insertComment();
			}
		});
	}
	public void insertComment(){
		if(mId!=0)
		{
			if(parentId==-1)
			{
				Comment comment = new Comment();
				comment.setContent(comments_edit.getText().toString().trim());
				comment.setmId(mId);
				comment.setParentId(parentId);
				comment.setTime(System.currentTimeMillis());
				comment.setuId(UserManager.getInstance().getLoginUser().getId());
				CommentApi.insertComment(comment,new RequestCallBack<GeneralResponse<Integer>>() {
					@Override
					public void onSuccess(GeneralResponse<Integer> responseData, boolean isFromCache) {
						showToast("评论成功！");
						getMomentsList();
						comments_edit.setText("");
						InputMethodManager inputmanger = (InputMethodManager) comments_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						inputmanger.hideSoftInputFromWindow(comments_edit.getWindowToken(), 0);
						comments_editText.setVisibility(View.GONE);
					}
					@Override
					public GeneralResponse<Integer>  onParseData(String responseData) {
						GeneralResponse<Integer>  response = new Gson().fromJson(responseData,
								new TypeToken<GeneralResponse<Integer> >() {
						}.getType());
						return response;
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						super.onFailure(error, msg);
					}
				});
			}
			else
			{

			}
		}
	}
	public void initAdapter(){
		Madapter = new CommonAdapterWithTwoData<UserMoments,UserComments>(getActivity(),moments ,coments,R.layout.circle_listview_item) {
			@Override
			public void convert(final ViewHolder helper, final UserMoments item ,List<UserComments> childDataList) {
				//设置touch事件
				helper.getConvertView().setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View view, MotionEvent motionevent) {
						switch (motionevent.getAction()) {
						case MotionEvent.ACTION_DOWN:
							if (view != null) {
								InputMethodManager inputmanger = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
								inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
							}
							comments_editText.setVisibility(View.GONE);
							break;
						}
						return true;
					}
				});

				BitMaoUtils.downLoadImage((ImageView)helper.getView(R.id.img_circle_user), item.getImg());

				NineGridlayout ivMore = (NineGridlayout)helper.getView(R.id.iv_ngrid_layout);
				CustomImageView ivOne =(CustomImageView)helper.getView(R.id.iv_oneimage);
				ivOne.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
				//获取item控件
				View tv_circle_isActivity = helper.getView(R.id.tv_circle_isActivity);
				View tv_circle_discuss = helper.getView(R.id.tv_circle_discuss);
				tv_circle_discuss.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mId = item.getId();
						openEdit(helper);
					}
				});
				//给item控件赋值
				helper.setText(R.id.tv_circle_content,item.getContent())
				.setText(R.id.tv_circle_time, new SimpleDateFormat("yyyy年MM月dd日").format(Long.valueOf(item.getTime())))
				.setText(R.id.tv_circle_username, item.getName());

				String[] sourceStrArray = item.getImages().split(";");
				ArrayList<Image> ImageList=new ArrayList<Image>();
				for (int i = 0; i < sourceStrArray.length; i++) {
					ImageList.add(new Image(sourceStrArray[i]));
				}
				if (sourceStrArray.length==0) {
					ivMore.setVisibility(View.GONE);
					ivOne.setVisibility(View.GONE);
				} else {
					if (sourceStrArray.length == 1) {
						ivMore.setVisibility(View.GONE);
						ivOne.setVisibility(View.VISIBLE);
						BitMaoUtils.downLoadImage(ivOne, sourceStrArray[0]);
					} else {
						ivMore.setVisibility(View.VISIBLE);
						ivOne.setVisibility(View.GONE);
						ivMore.setImagesData(ImageList);
					}
				}
				if(item.isActivity())
				{
					tv_circle_isActivity.setVisibility(View.VISIBLE);
				}
				else
				{
					tv_circle_isActivity.setVisibility(View.GONE);
				}
				if(childDataList.size()!=0)
				{
					CommonAdapter<UserComments> childAdapter = new CommonAdapter<UserComments>(getActivity(),childDataList,R.layout.circle_comment_list_item){
						@Override
						public void convert(ViewHolder helper1, UserComments item) {
							if(item!=null){
								helper1.setText(R.id.comment_answer, item.getName())
								.setText(R.id.comment_content, item.getContent());
								if(item.getParentId()!=-1)
								{
									helper1.getView(R.id.comment_isReply).setVisibility(View.VISIBLE);
									helper1.setText(R.id.comment_to_answer, item.getToname());
									helper1.getView(R.id.comment_to_answer).setVisibility(View.VISIBLE);
								}
							}
							else
							{
								return;
							}
						}
					};
					((ListView)helper.getView(R.id.circle_comment_list)).setAdapter(childAdapter);
					if(referss)
					{
						childAdapter.notifyDataSetChanged();
					}
					if(helper.getPosition()==moments.size())
					{
						referss=false;
					}
				}
				else
				{
					helper.getView(R.id.ln_circle_comment).setVisibility(View.GONE);
				}
			}
			public void openEdit(ViewHolder holder){
				setListViewPos(holder.getParent(),holder.getPosition());
				comments_editText.setVisibility(View.VISIBLE);
				comments_edit.setFocusable(true);
				comments_edit.requestFocus();
				InputMethodManager imm= (InputMethodManager) comments_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.RESULT_HIDDEN);

				//holder.getConvertView().setTop(height-holder.getConvertView().getMeasuredHeight());
			}
			public void setListViewPos(ViewGroup list,int pos)
			{
				if (android.os.Build.VERSION.SDK_INT >= 8) {  
					((ListView) list).smoothScrollToPosition(pos);  
				} else {  
					((ListView) list).setSelection(pos);  
				} 
			}
		};
	}
	public void getMomentsList(){
		HttpApi.getMoments(new RequestCallBack<GeneralResponse<List<String[]>>>() {
			@Override
			public void onSuccess(GeneralResponse<List<String[]>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if(responseData.isSuccess()) {
					moments.clear();
					coments.clear();
					App.getSharedPreferences().edit().putBoolean("getMoments", true)
					.putString("data",gson.toJson(responseData.getData())).commit();
					for(String[] s:responseData.getData() ){
						UserMoments moment =gson.fromJson(s[0],UserMoments.class);						
						List<UserComments> comment= gson.fromJson(s[1], new TypeToken<List<UserComments>>(){}.getType());						
						coments.add(comment);
						moments.add(moment);
					}
					referss=true;								
					Madapter.notifyDataSetChanged();
				}
			}

			@Override
			public GeneralResponse<List<String[]>> onParseData(String responseData) {
				GeneralResponse<List<String[]>> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<List<String[]>>>() {
				}.getType());
				return response;
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
			}
		});
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			//相当于Fragment的onResume
			onActive();
		} else {
			//相当于Fragment的onPause
		}
	}
	public void onActive() {
		SharedPreferences preferences = App.getSharedPreferences();
		if(preferences.getBoolean("getMoments",false))
		{
			String s = preferences.getString("data", null);
			if(s==null)
			{
				getMomentsList();
			}
			else {
				moments.clear();
				coments.clear();
				List<String[]> sList =gson.fromJson(s, new TypeToken<List<String[]>>(){}.getType()) ;
				for(String[] sitem : sList){
					UserMoments moment =gson.fromJson(sitem[0],UserMoments.class);						
					List<UserComments> comment= gson.fromJson(sitem[1], new TypeToken<List<UserComments>>(){}.getType());						
					coments.add(comment);
					moments.add(moment);
				}	
			}
		}
		else {
			getMomentsList();
		}
	}
}
