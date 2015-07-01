package com.example.service.comm;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class CommService extends Service {

	public static final String ACTION_FROME_ACT = "com.example.ACTION_FROME_ACT";	
	
	public static final String ACTION_FROME_SERVICE = "com.example.ACTION_FROME_SERVICE";	
	
	public static final String BINDER_ACT_ACTION = "BINDER_ACT_ACTION";	
	
	private static final String TAG = CommService.class.getSimpleName();
	
	private Map<String, LocalBinder> mLocalBinders = new HashMap<String, LocalBinder>();
	
	public class LocalBinder extends Binder {
		
		private INotifiable mINotifiable;
		
		public CommService getService(INotifiable notifiable) {
			this.mINotifiable = notifiable;
			return CommService.this;
		}
		
		public INotifiable getINotifiable() {
			return this.mINotifiable;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, " -- onBind() --");
		String act = intent.getStringExtra(BINDER_ACT_ACTION);
		Log.i(TAG, "act:" + act);
		LocalBinder binder = null;
		if (mLocalBinders.get(act) == null) {
			binder = new LocalBinder();
			mLocalBinders.put(act, binder);
		} else {
			binder = mLocalBinders.get(act);
		}
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, " -- onUnbind() --");
		int act = intent.getIntExtra(BINDER_ACT_ACTION, -1);
		mLocalBinders.remove(act);
		return true;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_FROME_ACT);
		registerReceiver(mReceiver, filter);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(mReceiver);
	}
	
	public void recvFromAct(String msg) {
		msg += " -> service";
		Log.i(TAG, msg);
		LocalBinder binder = mLocalBinders.get(CommAct.class.getName());
		if (binder != null) {
			binder.getINotifiable().notifyServiceEvent(msg);
		}
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.i(TAG, "-- onReceive() -- " + intent.toString());
			if (action.equals(ACTION_FROME_ACT)) {
				String msg = intent.getStringExtra("message");
				msg += " -> service";
				Log.i(TAG, msg);
				intent.setAction(ACTION_FROME_SERVICE);
				intent.putExtra("message", msg);
				sendBroadcast(intent);
			}
		}
	};
	

}
