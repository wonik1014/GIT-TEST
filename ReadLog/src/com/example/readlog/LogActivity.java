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
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LogActivity extends Activity {
	TableLayout tableLayout;
	TableRow tr;
	
	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_log);
		Intent intent = getIntent(); // 값을 받기 위한 Intent 생성
		String date = intent.getStringExtra("date");
		
		tableLayout = (TableLayout)findViewById(R.id.TableLayout);
		String s=getLog(date);
		ArrayList<String> result = jsonParserLog(s);
		setTable(result);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {		// JSON 쓰려니 이거 없으면 오류나서 사용했습니다.
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}
	
	private void setTable(ArrayList<String> s){
		TableRow.LayoutParams params = new TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
			      android.widget.TableRow.LayoutParams.WRAP_CONTENT);
		for(int i=0;i<s.size();i++){
			tr = new TableRow(LogActivity.this);
			String[] value = s.get(i).split("#");
			for(int j=0;j<3;j++){
				TextView tv0 = new TextView(LogActivity.this);
				tv0.setText(value[j]);
				tv0.setTextColor(Color.WHITE);
				tv0.setGravity(Gravity.CENTER);
				tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				tr.addView(tv0, params);
			}
			TextView tv1 = new TextView(LogActivity.this);
			tv1.setText("");
			tv1.setBackgroundColor(Color.WHITE);
			tv1.setGravity(Gravity.CENTER);
			tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float)0.1);
			tv1.setLayoutParams(new TableRow.LayoutParams(android.widget.TableRow.LayoutParams.FILL_PARENT,
				      android.widget.TableRow.LayoutParams.WRAP_CONTENT));
			tableLayout.addView(tr);
			tableLayout.addView(tv1);
		}
	}
	
	private String getLog(String date) {
		String domain = "http://yozizz27.cafe24.com";
		String URL = domain + "/Baek/get_log.jsp";

		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(URL + "?date=" + date);
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
			Toast.makeText(getApplicationContext(), "연결실패3333 ",
					Toast.LENGTH_LONG).show();
			return "";
		}
	}
	
	private ArrayList<String> jsonParserLog(String pRecvServerPage) {
		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			int size = json.getInt("size");
			ArrayList<String> result = new ArrayList<String>();
			for(int i=0;i<size;i++){
				result.add(json.getString("log_"+i));
			}
			return result;
		} catch (JSONException e) {
			return null;
		}
	}
}