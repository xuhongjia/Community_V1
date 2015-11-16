package cn.andrewlu.community;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import cn.andrewlu.community.service.ActivityManager;

/**
 * Created by andrewlu on 2015/9/6.
 */
public class BaseActivity extends FragmentActivity {

	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		ActivityManager.getInstance().push(this);
	}

	public void onDestroy(){
		super.onDestroy();
		ActivityManager.getInstance().destroy(this);
	}
	
    public void onResume() {
        super.onResume();
        ActivityManager.getInstance().setCurrentActivity(this);
    }


    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public Context getContext(){
    	return this.getApplicationContext();
    }
    public void showToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, resId, Toast.LENGTH_SHORT).show();
                //Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onBackPressed(){
    	back();
    }
    
    public void back(){
    	finish();
    }
}
