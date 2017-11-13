package com.doudou.driver.ui.main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.doudou.driver.R;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.data.models.UserBean;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.CallServer;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.ToastUtil;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AcceptOrderModelActivity extends Activity implements HttpListener {

    @BindView(R.id.handModel)
    RadioButton handModel;
    @BindView(R.id.autoModel)
    RadioButton autoModel;
    @BindView(R.id.modelRG)
    RadioGroup modelRG;
    @BindView(R.id.realTimeRB)
    RadioButton realTimeRB;
    @BindView(R.id.bookTimeRB)
    RadioButton bookTimeRB;
    @BindView(R.id.allTimeRB)
    RadioButton allTimeRB;
    @BindView(R.id.orderTypeRG)
    RadioGroup orderTypeRG;

    UserDataPreference userDataPreference;
    int model = 3;
    int acceptModel = 0;//0 手动 1 自动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order_model);
        ButterKnife.bind(this);
        getWindow().setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        userDataPreference = new UserDataPreference(this);
        UserBean userBean = JSON.parseObject(userDataPreference.getUserInfo(), UserBean.class);
        model = userBean.getMode();
        if (model == 1) {
            realTimeRB.setChecked(true);
        } else if (model == 2) {
            bookTimeRB.setChecked(true);
        } else {
            allTimeRB.setChecked(true);
        }
        acceptModel = userDataPreference.getAcceptModel();
        if (acceptModel == 0) {
            handModel.setChecked(true);
        } else if (acceptModel == 1) {
            autoModel.setChecked(true);
        }
        modelRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.handModel:
                        acceptModel = 0;
                        break;
                    case R.id.autoModel:
                        acceptModel = 1;
                        break;
                }
            }
        });
        orderTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.realTimeRB:
                        model = 1;
                        break;
                    case R.id.bookTimeRB:
                        model = 2;
                        break;
                    case R.id.allTimeRB:
                        model = 3;
                        break;
                }
            }
        });
    }

    @OnClick({R.id.btnCancel, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnSubmit:
                setMode(model);
                break;
        }
    }

    private void setMode(int mode) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.EDIT_MODEL)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("mode", mode);
        CallServer.getRequestInstance().add(this, 0, baseRequest, this, false, true);
    }

    @Override
    public void onSucceed(int what, String response) {
        userDataPreference.setAcceptModel(acceptModel);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        ToastUtil.showToast(this, "设置失败");
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }
}
