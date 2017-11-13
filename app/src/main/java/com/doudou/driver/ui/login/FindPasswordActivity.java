package com.doudou.driver.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.StringUtil;
import com.doudou.driver.utils.ToastUtil;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPasswordActivity extends BaseActivity implements HttpListener {


    @BindView(R.id.btnGetCode)
    Button btnGetCode;
    @BindView(R.id.phone)
    EditText editPhone;
    @BindView(R.id.password)
    EditText editPassword;


    String phone;
    String code;
    private TimeCount time;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        setTitle(R.string.title_find_password);
    }


    @OnClick({R.id.btnGetCode, R.id.next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGetCode:
                getCode();
                break;
            case R.id.next_step:
                nextStep();
                break;
        }
    }

    //获取验证码
    private void getCode() {
        phone = editPhone.getText().toString();
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        if (TextUtils.isEmpty(editPhone.getText().toString())) {
            ToastUtil.showToast(this, "请输入手机号");
        } else if (!StringUtil.isMobileNO(editPhone.getText().toString())) {
            ToastUtil.showToast(this, "手机格式不正确");
        } else {
            BaseRequest baseRequest = new BaseRequest(ConfigUtil.FIND_PWD_BACK)
                    .add("phone", editPhone.getText().toString());
            request(0, baseRequest, this, false, true);
        }
    }

    private void nextStep() {

        if (TextUtils.isEmpty(editPhone.getText().toString())) {
            ToastUtil.showToast(this, "请输入手机号");
        } else if (TextUtils.isEmpty(editPassword.getText().toString())) {
            ToastUtil.showToast(this, "请输入验证码");
        } else if (!code.equals(editPassword.getText().toString())) {
            ToastUtil.showToast(this, "验证码不正确");
        } else {
            if (TextUtils.equals(phone, editPhone.getText().toString())) {
                Intent intent = new Intent(this, FindPwdSetActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            } else {
                showMsg("手机号不正确");
            }
        }

    }


    @Override
    public void onSucceed(int what, String response) {
        ToastUtil.showToast(this, "验证码已发生到您的手机,请注意查收");
        time.start();
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject.containsKey("codes")) {
            code = jsonObject.getString("codes");
        }
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        showMsg(msg);
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    // 定时器，倒计时
    @SuppressWarnings("deprecation")
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btnGetCode.setTextColor(getResources().getColor(
                    R.color.colorPrimary));
            btnGetCode.setText("重新验证");
            btnGetCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btnGetCode.setTextColor(Color.GRAY);
            btnGetCode.setClickable(false);
            btnGetCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }

}
