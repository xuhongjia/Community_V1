package cn.andrewlu.community.ui.moments;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.andrewlu.community.App;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.api.CommentApi;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.Moments;
import cn.andrewlu.community.entity.UserComments;
import cn.andrewlu.community.entity.UserMoments;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.main.CommentsPageFragment;
import cn.andrewlu.community.utils.Bimp;
import cn.andrewlu.community.utils.FileUtils;
import cn.andrewlu.community.utils.ImageItem;
import cn.andrewlu.community.utils.PublicWay;
import cn.andrewlu.community.utils.Res;
import cn.andrewlu.community.widget.ProgressDialogDIY;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;



/**
 * 首页面activity
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:48:34
 */
public class ActivityMomentWrite extends BaseActivity{

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;

	@ViewInject(R.id.moment_cancel)
	View moment_cancel;
	@ViewInject(R.id.activity_selectimg_send)
	View activity_selectimg_send;

	@ViewInject(R.id.moment_content)
	EditText moment_content;

	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap ;
	private Dialog pd;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
		setContentView(parentView);
		ViewUtils.inject(this);
		Init();
	}
	public void Init() {

		pop = new PopupWindow(ActivityMomentWrite.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ActivityMomentWrite.this,
						AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(ActivityMomentWrite.this,R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(ActivityMomentWrite.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if(Bimp.tempSelectBitmap.size() == 9){
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position ==Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for(int i=0;i<PublicWay.activityList.size();i++){
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			finish();
		}
		return true;
	}
	@OnClick({R.id.moment_cancel,R.id.activity_selectimg_send})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.moment_cancel:
			finish();
			break;
		case R.id.activity_selectimg_send:
			//提交后台
			final List<ImageItem> bitmapList=Bimp.tempSelectBitmap;
			pd = new ProgressDialogDIY().createLoadingDialog(this, "图片上传中...");
			pd.show();
			if(bitmapList.size()!=0)
			{
				//File[] files = new File[bitmapList.size()];
				final StringBuffer sb = new StringBuffer();
				for(int i=0;i<bitmapList.size();i++){
					File file = new File(Environment.getExternalStorageDirectory(),i+".jpg");
					try {
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
						bitmapList.get(i).getBitmap().compress(Bitmap.CompressFormat.JPEG, 70, bos);
						bos.flush();
						bos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					HttpApi.upLoadGoodsImg(file, new RequestCallBack<GeneralResponse<String>>(){
						@Override
						public void onSuccess(ResponseInfo<Object> response,boolean isFromCache)
						{   
							GeneralResponse<String> s = new Gson().fromJson((String)response.result, new TypeToken<GeneralResponse<String>>(){}.getType()) ;
							sb.append(s.getData().split(";")[0]).append(";");
							String[] img=sb.toString().substring(0,sb.toString().length()-1).split(";");
							if(img.length==bitmapList.size()){
								insert(sb.toString().substring(0,sb.toString().length()-1));
							}
						}
						@Override
						public void onFailure(HttpException e, String msg) {
							pd.dismiss();
							showToast("图片上传失败，图片过大"+msg);
						}
					});
				}
				//			CommentApi.uploadImgs(files, new RequestCallBack<GeneralResponse<String>>() {
				//				@Override
				//				public void onSuccess(GeneralResponse<String> responseData, boolean isFromCache) {
				//					super.onSuccess(responseData, isFromCache);
				//					if(responseData.isSuccess())
				//					{
				//						Moments moments = new Moments();
				//						moments.setuId(UserManager.getInstance().getLoginUser().getId());
				//						moments.setImages(responseData.getData());
				//						moments.setTime(System.currentTimeMillis());
				//						moments.setContent(moment_content.getText().toString());
				//						CommentApi.insertMoment(moments, new RequestCallBack<GeneralResponse<Integer>>() {
				//							public void onSuccess(GeneralResponse<Integer> responseData, boolean isFromCache){
				//								finish();
				//							}
				//							public GeneralResponse<Integer> onParseData(String responseData) {
				//								GeneralResponse<Integer> response = new Gson().fromJson(responseData,
				//										new TypeToken<GeneralResponse<Integer>>() {
				//								}.getType());
				//								return response;
				//							}
				//							@Override
				//							public void onFailure(HttpException error, String msg) {
				//								super.onFailure(error, msg);
				//							}
				//						});
				//					}
				//				}
				//
				//				@Override
				//				public GeneralResponse<String> onParseData(String responseData) {
				//					GeneralResponse<String> response = new Gson().fromJson(responseData,
				//							new TypeToken<GeneralResponse<String>>() {
				//					}.getType());
				//					return response;
				//				}
				//
				//				@Override
				//				public void onFailure(HttpException error, String msg) {
				//					super.onFailure(error, msg);
				//				}
				//			});
				
			}
			else {
				insert("");
			}
			break;
		}

	}
	public void insert(String string){
		Moments moments = new Moments();
		moments.setuId(UserManager.getInstance().getLoginUser().getId());
		moments.setImages(string);
		moments.setTime(System.currentTimeMillis());
		moments.setContent(moment_content.getText().toString());
		CommentApi.insertMoment(moments, new RequestCallBack<GeneralResponse<Integer>>() {
			public void onSuccess(GeneralResponse<Integer> responseData, boolean isFromCache){
				Bimp.tempSelectBitmap.clear();
				finish();
			}
			public GeneralResponse<Integer> onParseData(String responseData) {
				GeneralResponse<Integer> response = new Gson().fromJson(responseData,
						new TypeToken<GeneralResponse<Integer>>() {
				}.getType());
				pd.dismiss();
				return response;
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
			}
		});
	}
}

