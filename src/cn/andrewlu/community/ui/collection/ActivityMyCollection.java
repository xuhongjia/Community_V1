package cn.andrewlu.community.ui.collection;


import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.R.id;
import cn.andrewlu.community.R.layout;
import cn.andrewlu.community.entity.Goods;
import cn.andrewlu.community.service.GoodsManager;
import cn.andrewlu.community.ui.user.ActivityLogin;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * Created by lenovo on 2015/9/8.
 */
public class ActivityMyCollection extends BaseActivity {

    @ViewInject(id = R.id.mycollection_back,click = "onClick")
    private View mycollection_back;

    @ViewInject(id = R.id.mycollection_BtnEditor,click = "onClick")
    private View mycollection_BtnEditor;
    private List<Goods> collectGoods;
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_mycollection);
        FinalActivity.initInjectedView(this);
        collectGoods=GoodsManager.getAllCollectGoods();
        ListView list = (ListView) findViewById(R.id.mycollection_list);

        MyCollectionAdapter adapter = new MyCollectionAdapter(this, collectGoods);
        list.setAdapter(adapter);
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mycollection_back: {
                finish();
                break;
            }
            case R.id.mycollection_BtnEditor: {
                Intent intent = new Intent(this, ActivityMyCollectionDelete.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }
}
