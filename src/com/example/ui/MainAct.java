package com.example.ui;

import com.example.app.R;
import com.example.audio.AudioAct;
import com.example.service.comm.CommAct;
import com.example.service.log.LogServiceAct;
import com.example.usc.AsrAct;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @project SampleAndroid   
 * @class MainAct 
 * @description       
 * @author yxmsw2007
 * @version   
 * @email yxmsw2007@gmail.com  
 * @data 2015-7-4 下午7:34:45    
 */
public class MainAct extends Activity implements OnClickListener {

	private static final String TAG = MainAct.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_act);
		findViewById(R.id.main_act_LogService).setOnClickListener(this);
		findViewById(R.id.main_act_ActServiceComm).setOnClickListener(this);
		findViewById(R.id.main_act_AsrAct).setOnClickListener(this);
		findViewById(R.id.main_act_AudioAct).setOnClickListener(this);
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
		case R.id.main_act_AsrAct:
			startActivity(new Intent(this, AsrAct.class));
			break;
		case R.id.main_act_AudioAct:
			startActivity(new Intent(this, AudioAct.class));
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
