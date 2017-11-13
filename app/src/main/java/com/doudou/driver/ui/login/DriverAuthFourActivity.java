package com.doudou.driver.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.ui.login.model.DriverRegBean;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.ToastUtil;
import com.doudou.driver.utils.secure.Md5;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.rest.Response;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverAuthFourActivity extends BaseActivity implements HttpListener {


    @BindView(R.id.editPwd)
    EditText editPwd;
    @BindView(R.id.editConPwd)
    EditText editConPwd;
    DriverRegBean regBean = new DriverRegBean();

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_driver_auth_four);
        ButterKnife.bind(this);
        setTitle(R.string.driver_auth);
        initData();
    }


    private void initData() {
        String data = getIntent().getExtras().getString("DriverRegBean");
        regBean = JSON.parseObject(data, DriverRegBean.class);
    }

    @OnClick({R.id.las_step, R.id.next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.las_step:
                finish();
                break;
            case R.id.next_step:
                String pwd = editPwd.getText().toString();
                String Newpwd = editConPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.showToast(this, "请输入密码");
                }else if (pwd.length()<6) {
                    ToastUtil.showToast(this, "请设置6位以上密码");
                }  else if (TextUtils.isEmpty(Newpwd)) {
                    ToastUtil.showToast(this, "请再次输入密码");
                } else {
                    if (!pwd.equals(Newpwd)) {
                        ToastUtil.showToast(this, "密码不一致");
                    } else {
                        driverRegister();
                    }
                }
                break;
        }
    }

    /**
     * Created by Administrator on 2017/4/20.*
     * phone 手机号
     * username 真实姓名
     * drivingn 驾驶证号
     * startdate 驾驶证初次领取日期(0000-00-00)
     * driving 驾驶证照片
     * carnum 车牌号 选择+输入
     * brand 车型 如野马T80
     * holder 车辆所有人
     * carregdate 车辆注册时间(0000-00-00)
     * color 车身颜色
     * carphoto 车辆照片
     * password 密码
     */
    private void driverRegister() {
        FileBinary fileBinary1 = new FileBinary(new File(regBean.getDriving()));
        FileBinary fileBinary2 = new FileBinary(new File(regBean.getCarphoto()));
        FileBinary fileBinary3 = new FileBinary(new File(regBean.getLicense()));
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.DRIVER_REGISTER);
        baseRequest.add("phone", regBean.getPhone());
        baseRequest.add("username", regBean.getUsername());
        baseRequest.add("drivingn", regBean.getDrivingn());
        baseRequest.add("startdate", regBean.getStartdate());
        baseRequest.add("driving", fileBinary1);
        baseRequest.add("carnum", regBean.getCarnum());
        baseRequest.add("brand", regBean.getBrand());
        baseRequest.add("holder", regBean.getHolder());
        baseRequest.add("carregdate", regBean.getCarregdate());
        baseRequest.add("color", regBean.getColor());
        baseRequest.add("carphoto", fileBinary2);
        baseRequest.add("license", fileBinary3);
        baseRequest.add("password", Md5.getMd5(editPwd.getText().toString()));
        request(0, baseRequest, this, false, true);
    }


    @Override
    public void onSucceed(int what, String response) {
        startActivity(new Intent(this,DriverAuthDoneActivity.class));
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        showMsg(msg);
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }
}
