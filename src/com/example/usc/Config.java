/**
 * Config.java [V 1.0.0]
 * classes : cn.yunzhisheng.prodemo.Config
 * liujunjie Create  at 2015-1-9  ??4:30:32
 */
package com.example.usc;

/**
 * @project SampleAndroid   
 * @class Config 
 * @description       
 * @author yxmsw2007
 * @version   
 * @email yxmsw2007@gmail.com  
 * @data 2015-7-4 下午10:04:55    
 */
public class Config {
	
	public static final String URL = "http://api.hivoice.cn/USCService/WebApi";
	
	/**
	 * 云知声开放平台网站上开发者的用户名
	 */
	public static final String USER_ID = "YZS1435890548092";
	
	/**
	 * 云知声开放平台网站上申请应用后获得的 appKey
	 */
	public static final String APP_KEY = "re75bz2b7qu3gd7lupkpw6uonnsejr7t5ueresyv";

	/**
	 * 标记请求来源的标识，如用户所设备序列号 (SN)，IMEI，MAC地址等
	 */
	public static final String DEVICE_ID = "IMEI1234567890";
	
	public static final int SAMPLE_RATE = 16000;
	
	public static final int AUDIO_FORMATE = 16;
	
	public static final String LANGUAGE = "zh_CN";
	
	public static final String CHARSET = "utf-8";
	
	/**
	 * "general", "poi", "song", "movietv", "medical"
	 */
	public static final String ENGINE = "general";

}
