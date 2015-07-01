package com.example.service.log;

import com.example.service.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class LogServiceAct extends Activity implements OnClickListener {

	private static final String TAG = LogServiceAct.class.getSimpleName();
	
	private boolean isRunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle(LogService.class.getSimpleName());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_service_act);
		findViewById(R.id.log_servie_act_start_log_service).setOnClickListener(this);
		findViewById(R.id.log_servie_act_stop_log_service).setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(new Intent(this, LogService.class));
		isRunning = false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.log_servie_act_start_log_service:
			startService(new Intent(this, LogService.class));
			if (!isRunning) {
				new Thread() {
					public void run() {
						int i = 0;
						while (isRunning) {
							Log.w(TAG, TAG + ", i: " + i);
							SystemClock.sleep(1000);
						}
					};
				}.start();
			}
			break;
		case R.id.log_servie_act_stop_log_service:
			stopService(new Intent(this, LogService.class));
			isRunning = false;
			break;

		default:
			break;
		}
	}
	
}
