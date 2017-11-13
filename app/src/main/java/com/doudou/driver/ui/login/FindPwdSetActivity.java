package com.doudou.driver.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.ToastUtil;
import com.doudou.driver.utils.secure.Md5;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPwdSetActivity extends BaseActivity implements HttpListener {


    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;

    String phone;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_find_pwd_set);
        ButterKnife.bind(this);
        setTitle(R.string.title_find_password);
        phone = getIntent().getExtras().getString("phone");
    }


    @OnClick(R.id.done)
    public void onViewClicked() {
        submit();
    }

    private void submit() {
        if (TextUtils.isEmpty(password.getText().toString())) {
            showMsg("请输入新密码");
        } else if (password.getText().toString().length() < 6) {
            ToastUtil.showToast(this, "请设置6位以上密码");
        } else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {
            showMsg("请再次输入新密码");
        } else if (!TextUtils.equals(password.getText().toString(), confirmPassword.getText().toString())) {
            showMsg("两次密码不一致");
        } else {
            BaseRequest baseRequest = new BaseRequest(ConfigUtil.FIND_PWD_SET)
                    .add("password", Md5.getMd5(password.getText().toString()))
                    .add("phone", phone);
            request(0, baseRequest, this, false, true);
        }
    }

    @Override
    public void onSucceed(int what, String response) {
        showMsg("修改成功");
        startActivity(new Intent(this, LoginActivity.class));
        SysApplication.getInstance().exit();
        finish();
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        showMsg(msg);
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

}
