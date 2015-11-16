package cn.andrewlu.community.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import cn.andrewlu.community.App;

/**
 * Created by andrewlu on 2015/7/20.
 * 获取屏幕尺寸工具类.
 */
public class DisplayUtil {
    private static int width = 0;
    private static int height = 0;

    public static int getDisplayWidth() {
        return width;
    }

    public static int getDisplayHeight() {
        return height;
    }

    static {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        display.getMetrics(metric);
        width = metric.widthPixels;  // 屏幕宽度（像素）
        height = metric.heightPixels;  // 屏幕高度（像素）
        //density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        //densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
    }
}
