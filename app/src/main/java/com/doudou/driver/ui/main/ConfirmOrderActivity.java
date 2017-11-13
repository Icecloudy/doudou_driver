package com.doudou.driver.ui.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.data.models.OrderInfo;
import com.doudou.driver.data.models.PassengerUser;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.StringUtil;
import com.doudou.driver.view.RoundAngleImageView;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmOrderActivity extends BaseActivity implements HttpListener {

    UserDataPreference userDataPreference;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.profile_image)
    RoundAngleImageView profileImage;
    @BindView(R.id.diverName)
    TextView diverName;
    @BindView(R.id.diverMoblie)
    TextView diverMoblie;
    @BindView(R.id.orderDate)
    TextView orderDate;
    @BindView(R.id.setOutLocation)
    TextView setOutLocation;
    @BindView(R.id.EsLocation)
    TextView EsLocation;
    @BindView(R.id.tvOrderMoney)
    TextView tvOrderMoney;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;
    @BindView(R.id.tvOrderDistance)
    TextView tvOrderDistance;


    OrderInfo orderInfo = new OrderInfo();
    double OrderCost;
    double distance;
    String phone;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        setTitle(R.string.confirm_order);
        userDataPreference = new UserDataPreference(this);
        setData();
    }

    private void setData() {
        String goTime = userDataPreference.getOrderStartTime();
        if (getIntent().getExtras() != null) {
            String pUserInfo = getIntent().getExtras().getString("orderInfo");
            orderInfo = JSON.parseObject(pUserInfo, OrderInfo.class);
            OrderCost = getIntent().getExtras().getDouble("cost");
            distance = getIntent().getExtras().getDouble("distance");
            tvOrderMoney.setText(getString(R.string.order_money, String.valueOf(OrderCost)));
            if (TextUtils.isEmpty(userDataPreference.getOrderStartTime())){
                goTime = orderInfo.getGotime();
            }
            int time =(int) StringUtil.compareDate(StringUtil.getHttpRequestTimestamp(),goTime);
            tvOrderTime.setText(getString(R.string.order_time, time));
            tvOrderDistance.setText(getString(R.string.order_distance, StringUtil.doubleFormat( distance)));
            orderNumber.setText(getString(R.string.order_number, orderInfo.getNmber()));
            PassengerUser user = orderInfo.getUser();
            Glide.with(this)
                    .load(user.getPhoto())
                    .placeholder(R.drawable.ic_head)
                    .error(R.drawable.ic_head)
                    .crossFade()
                    .centerCrop()
                    .into(profileImage);
            phone = user.getPhone();
            diverName.setText(TextUtils.isEmpty(user.getNickname()) ? "乘客" : user.getNickname());
            diverMoblie.setText(user.getPhone());
            setOutLocation.setText(getString(R.string.set_out_location, orderInfo.getAddresses()));
            EsLocation.setText(getString(R.string.get_off_location, orderInfo.getDown()));
            orderDate.setText(StringUtil.getHttpRequestTimestamp());
        }
    }

    @OnClick({R.id.imgCallDiver, R.id.imgCallDiverVoice, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCallDiver:
                showMessageDialog();
                break;
            case R.id.imgCallDiverVoice:
                break;
            case R.id.login:
                completeOrder(orderInfo.getId(), OrderCost, distance);
                break;
        }
    }
    /**
     * Show message dialog.
     */
    public void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.nav_service);
        builder.setMessage("打电话联系乘客");
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(ConfirmOrderActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void completeOrder(int orderid, double price, double distance) {
        BaseRequest request = new BaseRequest(ConfigUtil.DRIVER_ARRIVE)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("orderid", orderid)
                .add("price", StringUtil.doubleFormatD(price))
                .add("distance", StringUtil.doubleFormatD(distance));
        request(0, request, this, false, true);
    }


    @Override
    public void onSucceed(int what, String response) {
        showMsg("订单已完成");
        userDataPreference.setWorkModel(0);
        startActivity(new Intent(this,MainActivity.class));
        SysApplication.getInstance().exit();
        finish();
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        showMsg(msg);
        Log.e("123",msg);
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }
}
