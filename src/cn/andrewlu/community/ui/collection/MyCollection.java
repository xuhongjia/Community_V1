package cn.andrewlu.community.ui.collection;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lenovo on 2015/9/8.
 */
public class MyCollection {

    public static List<MyCollection> getCollections(Context context){
        List<MyCollection> collections = new ArrayList<MyCollection>();
            for(int i = 0; i < 1; i++){
                MyCollection collection = new MyCollection();
                collections.add(collection);
            }

        return collections;
    }
}
