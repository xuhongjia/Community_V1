package cn.andrewlu.community.ui.main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.andrewlu.community.R;
import cn.andrewlu.community.Tools;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.api.UserApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;
import cn.andrewlu.community.ui.LoginSuccessFragment;
import cn.andrewlu.community.ui.user.AddressSetting;
import cn.andrewlu.community.ui.user.AgeSetting;
import cn.andrewlu.community.ui.user.SettingActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 首页杂货铺子页面.
 */

public class PersonalPageFragement extends LoginSuccessFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle b) {
		return inflater.inflate(R.layout.activity_personinfo, null);
	}

	// 修改性别对话框item
	private String[] singleitems = { "男", "女" };
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private String imgSrc;
	private static final String IMAGE_FILE_NAME = "header.jpg";
	// 跳转至设置页面的按钮
	@ViewInject(id = R.id.person_setting, click = "onClick")
	private ImageView settingbutton;
	// 用于修改头像
	@ViewInject(id = R.id.personinfo_head_portrait, click = "onClick")
	private ImageView head_portrait;
	//用户昵称
	@ViewInject(id = R.id.person_name)
	private TextView person_name;
	//触发修改昵称
	@ViewInject(id = R.id.name_tochange, click = "onClick")
	private ImageView name_tochange;
	private EditText name_edit;
	private TextView name_change;
	// 触发修改年龄
	@ViewInject(id = R.id.personinfo_age, click = "onClick")
	private RelativeLayout age;
	// 触发修改性别
	@ViewInject(id = R.id.personinfo_sex, click = "onClick")
	private RelativeLayout sex;
	// 触发修改地址
	@ViewInject(id = R.id.personinfo_address, click = "onClick")
	private LinearLayout address;
	// 用于展示用户年龄信息
	@ViewInject(id = R.id.personinfo_show_age)
	private TextView show_age;
	// 用于展示用户性别信息
	@ViewInject(id = R.id.personinfo_show_sex)
	private TextView show_sex;
	// 用于展示用户信用度信息
	@ViewInject(id = R.id.personinfo_show_credit)
	private TextView show_credit;
	// 用于展示用户手机号码信息
	@ViewInject(id = R.id.personinfo_show_phone)
	private TextView show_phone;
	// 用于展示用户地址信息
	@ViewInject(id = R.id.personinfo_show_address)
	private TextView show_address;

	public void onLoginSuccess() {
		gain_data();
	}

	// 获取数据
	public void gain_data() {
		UserManager userManager = UserManager.getInstance();
		User u=userManager.getLoginUser();
		person_name.setText(u.getName());
		if(u.getSex()==1){
			show_sex.setText("男");
		}
		else if(u.getSex()==2){
			show_sex.setText("女");
		}
		show_age.setText(""+u.getAge());
		show_address.setText(""+u.getAddress());
		show_phone.setText(u.getAccount());
		show_credit.setText(""+u.getCredit());
		BitmapUtils bitmaputil = new BitmapUtils(mContext);
		if(u.getImg()!=null){
			bitmaputil.display(head_portrait, u.getImg());
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		//创建修改昵称对话框
		case R.id.name_tochange:{
			LayoutInflater inflater=LayoutInflater.from(mContext);
			View v=inflater.inflate(R.layout.dialog_name_change, null);
			AlertDialog.Builder build = new AlertDialog.Builder(mContext);
			build.setView(v);
			final AlertDialog dialog = build.create();
			Window window = dialog.getWindow();  
			WindowManager.LayoutParams lp = window.getAttributes();  		 
			lp.alpha = 0.6f;  
			window.setAttributes(lp);
			dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			dialog.show();
			name_edit=(EditText) v.findViewById(R.id.name_edit);
			name_change=(TextView) v.findViewById(R.id.name_but);
			name_edit.setText(person_name.getText());
			name_change.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					person_name.setText(name_edit.getText());
					UserManager userManager = UserManager.getInstance();
					User u=userManager.getLoginUser();
					u.setName(name_edit.getText().toString());
					UserApi.setUserInfo(u, new RequestCallBack<GeneralResponse<User>>() {
					});
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(mContext.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(name_edit.getWindowToken(), 0);
					dialog.dismiss();
				}
			});
			break;
		}
		// 创建修改性别对话框
		case R.id.personinfo_sex: {
			AlertDialog.Builder build = new AlertDialog.Builder(mContext);
			build.setItems(singleitems, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int which) {
					show_sex.setText(singleitems[which]);
					UserManager userManager = UserManager.getInstance();
					User u=userManager.getLoginUser();
					if(singleitems[which]=="男"){
						u.setSex(1);
					}
					else if(singleitems[which]=="女"){
						u.setSex(2);
					}
					UserApi.setUserInfo(u, new RequestCallBack<GeneralResponse<User>>() {
					});
				}
			});
			AlertDialog dialog = build.create();
			dialog.show();
			break;
		}
		// 跳转至年龄修改
		case R.id.personinfo_age: {
			Intent intent = new Intent(mContext, AgeSetting.class);
			String text = show_age.getText().toString();
			intent.putExtra("age", text);
			startActivityForResult(intent, 1);
			break;

		}
		// 跳转至地址修改
		case R.id.personinfo_address: {
			Intent intent1 = new Intent(mContext, AddressSetting.class);
			String text = show_address.getText().toString();
			intent1.putExtra("address", text);
			startActivityForResult(intent1, 2);
			break;

		}
		// 跳转至设置界面
		case R.id.person_setting: {
			Intent intent = new Intent(mContext, SettingActivity.class);
			startActivity(intent);
			break;
		}
		//修改头像
		case R.id.personinfo_head_portrait:{
			setImg();
		}
		}
	}
	//回传修改值
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == 1) {
			String age = data.getStringExtra("age");
			show_age.setText("" + age);
		} else if (requestCode == 2 && resultCode == 2) {
			String address = data.getStringExtra("address");
			show_address.setText("" + address);
		}
		if (resultCode != this.getActivity().RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case 1:
				resizeImage(data.getData());
				break;
			case 2:
				if (isSdcardExisting()) {
					resizeImage(getImageUri());
				} else {
					Toast.makeText(mContext, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}
				break;

			case 3:
				if (data != null) {
					showResizeImage(data);
				}
				break;
			}
		}
	}
	//设置头像
		private void setImg(){
			//设置头像
			new AlertDialog.Builder(mContext)
			.setTitle("设置头像")
			.setItems(items, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						Intent intentFromGallery = new Intent();
						intentFromGallery.setType("image/*"); // 设置文件类型
						intentFromGallery
						.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(intentFromGallery,
								1);
						break;
					case 1:

						Intent intentFromCapture = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 判断存储卡是否可以用，可用进行存储
						if (Tools.hasSdcard()) {

							intentFromCapture.putExtra(
									MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(Environment
											.getExternalStorageDirectory(),
											IMAGE_FILE_NAME)));
						}

						startActivityForResult(intentFromCapture,
								2);
						break;
					}
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
		}

		private boolean isSdcardExisting() {
			final String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		}

		public void resizeImage(Uri uri) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 150);
			intent.putExtra("outputY", 150);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, 3);
		}

		private void showResizeImage(Intent data)  {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				upload(photo);
				Drawable drawable = new BitmapDrawable(head_portrait.getResources(),photo);
				head_portrait.setImageDrawable(drawable);
			}
		}
		
		public void upload(Bitmap photo)
		{
			File file = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
				photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpApi.upLoadUserImg(file, new RequestCallBack<GeneralResponse<String>>(){
				@Override
				public void onSuccess(ResponseInfo<Object> response,boolean isFromCache)
				{
					GeneralResponse<String> s = new Gson().fromJson((String)response.result, new TypeToken<GeneralResponse<String>>(){}.getType()) ;
					imgSrc=s.getData().split(";")[0];
					UserManager userManager = UserManager.getInstance();
					User u=userManager.getLoginUser();
					u.setImg(imgSrc);
					UserApi.setUserInfo(u, new RequestCallBack<GeneralResponse<User>>() {
					});
				}

				@Override
				public void onFailure(HttpException e, String msg) {
					System.out.print("x");
				}
			});
			
		}
		
		private Uri getImageUri() {
			return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
					IMAGE_FILE_NAME));
		}

}
