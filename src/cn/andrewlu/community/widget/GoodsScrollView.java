package cn.andrewlu.community.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 商品滚动条
 * 
 *
 */
public class GoodsScrollView extends ScrollView {
	private OnScrollListener onScrollListener;

	private int lastScrollY;
	
	public GoodsScrollView(Context context) {
		this(context, null);
	}
	
	public GoodsScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GoodsScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}


	
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = GoodsScrollView.this.getScrollY();
			
			//每隔5毫秒刷新一次距离
			if(lastScrollY != scrollY){
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 10);  
			}
			if(onScrollListener != null){
				onScrollListener.onScroll(scrollY);
			}
			
		};

	}; 

	/**
	 * 判断用户手指刚触摸时的坐标
	 */

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(onScrollListener != null){
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch(ev.getAction()){
		case MotionEvent.ACTION_UP:
	         handler.sendMessageDelayed(handler.obtainMessage(), 10);  
			break;
		}
		return super.onTouchEvent(ev);
	}

/**
 * 暴露此接口，方便判断回调距离
 * @author lenovo
 *
 */

	public interface OnScrollListener{
	
		public void onScroll(int scrollY);
	}
	
	

}
