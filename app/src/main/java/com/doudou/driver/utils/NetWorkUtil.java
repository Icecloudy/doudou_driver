package com.doudou.driver.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/***
 * 获取一些网络相关信息
 * @author zhangMinfeng
 * @date 2016-03-08 16:19:32
 */
public class NetWorkUtil {

	private static String TAG="NetWorkUtil";

	/** 
     * 检测当的网络（WLAN、3G/2G）状态 ,是否可用
     * @param context Context 
     * @return true 表示网络可用 
     */  
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {  
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())   
            {  
                // 当前网络是连接的  
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {  
                    // 当前所连接的网络可用  
                    return true;  
                }  
            }  
        }  
        return false;  
    }
	/**
	 * 判断WIFI网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) { 
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
			.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
			.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) { 
				return mWiFiNetworkInfo.isAvailable(); 
			} 
		} 
		return false; 
	}
	
	/**
	 * 获取当前网络连接的类型信息
	 * @param context
	 * @return 0:手机网络；1：WiFi；-1：没有网络连接
	 */
	public static int getConnectedType(Context context) {
		if (context != null) { 
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
			.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) { 
				return mNetworkInfo.getType(); 
			} 
		} 
		return -1; 
	}
	
	public static String netTypeToString(int typeid){
		String str="";
		switch (typeid) {
		case 0:
			str = "mobile";
			break;
		case 1:
			str = "wifi";
			break;
		default:
			str = "none";
			break;
		}
		return str;
	}	
	/**
	 *获取网络类型，WiFi，2G,3G，4G
	 * @param context 上下文对象
	 * @return
	 */
	public static String getCurrentNetType(Context context) {
		String type = "";
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			type = "null";
		} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			type = "wifi";
		} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			int subType = info.getSubtype();
			if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
			|| subType == TelephonyManager.NETWORK_TYPE_EDGE) {
				type = "2g";
			} else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
			|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
			|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
				type = "3g";
			} else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
				type = "4g";
			}
		}
		return type;
	}

	/**
	   * 判断移动网络是否开启
	   * 
	   * @param context
	   * @return
	   */
	  public static boolean isNetEnabled(Context context) {
	    TelephonyManager tm = (TelephonyManager) context
	        .getSystemService(Context.TELEPHONY_SERVICE);
	    if (tm != null) {
	      if (tm.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
	        Log.i(TAG, "移动网络已经开启");
	        return true;
	      }
	    }
	    Log.i(TAG, "移动网络还未开启");
	    return false;
	  }

	  /**
	   * 判断WIFI网络是否开启
	   * 
	   * @param context
	   * @return
	   */
	  public static boolean isWifiEnabled(Context context) {
	    WifiManager wm = (WifiManager) context
	        .getSystemService(Context.WIFI_SERVICE);
	    if (wm != null && wm.isWifiEnabled()) {
	      Log.i(TAG, "Wifi网络已经开启");
	      return true;
	    }
	    Log.i(TAG, "Wifi网络还未开启");
	    return false;
	  }

	  /**
	   * 判断移动网络是否连接成功
	   * 
	   * @param context
	   * @return
	   */
	  public static boolean isNetContected(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context
	        .getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (cm != null && info != null && info.isConnected()) {
	      Log.i(TAG, "移动网络连接成功");
	      return true;
	    }
	    Log.i(TAG, "移动网络连接失败");
	    return false;
	  }

	  /**
	   * 判断WIFI是否连接成功
	   * 
	   * @param context
	   * @return
	   */
	  public static boolean isWifiContected(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context
	        .getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (info != null && info.isConnected()) {
	      Log.i(TAG, "Wifi网络连接成功");
	      return true;
	    }
	    Log.i(TAG, "Wifi网络连接失败");
	    return false;
	  }
	  
	  /***
	   * isWifiContected () 在模拟器上可以正确判断wifi是否可用,但是在真机上就判断不出来。wifi是断开的，但是返回的结果true,造成wifi判断不准确。经过尝试可使用如下的方法判断方能正确:
	   * @param inContext
	   * @return
	   */
	  public static boolean isWiFiActive(Context inContext) {
	        Context context = inContext.getApplicationContext();
	        ConnectivityManager connectivity = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (connectivity != null) {   
	            NetworkInfo[] info = connectivity.getAllNetworkInfo();
	            if (info != null) {   
	                for (int i = 0; i < info.length; i++) {   
	                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {   
	                        return true;   
	                    }   
	                }   
	            }   
	        }   
	        return false;   
	    }  
	   /**
	    * Android获取当前连接的wifi名称 ssid
	    * @param inContext
	    * @return
	    */
	  public static String getConnectWifiSsid(Context inContext){
	          WifiManager wifiManager = (WifiManager)inContext.getSystemService(Context.WIFI_SERVICE);
	          WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	          Log.d("wifiInfo", wifiInfo.toString());
	          Log.d("SSID",wifiInfo.getSSID());
	          if(wifiInfo.getSSID()==null) return "";
	          else return wifiInfo.getSSID(); 
	         
	   }

	  /**
	   * 判断移动网络和WIFI是否开启
	   * 
	   * @param context
	   * @return
	   */
	  public static boolean isNetWorkEnabled(Context context) {
	    return (isNetEnabled(context) || isWifiEnabled(context));
	  }

    
 
	
}
