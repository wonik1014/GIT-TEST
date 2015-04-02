package com.example.readlog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ArrayList<String> result;
	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ListView listView;
		ArrayAdapter<String> adapter;
		
		if (android.os.Build.VERSION.SDK_INT > 9) {		// JSON 쓰려니 이거 없으면 오류나서 사용했습니다.
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		String s=getDate();
		result = jsonParserDate(s);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, result);
		listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				Intent intent = new Intent(MainActivity.this, LogActivity.class); // 평범한 Intent 생성
				intent.putExtra("date", result.get(pos));
				startActivity(intent);
			}
		});
	}
		
	private String getDate() {
		String domain = "http://yozizz27.cafe24.com";
		String URL = domain + "/Baek/get_date.jsp";

		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(URL);
			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(),
							"utf-8"));
			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}
			return result;
		} catch (Exception e) {
			Log.d("msg", e.toString());
			client.getConnectionManager().shutdown(); // 연결 지연 종료
			Toast.makeText(getApplicationContext(), "연결실패 ",
					Toast.LENGTH_LONG).show();
			return "";
		}
	}

	private ArrayList<String> jsonParserDate(String pRecvServerPage) {
		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			int size = json.getInt("size");
			ArrayList<String> result = new ArrayList<String>();
			for(int i=0;i<size;i++){
				result.add(json.getString("date_"+i));
			}
			return result;
		} catch (JSONException e) {
			return null;
		}
	}
}


//package com.example.readlog;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class MainActivity extends Activity {
//	String[] result;
//	/** Called when the activity is first created. */
//	@SuppressLint("NewApi")
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_main);
//		
//		final EditText et_colname;
//		final EditText et_value;
//		Button b;
//		final TextView tv = null;
//		et_colname = (EditText)findViewById(R.id.EditText_colname);
//		et_value = (EditText)findViewById(R.id.EditText_value);
//		b = (Button)findViewById(R.id.Button_send);
//		
//		b.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Log.d("msg", et_colname.getText().toString());
//				Log.d("msg", et_value.getText().toString());
//				String s=SendByHttp(et_colname.getText().toString(), et_value.getText().toString());
//				result = jsonParser(s);
//			}
//		});
//		if (android.os.Build.VERSION.SDK_INT > 9) {		// JSON 쓰려니 이거 없으면 오류나서 사용했습니다.
//			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//					.permitAll().build();
//			StrictMode.setThreadPolicy(policy);
//		}
//		
//	}
//
//	private String SendByHttp(String colname, String value) {
//		String domain = "http://localhost:8080";
//		String URL = domain + "/MyJSP/javaBean/get_log.jsp";
//
//		DefaultHttpClient client = new DefaultHttpClient();
//		try {
//			Log.d("msg", "post 전");
//			HttpPost post = new HttpPost(URL + "?colname=" + colname + "&value=" + value);
//			Log.d("msg",post.getURI().toString());
//			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
//			HttpResponse response = client.execute(post);
//			Log.d("msg", "execute 후");
//			BufferedReader bufreader = new BufferedReader(
//					new InputStreamReader(response.getEntity().getContent(),
//							"utf-8"));
//			Log.d("msg", "bufreader 후");
//			String line = null;
//			String result = "";
//
//			while ((line = bufreader.readLine()) != null) {
//				result += line;
//			}
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//			client.getConnectionManager().shutdown(); // 연결 지연 종료
//			Toast.makeText(getApplicationContext(), "DB 연결실패 ",
//					Toast.LENGTH_LONG).show();
//			return "";
//		}
//	}
//
//	private String[] jsonParser(String pRecvServerPage) {
//		try {
//			JSONObject json = new JSONObject(pRecvServerPage);
//			int size = json.getInt("size");
//			String[] result = new String[size];
//			for(int i=0;i<size;i++){
//				result[i]=json.getString("log_"+i);
//			}
//			return result;
//		} catch (JSONException e) {
//			return null;
//		}
//	}
//}
