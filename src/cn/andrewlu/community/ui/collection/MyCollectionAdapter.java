package cn.andrewlu.community.ui.collection;

import java.util.List;

import cn.andrewlu.community.R;
import cn.andrewlu.community.R.drawable;
import cn.andrewlu.community.R.id;
import cn.andrewlu.community.R.layout;
import cn.andrewlu.community.entity.Goods;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by lenovo on 2015/9/8.
 */
public class MyCollectionAdapter extends BaseAdapter {
    private List<Goods> collectionsList;
    private LayoutInflater inflater = null;

    public MyCollectionAdapter(Context c, List<Goods> Collections) {
        this.collectionsList = Collections;
        inflater = LayoutInflater.from(c);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return collectionsList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        // ʵ�����һ��View. ͨ��xml�ļ�����ʵ��.
        System.out.println("s:" + System.currentTimeMillis());
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.inc_collection_listview_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            TextView textView = (TextView) convertView
                    .findViewById(R.id.collection_price);
            TextView textView1 = (TextView) convertView
                    .findViewById(R.id.collection_time);
            TextView textView2 = (TextView) convertView
                    .findViewById(R.id.collection_location);
            TextView textView3 = (TextView) convertView
                    .findViewById(R.id.collection_price);
            TextView textView4 = (TextView) convertView
                    .findViewById(R.id.collection_sellPrice);
            ImageView img = (ImageView) convertView.findViewById(R.id.collection_thumb);


            holder.image = img;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        MyCollection cityString = collectionsList.get(position);

        holder.image.setImageResource(R.drawable.iphone);

        System.out.println("e:" + System.currentTimeMillis());
        return convertView;
    }

    private class ViewHolder {

        private ImageView image;
    }



}

