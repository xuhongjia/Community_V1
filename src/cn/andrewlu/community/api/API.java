package cn.andrewlu.community.api;

import com.lidroid.xutils.HttpUtils;

public abstract class API {
	protected static final HttpUtils http = new HttpUtils();
	static {
		http.configSecureInterceptor(new SecurityHandler());
	}
	private static final String R_URL = "http://andrewlu.cn/CommunityWeb";
	// private final static String D_URL = "http://192.168.1.145:8080/web"; //
	// HY测试地址
	//private final static String D_URL = "http://172.18.158.164:8080/web"; //

//	private final static String D_URL = "http://172.21.102.125:8080/web"; // 徐宏佳测试地址

	private final static String D_URL = "http://172.18.158.2:7080/CommunityWeb"; // 徐宏佳测试地址

	protected final static String URL = R_URL;
}
