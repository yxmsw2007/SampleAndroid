package com.example.ui;

import com.example.service.R;
import com.example.service.log.LogService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainAct extends Activity {

	private static final String TAG = MainAct.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_act);
		
		startService(new Intent(this, LogService.class));
		
		new Thread() {
			public void run() {
				for (int i = 0; i < 1000; i++) {
					Log.w(TAG, TAG + ", i: " + i);
					SystemClock.sleep(1000);
				}
			};
		}.start();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(new Intent(this, LogService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
