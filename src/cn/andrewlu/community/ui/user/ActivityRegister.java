package cn.andrewlu.community.ui.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.util.TimeCount;


import cn.andrewlu.community.App;
import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.Tools;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;
import cn.andrewlu.community.widget.ProgressDialogDIY;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityRegister extends BaseActivity implements Callback  {

	@ViewInject(value= R.id.back)
	private View back;

	@ViewInject(value = R.id.rightBtn)
	private View rightBtn;

	@ViewInject(value = R.id.register_userheadphoto)
	private ImageView register_userheadphoto;

	//	@ViewInject(value = R.id.register_userheadphoto)
	//	private ImageView mImageHeader;

	@ViewInject(value = R.id.register_name)
	private EditText name;

	@ViewInject(value = R.id.register_phoneNumber)
	private EditText phoneNumber;

	@ViewInject(value = R.id.register_checkcode)
	private EditText checkcode;

	@ViewInject(value = R.id.register_passsword)
	private EditText password;

	@ViewInject( value = R.id.register_btnCheckcode)
	private View btnVerification;

	@ViewInject(value = R.id.register_btnEye)
	private View btnEye;

	@ViewInject(value = R.id.register_btnEye)
	private CheckBox show=null;
	//获取图片
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESIZE_REQUEST_CODE = 2;
	private static final String IMAGE_FILE_NAME = "header.jpg";
	private String[] items = new String[] { "选择本地图片", "拍照" };
	//mob的Key
	private static String APPKEY = "a656aaebb7bc";
	private static String APPSECRET = "4d3d859f42559cff2c64862862f1aa61";
	private TimeCount time;
	private boolean isSuccess;
	private Dialog pd;
	//是否返回
	private boolean isBack;
	private String imgSrc;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_register);
		ViewUtils.inject(this);
		SharedPreferences preferences = App.getSharedPreferences();//etSharedPreference();
		time = new TimeCount(60000, 1000,btnVerification);//构造CountDownTimer对象
		initSDK();
		//读取信息从SharedPreferences
		isBack = preferences.getBoolean("isBack", false);
		name.setText(preferences.getString("Rname", ""));
		phoneNumber.setText(preferences.getString("RphoneNumber", ""));
		checkcode.setText(preferences.getString("Rcheckcode", ""));
		password.setText(preferences.getString("Rpassword", ""));
		imgSrc = preferences.getString("imgSrc", "");
		isSuccess = preferences.getBoolean("isSuccess", false);
		initText();
	}
	private void initText(){
		App.getSharedPreferences().edit().remove("Rname")
		.remove("RphoneNumber").remove("Rcheckcode")
		.remove("Rpassword").remove("isBack").remove("imgSrc")
		.remove("isSuccess").commit();
	}
	private void savePreferences(){
		App.getSharedPreferences().edit().putString("Rname", name.getText().toString().trim())
		.putString("RphoneNumber", phoneNumber.getText().toString().trim())
		.putString("Rcheckcode", checkcode.getText().toString().trim())
		.putString("Rpassword", password.getText().toString().trim())
		.putBoolean("isBack", true).putBoolean("isSuccess", true).putString("imgSrc", imgSrc).commit();
	}
	
	@OnClick({R.id.register_btnCheckcode,R.id.back,R.id.rightBtn,R.id.register_btnCheckcode
		,R.id.register_btnEye,R.id.register_userheadphoto})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back: {
			finish();
			break;
		}
		case R.id.rightBtn: {
			next();
			break;
		}
		case R.id.register_btnCheckcode: {
			if(phoneNumber.getText().toString().trim().length()==11)
			{
				SMSSDK.getVerificationCode("86", phoneNumber.getText().toString().trim());
				time.start();
			}
			else
			{
				showToast("手机号不正确！");
			}
			break;
		}
		case R.id.register_btnEye: {
			if(this.show.isChecked()){
				//设置为明文显示
				this.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			}
			else {
				//设置为秘闻显示
				this.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
			break;
		}
		case R.id.register_userheadphoto: {
			setImg();
			//				getUserImg();
			break;
		}
		}
	}

	//下一步验证
	private void next(){
		if(Check())
		{
			if(!isBack)
			{
				SMSSDK.submitVerificationCode("86",phoneNumber.getText().toString().trim() ,checkcode.getText().toString().trim());
				//pd=ProgressDialog.show(this, "", "验证中请稍后....",true);
				pd=ProgressDialogDIY.createLoadingDialog(this, "验证中请稍后....");
				pd.show();
				final Handler handler = new Handler(){
					@Override
					public void handleMessage(Message msg) {  
						pd.dismiss();   
						if(isSuccess)
						{
							Intent intent = new Intent(ActivityRegister.this, ActivityInfocomplete.class);
							startActivity(intent);
							savePreferences();
							finish();
						}else {
							Toast.makeText(ActivityRegister.this, "验证码有误或手机号有误，请重新输入", Toast.LENGTH_SHORT).show();
						}
					} 
				};
				new Thread(new Runnable() {			
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
							handler.sendEmptyMessage(0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
			else 
			{
				Intent intent = new Intent(ActivityRegister.this, ActivityInfocomplete.class);
				startActivity(intent);
				savePreferences();
				finish();
			}
		}
	}
	//设置头像
	private void setImg(){
		//设置头像
		new AlertDialog.Builder(this)
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
							IMAGE_REQUEST_CODE);
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
							CAMERA_REQUEST_CODE);
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
	//返回事件
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				resizeImage(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (isSdcardExisting()) {
					resizeImage(getImageUri());
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}
				break;

			case RESIZE_REQUEST_CODE:
				if (data != null) {
					showResizeImage(data);
				}
				break;
			}
		}


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
		startActivityForResult(intent, RESIZE_REQUEST_CODE);
	}

	private void showResizeImage(Intent data)  {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			upload(photo);
			Drawable drawable = new BitmapDrawable(register_userheadphoto.getResources(),photo);
			register_userheadphoto.setImageDrawable(drawable);
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
			}

			@Override
			public void onFailure(HttpException e, String msg) {

			}
		});
	}
	
	private Uri getImageUri() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				IMAGE_FILE_NAME));
	}
	
	//mob获取验证码返回信息
	private void initSDK() {
		// 初始化SDK
		final Handler handler = new Handler(this);
		SMSSDK.initSDK(ActivityRegister.this, APPKEY, APPSECRET);
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			} 
		};
		//注册监听器
		SMSSDK.registerEventHandler(eventHandler);
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (result == SMSSDK.RESULT_COMPLETE) {

			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
				//验证成功
				Toast.makeText(ActivityRegister.this, "验证成功", Toast.LENGTH_SHORT).show();
				isSuccess =	true;
			}else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
				//获取成功
				Toast.makeText(ActivityRegister.this, "获取成功", Toast.LENGTH_SHORT).show();
			}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
				//获取国家信息
			} 
		}else{                                                                 
			((Throwable)data).printStackTrace(); 
		}
		return false;
	}

	public boolean Check() {
		// TODO Auto-generated method stub
		if(name.getText().toString().trim().length()<3)
		{
			showToast("昵称不能小于3个字符");
			return false;
		}
		else if(phoneNumber.getText().toString().trim().length()!=11)
		{
			showToast("手机号码只能为11位");
			return false;
		}
		else if(checkcode.getText().toString().trim().length()<4)
		{
			showToast("验证码不能小于4个字符");
			return false;
		}
		else if(password.getText().toString().trim().length()<6)
		{
			showToast("验证码不能小于6个字符");
			return false;
		}
		else
		{
			return true;
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		App.getSharedPreferences().edit().remove("isSuccess").commit();
		SMSSDK.unregisterAllEventHandler();
	}
}

