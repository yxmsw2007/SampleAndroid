package com.example.service.comm;

import com.example.service.R;
import com.example.service.comm.CommService.LocalBinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @project SampleAndroid   
 * @class CommAct 
 * @description       
 * @author yxmsw2007
 * @version   
 * @email yxmsw2007@gmail.com  
 * @data 2015-7-1 下午11:31:30    
 */
public class CommAct extends Activity implements ServiceConnection, INotifiable, OnClickListener, OnCheckedChangeListener {

	private static final String TAG = CommAct.class.getSimpleName();
	
	private CommService mService;
	
	private CheckBox comm_act_broadcast;
	private Button comm_act_send;
	private TextView comm_act_show_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle(CommService.class.getSimpleName());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_act);
		
		comm_act_broadcast = (CheckBox) findViewById(R.id.comm_act_broadcast);
		comm_act_send = (Button) findViewById(R.id.comm_act_send);
		comm_act_show_info = (TextView) findViewById(R.id.comm_act_show_info);
		
		comm_act_broadcast.setOnCheckedChangeListener(this);
		comm_act_send.setOnClickListener(this);
		
		Intent intent = new Intent(this, CommService.class);
		intent.putExtra(CommService.BINDER_ACT_ACTION, this.getClass().getName());
		
		//one service to many activities, use application context to bind 
//		getApplicationContext().bindService(intent, this, BIND_AUTO_CREATE);
		bindService(intent, this, BIND_AUTO_CREATE);
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(this);
		unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.comm_act_broadcast:
			if (isChecked) {
				IntentFilter filter = new IntentFilter();
				filter.addAction(CommService.ACTION_FROME_SERVICE);
				registerReceiver(mReceiver, filter);
			} else {
				unregisterReceiver(mReceiver);
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.comm_act_send:
			String msg = ((EditText)findViewById(R.id.comm_act_input)).getEditableText().toString();
			if (comm_act_broadcast.isChecked()) {
				Intent intent = new Intent(CommService.ACTION_FROME_ACT);
				msg += "(broadcast)";
				intent.putExtra("message", msg);
				sendBroadcast(intent);
			} else {
				msg += "(interface)";
				mService.recvFromAct(msg);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		mService = ((LocalBinder) service).getService(this);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		mService = null;
	}
	
	public void updateView(String msg) {
		comm_act_show_info.setText(msg);
	}

	@Override
	public void notifyServiceEvent(String msg) {
		msg += " -> activity";
		Log.i(TAG, msg);
		updateView(msg);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.i(TAG, "-- onReceive() -- " + intent.toString());
			if (action.equals(CommService.ACTION_FROME_SERVICE)) {
				String msg = intent.getStringExtra("message");
				msg += " -> activity";
				Log.i(TAG, msg);
				updateView(msg);
			}
		}
	};
}
