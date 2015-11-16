package cn.andrewlu.community.ui.goods;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import cn.andrewlu.community.ui.goods.TextAdapter;
import cn.andrewlu.community.R;

public class PetViewLeft extends LinearLayout implements ViewBaseAction {
	
	private ListView regionListView;
	private ListView plateListView;
	private ArrayList<String> groups = new ArrayList<String>();
	private LinkedList<String> childrenItem = new LinkedList<String>();
	private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
	private PetAdapter plateListViewAdapter;
	private PetAdapter earaListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;
	private String showString = "不限";

	public PetViewLeft(Context context) {
		super(context);
		init(context);
	}

	public PetViewLeft(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).equals(showArea)) {
				earaListViewAdapter.setSelectedPosition(i);
				childrenItem.clear();
				if (i < children.size()) {
					childrenItem.addAll(children.get(i));
				}
				tEaraPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
				plateListViewAdapter.setSelectedPosition(j);
				tBlockPosition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.secondhand_view_region, this, true);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
		setBackgroundDrawable(getResources().getDrawable(
				R.drawable.choosearea_bg_left));

		
			groups.add("二手");
			LinkedList<String> secondhandItem = new LinkedList<String>();
			secondhandItem.add("汽车");
			secondhandItem.add("手机");
			secondhandItem.add("电脑");
			secondhandItem.add("自行车");
			secondhandItem.add("书籍");
			secondhandItem.add("电器");
			secondhandItem.add("健身器材");
			children.put(0, secondhandItem);
			
			groups.add("家具");
			LinkedList<String> furnitureItem = new LinkedList<String>();
			furnitureItem.add("电视机");
			furnitureItem.add("电冰箱");
			furnitureItem.add("沙发");
			furnitureItem.add("空调");
			furnitureItem.add("风扇");
			furnitureItem.add("桌子");
			furnitureItem.add("椅子");
			children.put(1, furnitureItem);
			
			groups.add("电子");
			LinkedList<String> eletronicItem = new LinkedList<String>();
			eletronicItem.add("苹果");
			eletronicItem.add("三星");
			eletronicItem.add("小米");
			eletronicItem.add("华为");
			eletronicItem.add("魅族");
			eletronicItem.add("LG");
			eletronicItem.add("OPPO");
			children.put(2, eletronicItem);
			
			groups.add("宠物");
			LinkedList<String> petItem = new LinkedList<String>();
			petItem.add("汪星人");
			petItem.add("喵星人");
			petItem.add("兔子");
			petItem.add("蜥蜴");
			petItem.add("鹦鹉");
			petItem.add("仓鼠");
			petItem.add("蜘蛛");
			children.put(3, petItem);
			
			groups.add("租房");
			LinkedList<String> rentItem = new LinkedList<String>();
			rentItem.add("500-1000元");
			rentItem.add("1000-2000元");
			rentItem.add("3000-4000元");
			rentItem.add("5000-6000元");
			rentItem.add("6000-7000元");
			rentItem.add("7000-8000元");
			rentItem.add("8000元以上");
			children.put(4, rentItem);
			
			groups.add("日常");
			LinkedList<String> dayItem = new LinkedList<String>();
			dayItem.add("饮料");
			dayItem.add("零食");
			dayItem.add("方便面");
			dayItem.add("烟酒");
			dayItem.add("面包");
			dayItem.add("7000-8000元");
			dayItem.add("其他");
			children.put(5, dayItem);
			
			groups.add("菜场");
			LinkedList<String> greensItem = new LinkedList<String>();
			greensItem.add("白菜");
			greensItem.add("生菜");
			greensItem.add("菜心");
			greensItem.add("包菜");
			greensItem.add("西洋菜");
			greensItem.add("通心菜");
			greensItem.add("香菜");
			children.put(6, greensItem);
		

		earaListViewAdapter = new PetAdapter(context, groups,
				R.drawable.choose_item_selected,
				R.drawable.choose_eara_item_selector);
		earaListViewAdapter.setTextSize(15);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		earaListViewAdapter
				.setOnItemClickListener(new PetAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
						if (position < children.size()) {
							childrenItem.clear();
							childrenItem.addAll(children.get(position));
							plateListViewAdapter.notifyDataSetChanged();
						}
					}
				});
		if (tEaraPosition < children.size())
			childrenItem.addAll(children.get(3));
		plateListViewAdapter = new PetAdapter(context, childrenItem,
				R.drawable.choose_item_right,
				R.drawable.choose_plate_item_selector);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionnoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListViewAdapter
				.setOnItemClickListener(new PetAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {
						
						showString = childrenItem.get(position);
						if (mOnSelectListener != null) {
							
							mOnSelectListener.getValue(showString);
						}

					}
				});
		if (tBlockPosition < childrenItem.size())
			showString = "分类";
		if (showString.contains("不限")) {
			showString = showString.replace("不限", "");
		}
		setDefaultSelect();

	}

	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}

	public String getShowText() {
		return showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
}
