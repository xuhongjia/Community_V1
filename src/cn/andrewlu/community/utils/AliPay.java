package cn.andrewlu.community.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cn.andrewlu.community.R;
import cn.andrewlu.community.service.ActivityManager;

import com.alipay.sdk.app.PayTask;

import android.app.Activity;

public class AliPay {
	// private String goodsName;
	// private String goodsInfo;
	// private String goodsPrice;
	private static String PARTNER = "2088911305708354";
	private static String SELLER = "huao429006@163.com";
	private static String RSA_PRIVATE;
	private Activity context;
	private boolean payFlag = false;

	private AliPay(Activity context) {
		this.context = context;
		if (RSA_PRIVATE == null) {
			InputStream in = context.getResources().openRawResource(
					R.raw.pkcs8_privatekey);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			try {
				RSA_PRIVATE = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static AliPay instance() {
		return new AliPay(ActivityManager.topActivity());
	}

	public void pay(String n, String info, String price) {
		if (payFlag) {
			// 正在付款中,防止二次提交定单.
			return;
		}
		payFlag = true;
		try {
			final String order = getOrderInfo(n, info, price);
			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(context);
					// 调用支付接口，获取支付结果
					final String result = alipay.pay(order);
					if (mListener != null) {
						context.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								PayResult result2 = new PayResult(result);
								mListener.onPayResult(result2);
								payFlag = false;
							}
						});
					} else {
						payFlag = false;
					}
				}
			};

			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();
		} catch (Exception exception) {
			payFlag = false;
		}
	}

	// 获取用于支付的定单信息.
	private String getOrderInfo(String n, String info, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + n + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + info + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
		return payInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private OnPayResultListener mListener = null;

	public AliPay setOnPayResultListener(OnPayResultListener listener) {
		this.mListener = listener;
		return this;
	}

	public interface OnPayResultListener {
		public void onPayResult(PayResult result);
	}
}
