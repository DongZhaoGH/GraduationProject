package app.dongzhao.com.graduationproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.dongzhao.com.graduationproject.R;
import app.dongzhao.com.graduationproject.db.MyDbHelper;
import app.dongzhao.com.graduationproject.domain.Event;


public class DiaryHistoryActivity extends Activity implements OnItemClickListener{

	protected static final int ERROR = 0;
	protected static final int SUCCESS = 1;
	
	private ListView listView;
	private MyDbHelper dbHelper;
	private List<Event> historyList;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				listView.setAdapter(new MyAdapter());
				listView.setOnItemClickListener(DiaryHistoryActivity.this);
				break;

			case ERROR:
				Toast.makeText(DiaryHistoryActivity.this, "没有内容", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		dbHelper = new MyDbHelper(this);
		listView = (ListView) findViewById(R.id.listview);
		//没有数据显示的内容
		TextView emptyView = (TextView) findViewById(R.id.empty_view);
		listView.setEmptyView(emptyView);

		new Thread(new Runnable() {
			@Override
			public void run() {
				historyList = getHistoryData();
				if(historyList.size()<=0) {
					Message msg = Message.obtain();
					msg.what = ERROR;
					handler.sendMessage(msg);
				}else {
					Message msg = Message.obtain();
					msg.what = SUCCESS;
					handler.sendMessage(msg);
				}
			}
		}).start();

		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Event event = historyList.get(position);

		Intent intent = new Intent();
		intent.setAction("com.itheima.broadcast");
		intent.putExtra("event", event);
		sendBroadcast(intent);

		finish();


	}

	//拿数据
	private List<Event> getHistoryData() {
		List<Event> list = new ArrayList<Event>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select content,title,time from event", null);
		while(cursor.moveToNext()) {
			String content = cursor.getString(0);
			String title = cursor.getString(1);
			long time = cursor.getLong(2);
			Event event = new Event(title,content,time);
			list.add(event);
		}
		return list;
	}

	
	
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return historyList.size();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if(convertView == null) {
				view = View.inflate(DiaryHistoryActivity.this, R.layout.item, null);
			}else {
				view = convertView;
			}
			
			TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
			TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
			
			Event event = historyList.get(position);
			tvTitle.setText(event.getTitle());
			tvContent.setText(event.getContent());
			
			Date date = new Date(event.getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String format = sdf.format(date);
			tvTime.setText(format);
			
			return view;
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
