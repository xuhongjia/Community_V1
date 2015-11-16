package cn.andrewlu.community.utils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class MyHttpUtils {
	public static String getStreamFromURL(String imageURL,String path) {  
		HttpUtils http = new HttpUtils(); 
		File file = null ;  
		HttpHandler handler =http.download(imageURL, path,true, true,new RequestCallBack<File>() {
			@Override
			public void onSuccess(ResponseInfo<Object> response,boolean isFromCache){
				
			}
		});
		return file.getAbsolutePath();  

	}  
}
