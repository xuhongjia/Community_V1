package cn.andrewlu.community.ui.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.andrewlu.community.R;
import cn.andrewlu.community.RefreshManager;
import cn.andrewlu.community.api.GdMapApi;
import cn.andrewlu.community.api.GdMapListener;
import cn.andrewlu.community.api.HttpGoodsApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.service.ActivityManager;
import cn.andrewlu.community.ui.LoginSuccessFragment;
import cn.andrewlu.community.ui.goods.ActivityDay;
import cn.andrewlu.community.ui.goods.ActivityEletronic;
import cn.andrewlu.community.ui.goods.ActivityFurniture;
import cn.andrewlu.community.ui.goods.ActivityGreens;
import cn.andrewlu.community.ui.goods.ActivityPet;
import cn.andrewlu.community.ui.goods.ActivityRent;
import cn.andrewlu.community.ui.goods.ActivitySecondhand;
import cn.andrewlu.community.ui.goods.ActivitySearch;
import cn.andrewlu.community.ui.goods.Commoditydetail;
import cn.andrewlu.community.widget.GoodsScrollView;
import cn.andrewlu.community.widget.GoodsScrollView.OnScrollListener;
import cn.andrewlu.community.widget.common.CommonAdapter;
import cn.andrewlu.community.widget.common.ViewHolder;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 首页杂货铺子页面.
 */

public class MainPageFragment extends LoginSuccessFragment implements OnScrollListener {
	private View totalView;
	private GoodsScrollView scollView;
	private LinearLayout subItemLayout,gridLayout;
	private static WindowManager windowManager;
	private static View subView;
	private WindowManager.LayoutParams subParams;
	private int screenWidth;
	private int scrollViewHeight;
	private int subHeight;
	private TextView tvSort;
	private int subTop;
	private LayoutInflater inflater;
	private SimpleDateFormat format;
	private int top;
	public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle b) {
		totalView = inflater.inflate(R.layout.commodity_display, null);
		gridLayout=(LinearLayout)totalView.findViewById(R.id.gridLayout);
		this.inflater = inflater;
		scollView = (GoodsScrollView) totalView.findViewById(R.id.scrollView);
		subItemLayout = (LinearLayout) totalView.findViewById(R.id.subItemLayout);
		windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		screenWidth = windowManager.getDefaultDisplay().getWidth();
		tvSort=(TextView)totalView.findViewById(R.id.tvSort);
		scollView.setOnScrollListener(this);
		format=new SimpleDateFormat("yyyy年MM月dd日");
		return totalView;
	}

	@ViewInject(id = R.id.edt_display_search, click = "onClick")
	private ImageButton edt_display_search;
	@ViewInject(id = R.id.imgbtn_disply_search_clear, click = "onClick")
	private View imgbtn_disply_search_clear;
	@ViewInject(id = R.id.tv_display_location, click = "onClick")
	private TextView tv_display_location;
	@ViewInject(id = R.id.rog_display_list, click = "onClick")
	private View rog_display_list;
	@ViewInject(id = R.id.rbo_display_secondhand, click = "onClick")
	private View rbo_display_secondhand;
	@ViewInject(id = R.id.rbo_display_furniture, click = "onClick")
	private View rbo_display_furniture;
	@ViewInject(id = R.id.rbo_display_electronic, click = "onClick")
	private View rbo_display_electronic;

	@ViewInject(id = R.id.rbo_display_pet, click = "onClick")
	private View rbo_display_pet;

	@ViewInject(id = R.id.rbo_display_rent, click = "onClick")
	private View rbo_display_rent;
	
	@ViewInject(id = R.id.rbo_display_day, click = "onClick")
	private View rbo_display_day;
	
	@ViewInject(id = R.id.rbo_display_greens, click = "onClick")
	private View rbo_display_greens;

	@ViewInject(id = R.id.lv_display_list)
	private ListView lv_display_list;

	@ViewInject(id = R.id.reloactionProgress)
	private ProgressBar relocationProgressBar;

	private final List<Goods> goodsData = new ArrayList<Goods>();

	private CommonAdapter<Goods> adapter;

	public void onInitView(View view) {
		getGoods();

		lv_display_list.setFocusable(false);// 防止初始出現在底部
		adapter = new CommonAdapter<Goods>(mContext, goodsData, R.layout.listview_item) {
			@Override
			public void convert(ViewHolder helper, Goods item) {

				helper.setImageByView(R.id.img_list_commodity, item.getImgs().split(";")[0]);
				helper.setText(R.id.tv_display_title, item.getTitle());
				helper.setText(R.id.tv_display_time, "" + format.format(item.getPublishTime()));
				helper.setText(R.id.tv_display_price, "￥" + item.getPrice());
			}
		};

		lv_display_list.setAdapter(adapter);

		initListView();

		GdMapApi.getInstance(mContext).setOnLocationListener(new GdMapListener() {
			public void onLocationChanged(AMapLocation location) {
				if (location != null && location.getAddress() != null) {
					tv_display_location.setText(location.getAddress());
				} else {
					tv_display_location.setText("定位失败,你已失联");
				}
				relocationProgressBar.setVisibility(View.INVISIBLE);
			}
		});
		GdMapApi.getInstance(mContext).requestLocation();
	}

