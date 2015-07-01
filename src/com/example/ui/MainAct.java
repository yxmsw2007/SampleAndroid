package com.example.ui;

import com.example.service.R;
import com.example.service.comm.CommAct;
import com.example.service.log.LogServiceAct;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainAct extends Activity implements OnClickListener {

	private static final String TAG = MainAct.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_act);
		findViewById(R.id.main_act_LogService).setOnClickListener(this);
		findViewById(R.id.main_act_ActServiceComm).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_act_LogService:
			startActivity(new Intent(this, LogServiceAct.class));
			break;
		case R.id.main_act_ActServiceComm:
			startActivity(new Intent(this, CommAct.class));
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
