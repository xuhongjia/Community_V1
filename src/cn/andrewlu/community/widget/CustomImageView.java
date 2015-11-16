package cn.andrewlu.community.widget;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import cn.andrewlu.community.R;
import cn.andrewlu.community.api.BitMaoUtils;
import cn.andrewlu.community.entity.Image;

import com.squareup.picasso.Picasso;
/**
 * Created by Fanyua on 2015/9/12.
 */
public class CustomImageView extends ImageView {
	private String url;
	private boolean isAttachedToWindow;
	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setImageResource(R.drawable.wallpaperq);
	}

	public CustomImageView(Context context) {
		super(context);
		setImageResource(R.drawable.wallpaperq);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Drawable drawable=getDrawable();
			if(drawable!=null&&isAttachedToWindow) {
				drawable.mutate().setColorFilter(Color.GRAY,
						PorterDuff.Mode.MULTIPLY);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			Drawable drawableUp=getDrawable();
			if(drawableUp!=null) {
				drawableUp.mutate().clearColorFilter();
			}
			break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onAttachedToWindow() {
		isAttachedToWindow = true;
		setImageUrl(url);
		super.onAttachedToWindow();
	}

	@Override
	public void onDetachedFromWindow() {
		Picasso.with(getContext()).cancelRequest(this);
		isAttachedToWindow = false;
		setImageBitmap(null);
		super.onDetachedFromWindow();
	}


	public void setImageUrl(String url) {

		if (!TextUtils.isEmpty(url)) {
			this.url = url;
			if (isAttachedToWindow) {
				BitMaoUtils.downLoadImage(this, url);
			}
			
		}
	}
}
