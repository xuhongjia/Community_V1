package cn.andrewlu.community.ui.collection;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lenovo on 2015/9/8.
 */
public class MyCollectionDelete {

    public static List<MyCollectionDelete> getCollections(Context context){
        List<MyCollectionDelete> collections = new ArrayList<MyCollectionDelete>();
        for(int i = 1; i < 5; i++){
            MyCollectionDelete collection = new MyCollectionDelete();
            collections.add(collection);
        }

        return collections;
    }
}