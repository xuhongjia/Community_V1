package cn.andrewlu.community.widget.common;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.entity.Image;
import cn.andrewlu.community.entity.Moments;
import cn.andrewlu.community.entity.ScreenTools;
import cn.andrewlu.community.widget.NineGridlayout;
import cn.andrewlu.community.widget.CustomImageView;
public class NineGridAdapter extends BaseAdapter implements View.OnClickListener{
	private Context context;
	private LayoutInflater inflater;
	private List<Moments> list;
	//private List<Comment> lists;
	public NineGridAdapter(Context context,List<Moments>list) {
		this.context = context;
		this.list = list;
		//this.lists=lists;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView=inflater.inflate(R.layout.circle_listview_item, null);
			viewHolder=new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder=(ViewHolder)convertView.getTag();
		}
		Moments moments=list.get(position);
		viewHolder.tv_circle_content.setText(moments.getContent());
		viewHolder.tv_circle_time.setText(moments.getTime()+"");
		String[] sourceStrArray = moments.getImages().split(";");
		ArrayList<Image> ImageList=new ArrayList<Image>();
		for (int i = 0; i < sourceStrArray.length; i++) {
			ImageList.add(new Image(sourceStrArray[i]));
		}
		if (sourceStrArray.length==0) {
			viewHolder.ivMore.setVisibility(View.GONE);
			viewHolder.ivOne.setVisibility(View.GONE);
		} else {
			if (sourceStrArray.length == 1) {
				viewHolder.ivMore.setVisibility(View.GONE);
				viewHolder.ivOne.setVisibility(View.VISIBLE);

				handlerOneImage(viewHolder, sourceStrArray[0]);
			} else {
				viewHolder.ivMore.setVisibility(View.VISIBLE);
				viewHolder.ivOne.setVisibility(View.GONE);
				viewHolder.ivMore.setImagesData(ImageList);
			}
		}

		return convertView;
	}

	private void handlerOneImage(ViewHolder viewHolder, String image) {
		int totalWidth;
		int imageWidth;
		int imageHeight;
		int getWidth;
		int getHeight;
		try {
			URL m_url=new URL(image);
			HttpURLConnection con=(HttpURLConnection)m_url.openConnection();
			InputStream in=con.getInputStream();
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=true;
			BitmapFactory.decodeStream(in,null,options);
			getHeight=options.outHeight;
			getWidth=options.outWidth;
			ScreenTools screentools = ScreenTools.instance(context);
			totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
			imageWidth = screentools.dip2px(getWidth);
			imageHeight = screentools.dip2px(getHeight);
			if (getWidth<= getHeight) {
				if (imageHeight > totalWidth) {
					imageHeight = totalWidth;
					imageWidth = (imageHeight * getWidth) / getHeight;
				}
			} else {
				if (imageWidth > totalWidth) {
					imageWidth = totalWidth;
					imageHeight = (imageWidth * getHeight) / getWidth;
				}
			}
			ViewGroup.LayoutParams layoutparams = viewHolder.ivOne.getLayoutParams();
			layoutparams.height = imageHeight;
			layoutparams.width = imageWidth;
			viewHolder.ivOne.setLayoutParams(layoutparams);
			viewHolder.ivOne.setClickable(true);
			viewHolder.ivOne.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
			viewHolder.ivOne.setImageUrl(image);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	class ViewHolder  {
		private NineGridlayout ivMore;
		private CustomImageView ivOne;
		private ImageView img_circle_user;
		private TextView tv_circle_username;
		private TextView tv_circle_content;
		private TextView tv_circle_time;
		private TextView tv_circle_isActivity;
		private TextView tv_circle_discuss;
		private TextView tv_circle_good;
		private LinearLayout ln_circle_comment;
		public ViewHolder(View view){
			ivMore = (NineGridlayout)view.findViewById(R.id.iv_ngrid_layout);
			ivOne = (CustomImageView)view.findViewById(R.id.iv_oneimage);
			img_circle_user=(ImageView)view.findViewById(R.id.img_circle_user);
			tv_circle_username= (TextView) view.findViewById(R.id.tv_circle_username);
			tv_circle_content= (TextView) view.findViewById(R.id.tv_circle_content);
			tv_circle_time= (TextView) view.findViewById(R.id.tv_circle_time);
			tv_circle_isActivity= (TextView) view.findViewById(R.id.tv_circle_isActivity);
			tv_circle_discuss= (TextView) view.findViewById(R.id.tv_circle_discuss);
			tv_circle_good= (TextView) view.findViewById(R.id.tv_circle_good);
			ln_circle_comment=(LinearLayout) view.findViewById(R.id.ln_circle_comment);
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}

	}


}
