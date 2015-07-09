package com.example.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.LinkedList;

/**
 * @project SampleAndroid.workspace   
 * @class AudioRec 
 * @description       
 * @author yxmsw2007
 * @version   
 * @email yxmsw2007@gmail.com  
 * @data 2015-7-6 下午11:10:13    
 */
public class AudioRec {
	
	private static final String TAG = AudioRec.class.getSimpleName();
	
	private final static int PCM16_FRAME_SIZE = 320;

	public final static int STREAM_TYPE = MediaRecorder.AudioSource.MIC;
	public final static int SAMPLE_RATE_IN_HZ = 8000;
	public final static int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
	public final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
	
	private boolean mIsRunning;
	private boolean mIsMute;
	
	private IAudioRecCallback mCallback;
	
	public AudioRec(IAudioRecCallback callback) {
		this.mCallback = callback;
	}
	
	public AudioRec() {
		super();
		mIsRunning = false;
		mIsMute = false;
	}

	private class RecordThread extends Thread {

		public RecordThread() {
			super("RecordThread");
		}

		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			int minBufferSize = android.media.AudioRecord.getMinBufferSize(
					SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT);
			AudioRecord audioRecord = null;
			// 当手机有程序正在使用麦克风时，会初始化失败
			while (mIsRunning
					&& (audioRecord == null 
					|| (audioRecord.getState() != AudioRecord.STATE_INITIALIZED))) {
				if (audioRecord != null) {
					audioRecord.release();
				}
				audioRecord = new AudioRecord(
						STREAM_TYPE, SAMPLE_RATE_IN_HZ,
						CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);
				yield();
			}
			audioRecord.startRecording();
			byte[] buffer = new byte[minBufferSize];
			while (mIsRunning) {
				int len = audioRecord.read(buffer, 0, buffer.length);
				if (len > 0 && !mIsMute) {
					byte[] data = new byte[len];
					System.arraycopy(buffer, 0, data, 0, len);
					mCallback.read(data);
				}
			}
			if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
				audioRecord.stop();
				audioRecord.release();
			}
			
		}
	};

	public void startRecord() {
		if (mIsRunning) {
			return;
		}
		mIsRunning = true;
		RecordThread recordThread = new RecordThread();
		recordThread.start();
	}

	// 停止录音
	public void stopRecord() {
		mIsRunning = false;
		mIsMute = false;
	}

	public boolean ismIsMute() {
		return mIsMute;
	}

	public void setmIsMute(boolean mIsMute) {
		this.mIsMute = mIsMute;
	}

	public boolean isRecording() {
		return mIsRunning;
	}
	
}
