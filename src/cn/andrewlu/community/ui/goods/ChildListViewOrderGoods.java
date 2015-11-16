package cn.andrewlu.community.ui.goods;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChildListViewOrderGoods extends ListView { 
	 
    public  ChildListViewOrderGoods  (Context context, AttributeSet attrs) { 
 
        super(context, attrs); 
 
    } 
 
    public  ChildListViewOrderGoods  (Context context) { 
 
        super(context); 
 
    } 
 
    public  ChildListViewOrderGoods  (Context context, AttributeSet attrs, int defStyle) { 
 
        super(context, attrs, defStyle); 
 
    } 
 
    @Override
 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
 
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
 
                MeasureSpec.AT_MOST); 
 
        super.onMeasure(widthMeasureSpec, expandSpec); 
 
    } 
 
}
