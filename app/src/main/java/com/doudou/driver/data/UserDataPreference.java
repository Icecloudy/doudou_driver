package com.doudou.driver.data;

import android.content.Context;

import com.doudou.driver.utils.StringUtil;


/**
 * Created by jimmy on 2016/11/14.
 */

public class UserDataPreference extends BaseDataPreference {
    public static final String TOKEN = "token";

    public UserDataPreference(Context context) {
        super();
        GlobalPreference globalSp = new GlobalPreference(context);
        int uid = globalSp.getCurrentUid();
        sharedPreferences = context.getSharedPreferences("driver_data_" + uid,
                Context.MODE_PRIVATE);

    }

    public String getToken() {
        return getDataString("token");
    }

    public void setToken(String token) {
        setDataString("token", token);
    }


    public void setAccount(String mobile) {
        setDataString("mobile", mobile);
    }

    public String getAccount() {
        return getDataString("mobile");
    }

    public int getId() {
        return getDataInt("id");
    }

    public void setId(int id) {

        setDataInt("id", id);
    }

    public String getUserInfo() {
        return getDataString("userInfo", "");
    }

    public void setUserInfo(String s) {

        setDataString("userInfo", s);
    }

    public String getProfile() {
        return getDataString("Profile", "");
    }

    public void setProfile(String s) {

        setDataString("Profile", s);
    }

    public String getPassword() {
        return getDataString("password", "");
    }

    public void setPassword(String s) {

        setDataString("password", s);
    }

    public void setOrderStartTime() {
        setDataString("order_start_time", StringUtil.getHttpRequestTimestamp());
    }

    public String getOrderStartTime() {
        return getDataString("order_start_time");
    }

    public void clear() {
        // 退出登录如果同时选择清空数据，使用此方法
        sharedPreferences.edit().clear().apply();
    }

    public void setVoice(boolean voice) {
        setDataBoolean("remind", voice);
    }

    public boolean getVoice() {
        return getDataBoolean("remind");
    }

    public void setWorkModel(int statue) {
        setDataInt("workModel", statue);
    }

    public int getWorkModel() {
        return getDataInt("workModel");
    }

    public void setAcceptModel(int statue) {
        setDataInt("AcceptModel", statue);
    }

    public int getAcceptModel() {
        return getDataInt("AcceptModel",0);
    }

    public void setNotify(int orderId, boolean statue) {
        setDataBoolean("Notify" + orderId, statue);
    }

    public boolean getNotify(int orderId) {
        return getDataBoolean("Notify" + orderId,false);
    }

    public void setSetOffNotify(int orderId, boolean statue) {
        setDataBoolean("SetOffNotify" + orderId, statue);
    }

    public boolean getSetOffNotify(int orderId) {
        return getDataBoolean("SetOffNotify" + orderId,false);
    }
    public void setPosition(String latlng){
        setDataString("lastPosition", latlng);
    }
    public String getPosition(){
        return getDataString("lastPosition");
    }

    public void setDistance(int orderId,String distance){
        setDataString("distance"+orderId,distance);
    }
    public String getDistance(int orderId){
        return getDataString("distance"+orderId);
    }
}
