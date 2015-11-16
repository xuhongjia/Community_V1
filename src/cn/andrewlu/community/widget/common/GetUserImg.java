package cn.andrewlu.community.widget.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import cn.andrewlu.community.Tools;
import cn.andrewlu.community.api.HttpApi;
import cn.andrewlu.community.entity.GeneralResponse;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 封装失败！！！！
 * @author lenovo
 *
 */
public class GetUserImg{
	private ImageView userheadphoto;
	private Activity activity;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESIZE_REQUEST_CODE = 2;

	public static final int SEND_SUCCESS = 7;
	private static final String IMAGE_FILE_NAME = "header.jpg";

	private String[] items = new String[] { "选择本地图片", "拍照" };
	private String imgSrc;
	public GetUserImg(ImageView userheadphoto ,Activity activity){ 
		this.activity=activity;
		this.userheadphoto=userheadphoto;
	}
	public void setUserHeadPhoto()
	{
		//设置头像
		new AlertDialog.Builder(activity)
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
					activity.startActivityForResult(intentFromGallery,
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

					activity.startActivityForResult(intentFromCapture,
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != activity.RESULT_OK) {
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
					Toast.makeText(activity, "未找到存储卡，无法存储照片！",
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
		activity.startActivityForResult(intent, RESIZE_REQUEST_CODE);
	}

	private void showResizeImage(Intent data)  {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			upload(photo);
			Drawable drawable = new BitmapDrawable(userheadphoto.getResources(),photo);
			userheadphoto.setImageDrawable(drawable);
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
}
