package cn.andrewlu.community.ui.collection;


import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import java.util.List;

import cn.andrewlu.community.R;
import cn.andrewlu.community.R.drawable;
import cn.andrewlu.community.R.id;
import cn.andrewlu.community.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lenovo on 2015/9/8.
 */


public class MyCollectionDeleteAdapter extends BaseAdapter {
    private List<MyCollectionDelete> collectionsList;
    private LayoutInflater inflater = null;


    public MyCollectionDeleteAdapter(Context c, List<MyCollectionDelete> collections) {
        this.collectionsList = collections;
        inflater = LayoutInflater.from(c);

    }

    @Override
    public int getCount() {
        return collectionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return collectionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // ʵ�����һ��View. ͨ��xml�ļ�����ʵ��.
        System.out.println("s:" + System.currentTimeMillis());
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.inc_collection_delete_listview_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            TextView textView = (TextView) convertView
                    .findViewById(R.id.collection_delete_depict);
            TextView textView1 = (TextView) convertView
                    .findViewById(R.id.collection_delete_time);
            TextView textView2 = (TextView) convertView
                    .findViewById(R.id.collection_delete_location);
            TextView textView3 = (TextView) convertView
                    .findViewById(R.id.collection_delete_price);
            TextView textView4 = (TextView) convertView
                    .findViewById(R.id.collection_delete_sellPrice);
            ImageView img = (ImageView) convertView.findViewById(R.id.collection_delete_thumb);

            CheckBox checkBox =(CheckBox)convertView.findViewById(R.id.collection_delete_checkbox);

            final int index = position;

            checkBox.setTag(position);
            checkBox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    collectionsList.remove(index);
                    notifyDataSetChanged();

                }
            });


            holder.image = img;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.image.setImageResource(R.drawable.iphone);

        System.out.println("e:" + System.currentTimeMillis());
        return convertView;
    }

    final static class ViewHolder {

        private ImageView image;
    }

}
