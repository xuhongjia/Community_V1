package cn.andrewlu.community.ui.goods;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.HttpGoodsApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.entity.GoodsInOrder;
import cn.andrewlu.community.entity.Order;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.utils.AliPay;
import cn.andrewlu.community.utils.AliPay.OnPayResultListener;
import cn.andrewlu.community.utils.PayResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class PaymentActivity extends BaseActivity implements
OnPayResultListener {
	@ViewInject(id = R.id.payBtn, click = "onClick")
	private TextView payBtn;
	@ViewInject(id = R.id.back, click = "onClick")
	private View back;
	private User u;
	private Order order=new Order();
	private Goods goods;
	private GoodsInOrder goodsInOrder=new GoodsInOrder();
	private String biaoshi;
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_payment);
		FinalActivity.initInjectedView(this);
		biaoshi=(String) getIntent().getSerializableExtra("biaoshi");
		UserManager userManager = UserManager.getInstance();
		u=userManager.getLoginUser();
		if(biaoshi==null&&u!=null){
			dao();
		}
		else{
			
		}
		
	}
	@SuppressLint("SimpleDateFormat")
	private void dao(){
		
		goods = (Goods) getIntent().getSerializableExtra("good");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		long ct=System.currentTimeMillis();
		Date curDate = new Date(ct);//获取当前时间   
		int i=(int)(Math.random()*900)+100; //随机产生三位数字
		String str = formatter.format(curDate)+String.valueOf(i);
		order.setoId(str);
		order.setuId(u.getId());	
		order.setOrderTime(ct);
		order.setSellerId(goods.getuId());
		order.setPayType(0);
		order.setReceiveName(u.getName());
		order.setReceivePhone(u.getAccount());
		order.setState(1);
		order.setTotalPrice(goods.getPrice());
		order.setAddress(u.getAddress());
		goodsInOrder.setoId(str);
		goodsInOrder.setgId(goods.getId());
		goodsInOrder.setNum(1);
		goodsInOrder.setNote("无");
		insertOrder();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.payBtn: {
			// 向本地数据库加入一条订单信息.
			AliPay.instance().setOnPayResultListener(this)
			.pay("测试订单", "测试定单", "0.01");
			break;
		}
		case R.id.back: {
			// 向本地数据库加入一条订单信息.
			finish();
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onPayResult(PayResult result) {
		// TODO Auto-generated method stub
		// 根据订单状态,修改订单数据记录.
		switch (result.getResultStatusCode()) {
		case PayResult.STATUS_SUCCESS: {
			showToast("支付成功.");
			//把订单数据发到服务器
			order.setState(2);//付款成功，待发货状态
			insertOrder();
			payBtn.setText("支付成功.3秒后关闭本页面.");
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					finish();
				}
			}, 3000);
		}
		break;
		case PayResult.STATUS_CHECKING: {
			showToast("正在确认定单,请不要重复点击支付.");
			break;
		}
		case PayResult.STATUS_CANCEL: {
			showToast("支付取消");
			break;
		}
		default: {
			showToast("支付失败!");
		}
		break;
		}
	}

	private Handler mHandler = new Handler();
	//获取搜索热词
	private void insertOrder(){
		HttpGoodsApi.insertOrder(order, goodsInOrder,new RequestCallBack<GeneralResponse<String>>(){
			@Override
			public void onSuccess(GeneralResponse<String> responseData, boolean isFromCache) {
				super.onSuccess(responseData, isFromCache);
				if(responseData.isSuccess()) {
					Log.e("onSuccess", "成功");
				}
				else{
					showToast("发送订单信息失败");//出现失败应该要把钱退回，以后再处理
				}
			}

			@Override
			public GeneralResponse<String> onParseData(String responseData) {
				GeneralResponse<String> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<String>>() {
				}.getType());
				return response;
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
				showToast("出现错误，请联系运营客服");//出现错误应该要把钱退回，以后再处理
			}
		});
	}
}
