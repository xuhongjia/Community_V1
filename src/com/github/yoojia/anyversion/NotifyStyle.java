package com.github.yoojia.anyversion;

/**
 * Created by Yoojia.Chen yoojia.chen@gmail.com 2015-01-04 发现新版本的处理方式
 */
public enum NotifyStyle {

	/**
	 * 使用广播机制来处理新版本
	 */
	Broadcast,

	/**
	 * 使用系统级弹出窗口来处理新版本
	 */
	Dialog,

	/**
	 * 使用回调接口来处理新版本
	 */
	Callback;

	public int value() {
		int ret = 0;
		switch (this) {
		case Broadcast:
			ret = 1;
			break;
		case Dialog:
			ret = 2;
			break;
		case Callback:
			ret = 3;
			break;
		default:
			break;
		}
		return ret;
	}

	public static NotifyStyle getStyle(int value) {
		switch (value) {
		case 1:
			return Broadcast;
		case 2:
			return Dialog;
		case 3:
			return Callback;
		default:
			break;
		}
		return Dialog;
	}
}
