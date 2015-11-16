package cn.andrewlu.community.ui.collection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.R.id;
import cn.andrewlu.community.R.layout;

/**
 * Created by lenovo on 2015/9/9.
 */
public class ActivityMyCollectionDelete extends BaseActivity  {

    @ViewInject(id = R.id.mycollection_delect_back,click = "onClick")
    private View mycollection_delect_back;

    @ViewInject(id = R.id.mycollection_delect_BtnEditor,click = "onClick")
    private View mycollection_delect_BtnEditor;



    private List<MyCollectionDelete> collectionsList;

    private ListView list;

    private MyCollectionDeleteAdapter adapter;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_mycollection_delete);
        FinalActivity.initInjectedView(this);

        list = (ListView) findViewById(R.id.mycollection_delect_list);

        adapter = new MyCollectionDeleteAdapter(this, MyCollectionDelete.getCollections(this));
        list.setAdapter(adapter);
    }







    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mycollection_delect_back: {
                Intent intent = new Intent(this, ActivityMyCollection.class);
                startActivity(intent);
                finish();
                break;
            }

        }
    }
}
