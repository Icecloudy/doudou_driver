package com.doudou.driver.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.data.BaseDataPreference;
import com.doudou.driver.data.GlobalPreference;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.data.models.UserBean;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.ui.main.MainActivity;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.StringUtil;
import com.doudou.driver.utils.ToastUtil;
import com.doudou.driver.utils.secure.Md5;
import com.orhanobut.logger.Logger;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity implements HttpListener {


    @BindView(R.id.phone)
    EditText editPhone;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.user_agreement)
    AppCompatTextView userAgreement;
    String phone;
    String pwd;

    BaseDataPreference baseDataPreference;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        addForeColorSpan();
        setTitle(R.string.login);
        setBackBar(false);
        baseDataPreference = new BaseDataPreference(this);

        editPhone.setText(baseDataPreference.getLoginMobile());
    }

    private void addForeColorSpan() {
        SpannableString spanString = new SpannableString(getString(R.string.user_agreement));
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        spanString.setSpan(span, 12, getString(R.string.user_agreement).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        userAgreement.append(spanString);
    }


    @OnClick({R.id.find_password, R.id.login, R.id.user_agreement, R.id.Driver})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.find_password:
                intent = new Intent(this, FindPasswordActivity.class);
                break;
            case R.id.login:
                Login();
                break;
            case R.id.user_agreement:
              intent = new Intent(this, UserAgreementActivity.class);
                intent.putExtra("title","用户协议");
                break;
            case R.id.Driver:
                intent = new Intent(this, DriverAuthOneActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void Login() {
        String registerId = JPushInterface.getRegistrationID(this);
        phone = editPhone.getText().toString();
        pwd = password.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(this, "请输入手机号");
        }else if (!StringUtil.isMobileNO(editPhone.getText().toString())) {
            ToastUtil.showToast(this, "手机格式不正确");
        } else if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showToast(this, "请输入密码");
        } else {
            BaseRequest baseRequest = new BaseRequest(ConfigUtil.LOGIN)
                    .add("clientid", registerId)
                    .add("phone", phone)
                    .add("password", Md5.getMd5(pwd));
            request(0, baseRequest, this, false, true);
        }
    }

    @Override
    public void onSucceed(int what, String response) {
        Logger.d(response);
        try {
            baseDataPreference.setLoginMobile(phone);
            UserBean userBean = JSON.parseObject(response, UserBean.class);

            GlobalPreference sp = new GlobalPreference(this);
            sp.setCurrentUid(userBean.getId());

            UserDataPreference userDataPreference = new UserDataPreference(this);
            userDataPreference.setUserInfo(response);
            userDataPreference.setProfile(response);
            userDataPreference.setPassword(pwd);
            userDataPreference.setToken(userBean.getToken());
            userDataPreference.setAccount(userBean.getPhone());
            userDataPreference.setId(userBean.getId());

            startActivity(new Intent(this, MainActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        showMsg(msg);
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }
}
