package com.doudou.driver.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverAuthDoneActivity extends BaseActivity {

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_driver_auth_done);
        setTitle("提交成功");
        ButterKnife.bind(this);
        setBackBar(false);
    }


    @OnClick(R.id.backToLogin)
    public void onViewClicked() {
        startActivity(new Intent(this,LoginActivity.class));
        SysApplication.getInstance().exit();
    }
}
