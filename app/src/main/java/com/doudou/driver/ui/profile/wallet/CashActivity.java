package com.doudou.driver.ui.profile.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.data.models.UserBean;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.CallServer;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.StringUtil;
import com.doudou.driver.utils.secure.Md5;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class CashActivity extends BaseActivity implements HttpListener{


    @BindView(R.id.editCashCount)
    EditText editCashCount;
    @BindView(R.id.cashInto)
    TextView cashInto;
    @BindView(R.id.editAccountName)
    EditText editAccountName;
    @BindView(R.id.editAccountNumber)
    EditText editAccountNumber;
    @BindView(R.id.balanceCount)
    TextView balanceCount;

    @BindView(R.id.accountRadioGroup)
    RadioGroup accountRadioGroup;

    UserDataPreference userDataPreference;
    private int type = 0;
    private UserBean userBean;
    private static final int CASH = 0x001;
    private static final int GET_USER = 0x002;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cash);
        ButterKnife.bind(this);
        setTitle("提现");
        userDataPreference = new UserDataPreference(this);
        userBean = JSON.parseObject(userDataPreference.getUserInfo(), UserBean.class);
        balanceCount.setText(getString(R.string.cash_count,String.valueOf(userBean.getBalance())));
        accountRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ali:
                        type = 1;
                        break;
                    case R.id.bank:
                        type = 2;
                        break;
                }
            }
        });
    }


    @OnClick(R.id.btnChangeToCash)
    public void onViewClicked() {
        double balance = StringUtil.parseDoubleMoney(editCashCount.getText().toString());
        if (balance==0){
            showMsg("请输入提现金额");
        }else if (TextUtils.isEmpty(editAccountName.getText().toString())){
            showMsg("请输入账户姓名");
        }else if (TextUtils.isEmpty(editAccountNumber.getText().toString())){
            showMsg("请输入账号");
        }else if (type==0){
            showMsg("请选择账户类型");
        }else{
            if (balance>userBean.getBalance()){
                showMsg("提现金额不能大于"+userBean.getBalance()+"元");
            }else{
                cash(balance,editAccountNumber.getText().toString(),editAccountName.getText().toString(),type);
            }
        }

    }

    /**
     * @param  balance 提现额度(元)
     * @param  payaccount 提现目标账号
     * @param  type 提现类型[1支付宝,2银行卡]
     */
    private void cash(double balance ,String payaccount,String payName,int type){
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.WITHDRAW)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("balance",balance)
                .add("payaccount", payaccount)
                .add("name", payName)
                .add("type", type);
        request(CASH, baseRequest, this, false, true);
    }


    private void getUser() {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.LOGIN)
                .add("clientid", JPushInterface.getRegistrationID(this))
                .add("phone", userDataPreference.getAccount())
                .add("password", Md5.getMd5(userDataPreference.getPassword()));
        CallServer.getRequestInstance().add(this, GET_USER, baseRequest, this, false, false);
    }

    @Override
    public void onSucceed(int what, String response) {
        if (what==CASH){
            showMsg("提现成功，等待后台审核");
            getUser();
        }else{
            userDataPreference.setUserInfo(response);
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
}
