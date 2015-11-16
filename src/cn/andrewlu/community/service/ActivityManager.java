package cn.andrewlu.community.service;

import java.util.Collection;
import java.util.LinkedList;

import android.app.Activity;

/**
 * Created by andrewlu on 2015/9/6.
 */
public class ActivityManager {
	private static ActivityManager _manager = new ActivityManager();
	private final Collection<Activity> activities = new LinkedList<Activity>();

	public static ActivityManager getInstance() {
		return _manager;
	}

	private Activity currentActivity = null;

	public void setCurrentActivity(Activity activity) {
		currentActivity = activity;
	}

	public static Activity topActivity() {
		return getInstance().currentActivity;
	}

	public void push(Activity activity) {
		activities.add(activity);
	}

	public void destroy(Activity activity) {
		activities.remove(activity);
	}

	public void exitApp() {
		for (Activity activity : activities) {
			if (activity != null && !activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
