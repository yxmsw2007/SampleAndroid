package com.example.usc;

import java.util.ArrayList;

import com.example.app.R;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @project SampleAndroid   
 * @class AsrAct 
 * @description       
 * @author yxmsw2007
 * @version   
 * @email yxmsw2007@gmail.com  
 * @data 2015-7-4 下午7:33:23    
 */
public class AsrAct extends Activity implements OnTouchListener{
	
	private static final String TAG = AsrAct.class.getSimpleName();
	
	private static AudioRecord mRecord;  
    // 音频获取源  
    private int audioSource = MediaRecorder.AudioSource.MIC;  
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025  
    private static int sampleRateInHz = 16000;// 44100;  
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道  
    private static int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;// AudioFormat.CHANNEL_IN_STEREO;  
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。  
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;  
    // 音频大小  
    private int minBufSize;  
    
    private TextView asr_act_info;
    
    private Button asr_act_recognizer;
    
    private boolean isRecording = false;
    
	private AsrClient mAsrClient;
	
	private ArrayList<byte[]> buffers = new ArrayList<byte[]>();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asr_act);
		
		Log.d(TAG, "-- onCreate() --");
		
		asr_act_info = (TextView) findViewById(R.id.asr_act_info);
		
		asr_act_recognizer = (Button) findViewById(R.id.asr_act_recognizer);
		
		asr_act_recognizer.setOnTouchListener(this);
		
		minBufSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
		
		mRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, minBufSize);
		
		mAsrClient = new AsrClient();
		mAsrClient.setmSampleRate(sampleRateInHz);
		mAsrClient.setmAudioFormat(audioFormat == AudioFormat.ENCODING_PCM_16BIT ? 16 : 8);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.asr_act_recognizer:
			Log.d(TAG, "-- onTouch() -- id: " + v.getId() + ", action: " + event.getAction());
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				asr_act_recognizer.setText("recording");
				isRecording = true;
				startRecognizer();
			} else if(event.getAction() == MotionEvent.ACTION_UP){
				asr_act_recognizer.setText("Hold and speak");
				isRecording = false;
			}
			break;

		default:
			break;
		}
		
		return false;
	}
	
	private void updateView(String result) {
		asr_act_info.setText(result == null ? "Parse failed!" : result);
	}
	
	private void startRecognizer() {  
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d(TAG, "-- startRecognizer() --");
				mRecord.startRecording();
				buffers.clear();
		        // new一个byte数组用来存一些字节数据，大小为缓冲区大小  
		        byte[] audiodata = new byte[minBufSize];  
		        int readsize = 0;  
		        while (isRecording == true) {  
		            readsize = mRecord.read(audiodata, 0, minBufSize);  
		            Log.i("采集大小", String.valueOf(readsize));  
		            if (AudioRecord.ERROR_INVALID_OPERATION != readsize) { 
		            	byte[] data = new byte[readsize];
		            	System.arraycopy(audiodata, 0, data, 0, readsize);
		            	buffers.add(data);
		            }  
		        }  
		        final String result = mAsrClient.parseAudio(buffers);
				if(result != null) {
					System.out.println("识别结果:\n" + result);
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						updateView(result);
					}
				});
			}
			
		}).start();
    }  
	
}
