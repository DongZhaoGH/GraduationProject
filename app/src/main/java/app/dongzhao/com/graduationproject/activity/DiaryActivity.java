package app.dongzhao.com.graduationproject.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.dongzhao.com.graduationproject.R;
import app.dongzhao.com.graduationproject.db.MyDbHelper;
import app.dongzhao.com.graduationproject.domain.Event;

public class DiaryActivity extends Activity implements OnClickListener {

	private EditText etTitle;
	private EditText etContent;
	
	private Button btHistory;
	private Button btSave;
	
	private TextView tvImportant;
	
	private MyDbHelper dbHelper;
	private MyReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_diary);
		
		dbHelper = new MyDbHelper(this);
		
		etTitle = (EditText) findViewById(R.id.et_title);
		etContent = (EditText) findViewById(R.id.et_content);	
		tvImportant = (TextView) findViewById(R.id.tv_important);
		btHistory = (Button) findViewById(R.id.bt_history);
		btSave = (Button) findViewById(R.id.bt_save);
		
		btHistory.setOnClickListener(this);
		btSave.setOnClickListener(this);
		
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.itheima.broadcast");
		registerReceiver(receiver, filter);
		
	}

	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Event evnet = (Event) intent.getSerializableExtra("event");
			
			tvImportant.setText(evnet.getContent());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_history:
			Intent intent = new Intent(this,DiaryHistoryActivity.class);
			startActivity(intent);
			break;

		case R.id.bt_save:
			save();
			break;
		}
		
	}

	private void save() {
		String title = etTitle.getText().toString().trim();
		String content = etContent.getText().toString().trim();
		
		if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			Toast.makeText(this, "填写不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("content", content);
		values.put("time", System.currentTimeMillis());
		
		long insert = db.insert("event", null, values);
		
		if(insert != -1) {
			Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
			etTitle.setText("");
			etContent.setText("");
		}else {
			Toast.makeText(this, "存储失败", Toast.LENGTH_SHORT).show();
		}
		db.close();
	}
	
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}
	
}
