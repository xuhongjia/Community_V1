package cn.andrewlu.community;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import io.rong.app.map.NewLocationProvider;
import io.rong.app.photo.NewCameraInputProviders;
import io.rong.app.photo.PhotoCollectionsProvider;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imlib.model.Conversation.ConversationType;

import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.Version;
import com.github.yoojia.anyversion.VersionParser;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by andrewlu on 2015/9/6.
 */
public class App extends Application {
	public void onCreate() {
		super.onCreate();
		CrashReport.initCrashReport(this, "900008151", false);
		_app = this;

		// 初始化融云
		InitRongIM();

		initAnyVersion();
	}

	private static App _app;

	public static App getInstance() {
		return _app;
	}

	public static SharedPreferences getSharedPreferences() {
		return _app.getSharedPreferences("community", MODE_PRIVATE);
	}

	/**
	 * HY 获得当前进程的名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentPidName(Context context) {
		// 获得当前运行的线程ID
		int pId = android.os.Process.myPid();

		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		// 遍历所用正在运行的线程
		for (ActivityManager.RunningAppProcessInfo info : manager
				.getRunningAppProcesses()) {
			// 如果找到需要的线程则返回该线程名称
			if (info.pid == pId)
				return info.processName;
		}

		return null;
	}

	private void InitRongIM() {
		// 仅有全局进程和push可以初始化融云
		if (getApplicationInfo().packageName
				.equals(getCurrentPidName(getApplicationContext()))
				|| "io.rong.push"
						.equals(getCurrentPidName(getApplicationContext()))) {
			RongIM.init(this);

			// 扩展功能自定义
			InputProvider.ExtendProvider[] provider = {
					new NewCameraInputProviders(RongContext.getInstance()),// 相机
					new PhotoCollectionsProvider(RongContext.getInstance()),// 图片
					new NewLocationProvider(RongContext.getInstance()),// 地理位置
			};
			RongIM.resetInputExtensionProvider(ConversationType.PRIVATE,
					provider);
		}
	}

	public void initAnyVersion() {
		AnyVersion.init(this, new VersionParser() {
			@Override
			public Version onParse(String response) {
				return new Gson().fromJson(response, Version.class);
			}
		});
		AnyVersion anyVersion = AnyVersion.getInstance();
		anyVersion
				.setURL("http://andrewlu.cn/CommunityWeb/action/version/checkUpdate");
	}
}
