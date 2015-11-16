package cn.andrewlu.community.widget.common;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.andrewlu.community.R;
import cn.andrewlu.community.ui.goods.BitmapUtils;

public class PhotoGridItem extends RelativeLayout implements Checkable {
	private Context mContext;
	private boolean mCheck;
	private ImageView mImageView;
	private ImageView mSelect;
	
	public PhotoGridItem(Context context) {
		this(context, null, 0);
	}
	
	public PhotoGridItem(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }

	public PhotoGridItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.photoalbum_gridview_item, this);
		mImageView = (ImageView)findViewById(R.id.photo_img_view);
		mSelect = (ImageView)findViewById(R.id.photo_select);
	}
	@Override
	public void setChecked(boolean checked) {
		mCheck = checked;
		mSelect.setImageDrawable(checked ? getResources().getDrawable(R.drawable.cb_on) : getResources().getDrawable(R.drawable.cb_normal));
	}   
	
	@Override
	public boolean isChecked() {
		return mCheck;
	}

	@Override
	public void toggle() {  
		setChecked(!mCheck);
	}
	
	public void setImgResID(int id){
		if(mImageView != null){
			mImageView.setBackgroundResource(id);
		}
	}
	
	public void SetBitmap(Bitmap bit){
		if(mImageView != null){
			mImageView.setImageBitmap(bit);
		}
	}
	public void SetImg(String src){
		Bitmap bitmap=BitmapUtils.decodeSampledBitmapFromSd(src,50,50);
		mImageView.setImageBitmap(bitmap);
	}
	
}
