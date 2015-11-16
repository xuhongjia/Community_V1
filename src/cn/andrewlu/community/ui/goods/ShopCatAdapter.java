package cn.andrewlu.community.ui.goods;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.entity.ShopCat;

public class ShopCatAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	private ArrayList<ShopCat> list;
	private Context c;
	private View view;
	public ShopCatAdapter(Context c,ArrayList<ShopCat> list)
	{
		this.c=c;
		inflater=LayoutInflater.from(c);
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view=convertView;
		if(view==null)
		{
			view=inflater.inflate(R.layout.shopcat_list_item, null);
			Viewholder holder=new Viewholder();
			holder.tvTitle=(TextView)view.findViewById(R.id.goodsTitle);
			holder.tvPrice=(TextView)view.findViewById(R.id.goodsMoney);
			holder.tvCount=(TextView)view.findViewById(R.id.goodsCount);
			view.setTag(holder);
		}
		else
		{
			view=convertView;
		}
		final Viewholder holder=(Viewholder)view.getTag();
		final ShopCat goods=list.get(position);
		holder.tvCount.setText(goods.getCount()+"");
		holder.tvPrice.setText(goods.getPrice()+"");
		holder.tvTitle.setText(goods.getTitle()+"");
	
		return view;
	}
	
	class Viewholder
	{
		public TextView tvTitle;
		public TextView tvPrice;
		public TextView tvCount;
	}
}
