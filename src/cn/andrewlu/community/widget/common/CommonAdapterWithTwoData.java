package cn.andrewlu.community.widget.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.andrewlu.community.entity.OrderAllGoods;

public abstract class CommonAdapterWithTwoData<T,V> extends BaseAdapter 
{
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;
	protected List<List<V>> childData;
	protected final Set<Integer> clickableIds = new HashSet<Integer>();

	public CommonAdapterWithTwoData(Context context, List<T> mDatas,List<List<V>> childData,int itemLayoutId)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
		this.childData = childData;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
		convert(viewHolder, getItem(position) , childData.get(position));
		View retView = viewHolder.getConvertView();

		for(Integer id: clickableIds){
			View item = retView.findViewById(id);
			item.setTag(getItem(position));
		}
		return retView;

	}

	public abstract void convert(ViewHolder helper, T item ,List<V> childDataList );


	protected ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent)
	{
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position,clickableIds);
	}


}
