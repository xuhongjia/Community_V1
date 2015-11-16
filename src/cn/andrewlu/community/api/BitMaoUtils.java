package cn.andrewlu.community.api;

import com.lidroid.xutils.BitmapUtils;

import android.widget.ImageView;
import cn.andrewlu.community.service.ActivityManager;

/**
 * 专门用来下载图片
 * @author Administrator
 *
 */
public class BitMaoUtils 
{
	private static BitmapUtils utils=new BitmapUtils(ActivityManager.topActivity());
	
	/**
	 * 下载图片
	 * @param view
	 * @param url
	 */
	public static void downLoadImage(ImageView view,String url)
	{
		utils.display(view, url);
	
	}

	public static BitmapUtils getUtils() {
		return utils;
	}

	public static void setUtils(BitmapUtils utils) {
		BitMaoUtils.utils = utils;
	}
}
