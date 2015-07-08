package com.example.audio;

import java.util.LinkedList;

import com.example.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @project SampleAndroid.workspace   
 * @class AudioAct 
 * @description       
 * @author yxmsw2007
 * @version   
 * @email yxmsw2007@gmail.com  
 * @data 2015-7-7 下午11:48:21    
 */
public class AudioAct extends Activity implements IAudioRecCallback, OnClickListener {

	private static final String TAG = AudioAct.class.getSimpleName();

	private LinkedList<byte[]> mAudioBuffer = new LinkedList<byte[]>();
	
	private AudioPlay mAudioPlay = new AudioPlay();
	
	private AudioRec mAudioRec = new AudioRec(this);
	
	private Button audio_act_record;
	private Button audio_act_play;
	private TextView audio_act_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_act);
		audio_act_record = (Button) findViewById(R.id.audio_act_record);
		audio_act_play = (Button) findViewById(R.id.audio_act_play);
		audio_act_info = (TextView) findViewById(R.id.audio_act_info);
		
		audio_act_record.setOnClickListener(this);
		audio_act_play.setOnClickListener(this);
		
	}

	@Override
	public void read(byte[] data) {
		Log.d(TAG, "" + data.length);
		if (mAudioPlay.isPlaying()) {
			mAudioPlay.playAudio(data);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.audio_act_record:
			if (mAudioRec.isRecording()) {
				mAudioRec.stopRecord();
			} else {
				mAudioRec.startRecord();
			}
			updateView();
			break;
		case R.id.audio_act_play:
			if (mAudioPlay.isPlaying()) {
				mAudioPlay.stopPlay();
			} else {
				mAudioPlay.startPlay();
			}
			updateView();
			break;

		default:
			break;
		}
	}
	
	private void updateView() {
		audio_act_record.setText(mAudioRec.isRecording() ? "Record(On)" : "Record(Off)");
		audio_act_play.setText(mAudioPlay.isPlaying() ? "Play(On)" : "Play(Off)");
	}
	
}