//	/**
//	 * 獲得listview的總高度
//	 */
//	private void measureListViewHeight() {
//		ListAdapter adapter = lv_display_list.getAdapter();
//		if (lv_display_list == null)
//			return;
//		int viewHeight = 0;
//		for (int i = 0; i < goodsData.size(); ++i) {
//			View view = adapter.getView(i, null, lv_display_list);
//			view.measure(0, 0);
//			viewHeight = viewHeight + view.getMeasuredHeight();
//
//		}
//		ViewGroup.LayoutParams params = lv_display_list.getLayoutParams();
//		params.height = viewHeight + (lv_display_list.getDividerHeight()) * (adapter.getCount() - 1);
//		lv_display_list.setLayoutParams(params);
//	}

	public void onViewCreated(View v, Bundle b) {
		super.onViewCreated(v, b);

	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.edt_display_search: {
			Intent intent = new Intent(getActivity(), ActivitySearch.class);
			startActivity(intent);
			break;
		}
		case R.id.imgbtn_disply_search_clear: {

			break;
		}
		case R.id.tv_display_location: {
			tv_display_location.setText("重新定位中...");
			//relocationProgressBar.setVisibility(View.VISIBLE);
			GdMapApi.getInstance(mContext).requestLocation();
			break;
		}
		case R.id.rbo_display_secondhand: {
			Intent intent = new Intent(getActivity(), ActivitySecondhand.class);
			startActivity(intent);
			break;
		}
		case R.id.rbo_display_furniture: {
			Intent intent = new Intent(getActivity(), ActivityFurniture.class);
			startActivity(intent);
			break;
		}
		case R.id.rbo_display_electronic: {
			Intent intent = new Intent(getActivity(), ActivityEletronic.class);
			startActivity(intent);
			break;
		}
		case R.id.rbo_display_pet: {
			Intent intent = new Intent(getActivity(), ActivityPet.class);
			startActivity(intent);
			break;
		}
		case R.id.rbo_display_rent: {
			Intent intent = new Intent(getActivity(), ActivityRent.class);
			startActivity(intent);
			break;
		}
		case R.id.rbo_display_day: {
			Intent intent = new Intent(getActivity(), ActivityDay.class);
			startActivity(intent);
			break;
		}
		case R.id.rbo_display_greens: {
			Intent intent = new Intent(getActivity(), ActivityGreens.class);
			startActivity(intent);
			break;
		}
		}
	}

	private void getGoods() {
		HttpGoodsApi.getGoodsList(new RequestCallBack<GeneralResponse<List<Goods>>>() {
			@Override
			public void onSuccess(GeneralResponse<List<Goods>> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if (responseData.isSuccess()) {
					goodsData.clear();
					goodsData.addAll(justSHow(responseData.getData()));
					adapter.notifyDataSetChanged();
//					measureListViewHeight();

				}
			}

			private List<Goods> justSHow(List<Goods> data) {
				// TODO Auto-generated method stub
				List<Goods> showList=new ArrayList<Goods>();
				for(Goods item :data)
				{
					if(item.getIsShow()==1)
					{
						showList.add(item);
					}
				}
				return showList;
			}

			@Override
			public GeneralResponse<List<Goods>> onParseData(String responseData) {
				GeneralResponse<List<Goods>> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<List<Goods>>>() {
				}.getType());
				return response;
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
			}

			public void onFinished() {
				System.out.println();
			}
		});
	}

	// 下拉列表跳转详情页
	private void initListView() {
		lv_display_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				hiddenSubView();
				goToGoodsDetails(position);
			}
		});
	}

	/**
	 * 传递商品
	 * 
	 * @param index
	 */
	private void goToGoodsDetails(int index) {
		Intent intent = new Intent(ActivityManager.topActivity(), Commoditydetail.class);
		intent.putExtra("good", goodsData.get(index));
		
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.enteranim,R.anim.exitanima);
	}

	@Override
	public void onScroll(int scrollY) {
		subHeight = subItemLayout.getHeight();
		subTop = tvSort.getTop();
		scrollViewHeight=scollView.getTop();
		top=gridLayout.getHeight();
		if (scrollY > top) {
			if (subView == null)
				showSubView();
		} else if (scrollY <  top)
			hiddenSubView();
	}

	private void showSubView() {
		// TODO Auto-generated method stub
		if (subView == null) {
			subView = inflater.inflate(R.layout.subitemlayout, null);
			if (subParams == null) {
				subParams = new WindowManager.LayoutParams();
				subParams.type = LayoutParams.TYPE_PHONE; // 悬浮窗的类型，一般设为2002，表示在所有应用程序之上，但在状态栏之下
				subParams.format = PixelFormat.RGBA_8888;
				subParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE; // 悬浮窗的行为，比如说不可聚焦，非模态对话框等等
				subParams.gravity = Gravity.TOP; // 悬浮窗的对齐方式
				subParams.width = screenWidth;
				subParams.height = subHeight;
				subParams.x = 0; // 悬浮窗X的位置
				subParams.y = scrollViewHeight; //// 悬浮窗Y的位置
			}
		}

		windowManager.addView(subView, subParams);
	}

	public static void hiddenSubView() {
		// TODO Auto-generated method stub
		if (subView != null) {
			windowManager.removeView(subView);
			subView = null;
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		//判断是否需要刷新
		if(RefreshManager.refreshFlag==true)
		{
			getGoods();
			RefreshManager.refreshFlag=false;
		}
			
	}

}
