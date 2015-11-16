package cn.andrewlu.community.ui.rongyun;

import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.andrewlu.community.R;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * HY 会话
 * 
 * @author Administrator
 *
 */
public class ConversationActivity extends FragmentActivity
{
	/**
	 * 目标 Id
	 */
	private String mTargetId;

	/**
	 * 会话类型
	 */
	private Conversation.ConversationType mConversationType;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rongyun_conversation);
		Intent intent = getIntent();
		getIntentDate(intent);
		TextView textView=(TextView )findViewById(R.id.rongyun_conversation_id);
//		textView.setText(getIntent().getData().getQueryParameter("targetId"));
		textView.setText(getIntent().getData().getQueryParameter("title"));
		ImageButton bt=(ImageButton)findViewById(R.id.rongyun_conversation_back);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	 

	/**
	 * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
	 */
	private void getIntentDate(Intent intent) 
	{

		mTargetId = intent.getData().getQueryParameter("targetId");
		// intent.getData().getLastPathSegment();//获得当前会话类型
		mConversationType = Conversation.ConversationType
				.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

		enterFragment(mConversationType, mTargetId);
	}

	/**
	 * 加载会话页面 ConversationFragment
	 *
	 * @param mConversationType
	 *            会话类型
	 * @param mTargetId
	 *            目标 Id
	 */
	private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId)
	{

		ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
				.findFragmentById(R.id.Rongyun_conversation);

		Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
				.appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
				.appendQueryParameter("targetId", mTargetId).build();

		fragment.setUri(uri);
	}
	
			 
}
