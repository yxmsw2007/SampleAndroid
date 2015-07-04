package com.example.usc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;

/**
 * ASR webAPI 示例程序
 * 
 * @author unisound
 * @date 2015年1月27日
 */
public class AsrClient {
	
	private static final String TAG = AsrClient.class.getSimpleName();

	private String mWebApiUrl;
	
	/**
	 * 云知声开放平台网站上开发者的用户名
	 */
	private String mUserId;
	
	/**
	 * 云知声开放平台网站上申请应用后获得的 appKey
	 */
	private String mAppKey;
	
	/**
	 * 标记请求来源的标识，如用户所设备序列号 (SN)，IMEI，MAC地址等
	 */
	private String mDeviceId;
	
	private int mAudioFormat; 

	private int mSampleRate; 
	
	private String mLanguage; 
	
	private String mCharset; 

	/**
	 * "general", "poi", "song", "movietv", "medical"
	 */
	private String mEngine; 

	public AsrClient() {
		this.mWebApiUrl = Config.URL;
		this.mUserId = Config.USER_ID;
		this.mAppKey = Config.APP_KEY;
		this.mDeviceId = Config.DEVICE_ID;
		this.mAudioFormat = Config.AUDIO_FORMATE;
		this.mSampleRate = Config.SAMPLE_RATE;
		this.mLanguage = Config.LANGUAGE;
		this.mCharset = Config.CHARSET;
		this.mEngine = Config.ENGINE;
	}

	public String getmWebApiUrl() {
		return mWebApiUrl;
	}

	public void setmWebApiUrl(String mWebApiUrl) {
		this.mWebApiUrl = mWebApiUrl;
	}

	public String getmUserId() {
		return mUserId;
	}

	public void setmUserId(String mUserId) {
		this.mUserId = mUserId;
	}

	public String getmAppKey() {
		return mAppKey;
	}

	public void setmAppKey(String mAppKey) {
		this.mAppKey = mAppKey;
	}

	public String getmDeviceId() {
		return mDeviceId;
	}

	public void setmDeviceId(String mDeviceId) {
		this.mDeviceId = mDeviceId;
	}

	public int getmAudioFormat() {
		return mAudioFormat;
	}

	public void setmAudioFormat(int mAudioFormat) {
		this.mAudioFormat = mAudioFormat;
	}

	public int getmSampleRate() {
		return mSampleRate;
	}

	public void setmSampleRate(int mSampleRate) {
		this.mSampleRate = mSampleRate;
	}

	public String getmLanguage() {
		return mLanguage;
	}

	public void setmLanguage(String mLanguage) {
		this.mLanguage = mLanguage;
	}

	public String getmCharset() {
		return mCharset;
	}

	public void setmCharset(String mCharset) {
		this.mCharset = mCharset;
	}

	public String getmEngine() {
		return mEngine;
	}

	public void setmEngine(String mEngine) {
		this.mEngine = mEngine;
	}

	public String parseAudio(ArrayList<byte[]> buffers) {
		int length = 0;
		for (int i = 0; i < buffers.size(); i++) {
			length += buffers.get(i).length;
		}
		return parseAudio(buffers, length);
	}
	
	public String parseAudio(ArrayList<byte[]> buffers, int length) {
		byte[] data = new byte[length];
		int off = 0;
		for (int i = 0; i < buffers.size(); i++) {
			byte[] buffer = buffers.get(i);
			int len = buffer.length;
			if (off + len <= length) {
				System.arraycopy(buffer, 0, data, off, len);
				off += len;
			}
		}
		return parseAudio(data);
	}
	
	public String parseAudio(byte[] buffer) {
		HttpURLConnection conn = null;
		FileInputStream inputStream = null;
		try {
			URL url = new URL(this.mWebApiUrl + "?appkey=" + this.mAppKey
					+ "&userid=" + this.mUserId + "&id=" + this.mDeviceId);
			conn = (HttpURLConnection) url.openConnection();
			// 上传的语音数据流格式
			conn.setRequestProperty("Content-Type", "audio/x-wav;codec=pcm;bit=" + this.mAudioFormat + ";rate=" + this.mSampleRate);
			// 识别结果返回的格式
			conn.setRequestProperty("Accept", "text/plain");
			// 语音数据流记录的语种及识别结果的语种
			conn.setRequestProperty("Accept-Language", this.mLanguage);
			// 编码格式
			conn.setRequestProperty("Accept-Charset", this.mCharset);
			// 指定ASR识别的引擎
			conn.setRequestProperty("Accept-Topic", this.mEngine);

			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStream out = conn.getOutputStream();

			// 上传语音数据
			out.write(buffer);
			out.flush();
			out.close();

			// 获取结果
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader resultReader = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), this.mCharset));
				String line = "";
				String result = "";
				while ((line = resultReader.readLine()) != null) {
					result += line;
				}
				resultReader.close();
				return result;
			} else {
				Log.w(TAG, "Parse failed!  ResponseCode = " + conn.getResponseCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}