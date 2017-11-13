package com.doudou.driver.ui.splash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.doudou.driver.R;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.CallServer;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.ui.login.LoginActivity;
import com.doudou.driver.ui.main.MainActivity;
import com.doudou.driver.utils.ConfigUtil;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashActivity extends AppCompatActivity implements HttpListener {


    @BindView(R.id.imgAdv)
    ImageView imgAdv;
    private int delayTime = 3000;

    private Runnable jumpToHomeRunnable = new Runnable() {

        @Override
        public void run() {
            jumpToHome();
        }
    };
    private Handler handler = new Handler();
    AdvData advData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this);
        getAdv();
    }


    public void jumpToHome() {
        Intent intent;
        UserDataPreference sp = new UserDataPreference(this);
        if (!TextUtils.isEmpty(sp.getToken())) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }


    private void getAdv() {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_SYSTEM_AdV)
                .add("com", 2);
        CallServer.getRequestInstance().add(this, 2, baseRequest, this, false, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        try {
            advData = JSON.parseObject(response, AdvData.class);
            Glide.with(this)
                    .load(advData.getPic())
                    .placeholder(R.drawable.welcome)
                    .error(R.drawable.welcome)
                    .centerCrop()
                    .into(imgAdv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.postDelayed(jumpToHomeRunnable, delayTime);

    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        handler.postDelayed(jumpToHomeRunnable, delayTime);
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        handler.postDelayed(jumpToHomeRunnable, delayTime);
    }

    @OnClick(R.id.imgAdv)
    public void onViewClicked() {
        if (advData != null) {
            if (!TextUtils.isEmpty(advData.getUrl())) {
                Uri uri = Uri.parse(advData.getUrl());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("status", "onWindowFocusChanged");
        if (hasFocus) {
            handler.postDelayed(jumpToHomeRunnable, delayTime);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            try {
                handler.removeCallbacksAndMessages(null);
            } catch (Exception e) {

            }
        }
    }
}
