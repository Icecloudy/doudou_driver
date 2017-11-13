package com.doudou.driver;


import com.iflytek.cloud.SpeechUtility;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.URLConnectionNetworkExecutor;
import com.yolanda.nohttp.cache.DBCacheStore;
import com.yolanda.nohttp.cookie.DBCookieStore;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends android.app.Application {

	private static MyApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		if (getApplicationContext() !=  null){
			init();
		}
	}
	private void init(){
		//科大讯飞
		SpeechUtility.createUtility(MyApplication.this, "appid=" + getString(R.string.app_id));
		//	JPush
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		//初始化NoHttp
		NoHttp.initialize(MyApplication.this);
		com.orhanobut.logger.Logger.init("666吊吊吊");
		Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
		Logger.setTag("NoHttpSample");// 设置NoHttp打印Log的tag。
		// 如果你需要自定义配置：
		NoHttp.initialize(MyApplication.this, new NoHttp.Config()
				// 设置全局连接超时时间，单位毫秒，默认10s。
				.setConnectTimeout(30 * 1000)
				// 设置全局服务器响应超时时间，单位毫秒，默认10s。
				.setReadTimeout(30 * 1000)
				// 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
				.setCacheStore(
						new DBCacheStore(MyApplication.this).setEnable(true) // 如果不使用缓存，设置false禁用。
				)
				// 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现。
				.setCookieStore(
						new DBCookieStore(MyApplication.this).setEnable(false) // 如果不维护cookie，设置false禁用。
				)
				// 配置网络层，默认使用URLConnection。
				.setNetworkExecutor(new URLConnectionNetworkExecutor())
		);
	}


	public static MyApplication getInstance() {
		return instance;
	}



}
