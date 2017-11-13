package com.doudou.driver.ui.profile.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.StringUtil;
import com.doudou.driver.utils.ToastUtil;
import com.doudou.driver.utils.secure.Md5;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetPasswordActivity extends BaseActivity implements HttpListener {
    @BindView(R.id.btn_identifying_code)
    Button btnIdentifyingCode;
    @BindView(R.id.editMobile)
    EditText editMobile;
    @BindView(R.id.editCode)
    EditText editCode;
    @BindView(R.id.editPwd)
    EditText editPwd;
    @BindView(R.id.editConPwd)
    EditText editConPwd;
    @BindView(R.id.submit)
    AppCompatButton submit;
    private String title;
    UserDataPreference userDataPreference;
    String code = "code";
    private TimeCount time;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        userDataPreference = new UserDataPreference(this);
        if (getIntent().getExtras() != null) {
            title = getIntent().getExtras().getString("title");
            setTitle(title);
        } else {
            setTitle(R.string.change_password);
        }
    }


    @OnClick({R.id.btn_identifying_code, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_identifying_code:
                getCode();
                break;
            case R.id.submit:

                changePassword();
                break;
        }
    }

    /**
     */
    private void changePassword() {
        if (TextUtils.isEmpty(editMobile.getText().toString())) {
            ToastUtil.showToast(this, "请输入手机号");
        } else if (TextUtils.isEmpty(editCode.getText().toString())) {
            ToastUtil.showToast(this, "请输入验证码");
        } else if (!code.equals(editCode.getText().toString())) {
            ToastUtil.showToast(this, "验证码不正确");
        } else if (TextUtils.isEmpty(editPwd.getText().toString())) {
            ToastUtil.showToast(this, "请输入密码");
        } else if (editPwd.getText().toString().length()<6) {
            ToastUtil.showToast(this, "请设置6位以上密码");
        } else if (TextUtils.isEmpty(editConPwd.getText().toString())) {
            ToastUtil.showToast(this, "请再次输入密码");
        } else if (!editConPwd.getText().toString().equals(editPwd.getText().toString())) {
            ToastUtil.showToast(this, "两次密码不一致");
        } else if (!userDataPreference.getAccount().equals(editMobile.getText().toString())) {
            ToastUtil.showToast(this, "手机号不正确");
        }else if (userDataPreference.getPassword().equals(editPwd.getText().toString())) {
            ToastUtil.showToast(this, "新密码不能跟原来密码一样");
        }else{
            BaseRequest baseRequest = new BaseRequest(ConfigUtil.CHANGE_PWD)
                    .add("phone", userDataPreference.getAccount())
                    .add("token", userDataPreference.getToken())
                    .add("password", Md5.getMd5(userDataPreference.getPassword()))
                    .add("newpassword", Md5.getMd5(editPwd.getText().toString()));
            request(2, baseRequest, this, false, true);
        }
    }

    //获取验证码
    private void getCode() {
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        if (TextUtils.isEmpty(editMobile.getText().toString())) {
            ToastUtil.showToast(this, "请输入手机号");
        } else if (!StringUtil.isMobileNO(editMobile.getText().toString())) {
            ToastUtil.showToast(this, "手机格式不正确");
        } else if (!userDataPreference.getAccount().equals(editMobile.getText().toString())) {
            ToastUtil.showToast(this, "请输入本账户手机号");
        } else {
            BaseRequest baseRequest = new BaseRequest(ConfigUtil.FIND_PWD_BACK)
                    .add("phone", editMobile.getText().toString());
            request(1, baseRequest, this, false, true);
        }
    }

    @Override
    public void onSucceed(int what, String response) {
        if (what == 1) {
            ToastUtil.showToast(this, "验证码已发生到您的手机,请注意查收");
            time.start();
            JSONObject jsonObject = JSONObject.parseObject(response);
            if (jsonObject.containsKey("codes")) {
                code = jsonObject.getString("codes");
            }
        }else{
            showMsg("密码修改成功");
            JSONObject jsonObject = JSONObject.parseObject(response);
            if (jsonObject.containsKey("token")) {
                userDataPreference.setToken(jsonObject.getString("token"));
                userDataPreference.setPassword(editPwd.getText().toString());
            }
            finish();
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
            btnIdentifyingCode.setTextColor(getResources().getColor(
                    R.color.colorPrimary));
            btnIdentifyingCode.setText("重新验证");
            btnIdentifyingCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btnIdentifyingCode.setTextColor(Color.GRAY);
            btnIdentifyingCode.setClickable(false);
            btnIdentifyingCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
