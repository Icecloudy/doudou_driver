package com.doudou.driver.ui.profile.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.data.models.UpdateBean;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.ui.login.LoginActivity;
import com.doudou.driver.ui.login.UserAgreementActivity;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.StringUtil;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.doudou.driver.R.id.tvVersionUpdate;

public class SettingsActivity extends BaseActivity implements HttpListener {

    @BindView(R.id.switchSounds)
    Switch switchSounds;
    @BindView(R.id.tvIsNewVersion)
    TextView tvIsNewVersion;
    boolean isNeedUpdate;
    UpdateBean updateBean = new UpdateBean();
    UserDataPreference userDataPreference;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setTitle(R.string.nav_setting);
        getAppUpdata();
        userDataPreference = new UserDataPreference(this);
        switchSounds.setChecked(!userDataPreference.getVoice());
        switchSounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDataPreference.setVoice(!isChecked);
            }
        });
    }


    @OnClick({tvVersionUpdate, R.id.tvFeedback, R.id.tvAgreement, R.id.tvAbout, R.id.mine_logout})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case tvVersionUpdate:
                if (isNeedUpdate) {
                    showUpdateDialog(updateBean);
                } else {
                    showMsg("已是最新版本");
                }
                break;
            case R.id.tvFeedback:
                intent = new Intent(this, FeedbackActivity.class);
                break;
            case R.id.tvAgreement:
                intent = new Intent(this, UserAgreementActivity.class);
                intent.putExtra("title", "用户协议");
                break;
            case R.id.tvAbout:
                intent = new Intent(this, AboutLexingActivity.class);
                break;
            case R.id.mine_logout:
                showMessageDialog(R.string.logout, "确定退出吗？", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //下班
                        if (userDataPreference.getWorkModel() != 1) {
                            setWorkModel(1);
                        } else {
                            new UserDataPreference(SettingsActivity.this).removeData(UserDataPreference.TOKEN);
                            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                            SysApplication.getInstance().exit();
                        }

                    }
                });

                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /*

    * @param app_id 安装包类型[1安卓乘客,2安卓司机,3苹果乘客,4苹果司机]
	 * @param version_id 当前版本号 [1.0/1.0.1/10.0.1等都可以]

     */
    private void getAppUpdata() {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.APP_UPDATE)
                .add("app_id", 2)
                .add("version_id", StringUtil.getPackageVersionName(this));
        request(0, baseRequest, this, false, false);
    }

    private static final int SET_STATE = 0x003;

    private void setWorkModel(int status) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.SET_DRIVER_STATE)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("status", status);//[0上班,1下班(默认),2载客中]
        request(SET_STATE, baseRequest, this, false, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        if (what == SET_STATE) {
            new UserDataPreference(SettingsActivity.this).removeData(UserDataPreference.TOKEN);
            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            SysApplication.getInstance().exit();
        } else {
            updateBean = JSON.parseObject(response, UpdateBean.class);
            if (updateBean.getStatus() == 0) {//	status(int) [0不用更新,1可更新,2强制更新]
                tvIsNewVersion.setText("已是最新版本");
            } else {
                tvIsNewVersion.setText("发现新版本");
                isNeedUpdate = true;
            }
        }
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        showMsg(msg);
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    private void showUpdateDialog(final UpdateBean updateBean) {
        showMessageDialog("更新提示", updateBean.getContent(), "更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(updateBean.getApk_url());
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    }
                }
        );
    }
}
