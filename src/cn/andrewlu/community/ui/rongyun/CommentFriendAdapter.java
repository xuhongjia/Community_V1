package cn.andrewlu.community.ui.rongyun;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.User;

/**
 *联系人适配器
 * @author Administrator  HY
 *
 */
public class CommentFriendAdapter extends BaseAdapter
{
	private List<User> list;
	private Context c;
	 public CommentFriendAdapter(Context c,List<User> list) {
		// TODO Auto-generated constructor stub
		 this.c=c;
		 this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=null;
		if(convertView==null)
		{
			LayoutInflater inflater=LayoutInflater.from(c);
			 view=inflater.inflate(R.layout.comment_friend_item, null);
			 ImageView imageView=(ImageView)view.findViewById(R.id.comment_friend_list_image);
			 TextView textView=(TextView)view.findViewById(R.id.comment_friend_list_name);
			 ViewHolder holder=new ViewHolder();
			 holder.view =imageView;
			 holder.name=textView;
			 view.setTag(holder);
		}
		else
			view=convertView;
			ViewHolder holder=(ViewHolder)view.getTag();
			ImageView imageView=holder.view;
			TextView textView=holder.name;
			User user=list.get(position);
			textView.setText(user.getName());
			BitmapUtils a=new BitmapUtils(c);
			a.configDefaultLoadFailedImage(R.drawable.head_portrait_pic);
			a.display(imageView, user.getImg());
			
			return view;
	}
	
	private class ViewHolder
	{
		public ImageView view;
		public TextView name;
	}
}
