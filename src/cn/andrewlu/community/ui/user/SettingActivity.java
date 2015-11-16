package cn.andrewlu.community.ui.user;

import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.Callback;
import com.github.yoojia.anyversion.NotifyStyle;
import com.github.yoojia.anyversion.Version;

import cn.andrewlu.community.BaseActivity;
import cn.andrewlu.community.R;
import cn.andrewlu.community.entity.User;
import cn.andrewlu.community.service.UserManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * Created by lenovo on 2015/9/9.
 */
public class SettingActivity extends BaseActivity {
	@ViewInject(id=R.id.remind_way)
	private TextView remind_way;
	@ViewInject(id=R.id.ring_parent)
	private RelativeLayout ring_parent;
	@ViewInject(id=R.id.vibration_parent)
	private RelativeLayout vibration_parent;
	// 返回键
	@ViewInject(id = R.id.setting_return, click = "onClick")
	private ImageView setting_return;
	// 消息提醒设置
	@ViewInject(id = R.id.mes_remind, click = "onClick")
	private CheckBox mes_remind;
	// 铃声设置
	@ViewInject(id = R.id.ring, click = "onClick")
	private CheckBox ring;
	// 振动设置
	@ViewInject(id = R.id.vibration, click = "onClick")
	private CheckBox vibration;
	// 修改密码
	@ViewInject(id = R.id.passsword_change, click = "onClick")
	private TextView password_change;
	// 版本更新
	@ViewInject(id = R.id.versions_updata, click = "onClick")
	private View versions_updata;
	//隐私设置
	@ViewInject(id = R.id.private_setting, click = "onClick")
	private TextView private_setting;

	@ViewInject(id = R.id.newlyVersion)
	private TextView newlyVersionTextView;

	@ViewInject(id = R.id.hasNewVersion)
	private View hasNewVersion;

	// 关于
	@ViewInject(id = R.id.about, click = "onClick")
	private TextView about;
	// 退出帐号
	@ViewInject(id = R.id.exit_count, click = "onClick")
	private Button exit_count;

	private Version newlyVersion = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personinfo_setting);
		FinalActivity.initInjectedView(this);
		if(!mes_remind.isChecked()){
			ring_parent.setVisibility(View.GONE);
			vibration_parent.setVisibility(View.GONE);
			remind_way.setVisibility(View.GONE);
		}
		else{
			ring_parent.setVisibility(View.VISIBLE);
			vibration_parent.setVisibility(View.VISIBLE);
			remind_way.setVisibility(View.VISIBLE);
		}

		// 检测更新,并弹出通知消息.
		AnyVersion anyVersion = AnyVersion.getInstance();

		anyVersion.setCallback(new Callback() {

			@Override
			public void onVersion(Version newVersion) {
				// TODO Auto-generated method stub
				hasNewVersion.setVisibility(View.VISIBLE);
				newlyVersionTextView.setText("最新版本:" + newVersion.name);
				newlyVersion = newVersion;
			}
		});
		anyVersion.check(NotifyStyle.Callback);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		// 返回点击事件
		case R.id.setting_return: {
			finish();
			break;
		}
		// 消息提醒点击事件
		case R.id.mes_remind: {
			if(!mes_remind.isChecked()){
				ring_parent.setVisibility(View.GONE);
				vibration_parent.setVisibility(View.GONE);
				remind_way.setVisibility(View.GONE);
			}
			else{
				ring_parent.setVisibility(View.VISIBLE);
				vibration_parent.setVisibility(View.VISIBLE);
				remind_way.setVisibility(View.VISIBLE);
			}
			break;
		}
		// 铃声点击事件
		case R.id.ring: {
			break;
		}
		// 振动点击事件
		case R.id.vibration: {
			break;
		}
		case R.id.private_setting:{
			Intent intent=new Intent(this,PrivacySetting.class);
			startActivity(intent);
			break;
		}
		// 修改密码点击事件
		case R.id.passsword_change: {
			UserManager userManager = UserManager.getInstance();
			User u=userManager.getLoginUser();
			Intent i=new Intent(this,ActivityFindPswd.class);
			i.putExtra("account", u.getAccount());
			startActivity(i);
			break;
		}
		// 版本更新点击事件
		case R.id.versions_updata: {

			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle("版本更新");
			if (newlyVersion == null) {
				build.setMessage("已是最新版本");
				build.setNeutralButton("确定", null);
			} else {
				// 静默升级方式最坑爹,一旦点击检查版本就直接开始下载了.
				if (newlyVersion.forceUpdateMode == Version.MODE_SILENT) {
					// 直接开始下载新版本.
					showToast("升级开始");
					AnyVersion.getInstance().submitDownload(newlyVersion);
					break;
				}

				build.setMessage(newlyVersion.note);

				// 可选或强制升级则点击确定按钮后开始下载文件.
				build.setPositiveButton("现在更新",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								showToast("正始下载更新");
								AnyVersion.getInstance().submitDownload(
										newlyVersion);
								dialog.dismiss();
							}
						});
				// 可选更新时可以点击以后再说.
				if (newlyVersion.forceUpdateMode == Version.MODE_OPTIONAL) {
					build.setNegativeButton("以后再说",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					// 如果强制更新,则弹窗不可取消.
				} else if (newlyVersion.forceUpdateMode == Version.MODE_FORCE) {
					build.setCancelable(false);
				}

			}
			AlertDialog dialog = build.create();
			dialog.show();
			break;
		}
		// 关于点击事件
		case R.id.about: {
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle("关于");
			build.setMessage("该app由艾悠乐优秀小组开发");
			build.setNeutralButton("确定", null);
			AlertDialog dialog = build.create();
			dialog.show();
			break;

		}
		// 退出帐号点击时间
		case R.id.exit_count: {
			logout();
			finish();
			break;
		}
		}
	}

	private void logout() {
		UserManager.getInstance().logout();
		finish();
		UserManager.getInstance().showLoginActivity();
	}
}
