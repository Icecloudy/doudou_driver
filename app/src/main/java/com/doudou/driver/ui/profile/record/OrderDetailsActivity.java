package com.doudou.driver.ui.profile.record;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.view.RoundAngleImageView;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailsActivity extends BaseActivity implements HttpListener {

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
    UserDataPreference userDataPreference;
    OrderRecordBean recordBean;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        setTitle(R.string.order_details);

        userDataPreference = new UserDataPreference(this);
        if (getIntent().getExtras() != null) {
            int orderId = getIntent().getExtras().getInt("orderId");
            getOrderRecord(orderId);
        }
    }

    @OnClick({R.id.imgCallDiver, R.id.imgCallDiverVoice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCallDiver:
                showMessageDialog();
                break;
            case R.id.imgCallDiverVoice:
                break;
        }
    }

    /**
     * * @param int orderid 订单id
     *  userid 用户id / 司机id
     */
    private void getOrderRecord(int id) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_TRIP_RECORD_DETAILS)
                .add("orderid", id)
                .add("userid", userDataPreference.getId());
        request(0, baseRequest, this, false, true);

    }

    @Override
    public void onSucceed(int what, String response) {
        try {
            recordBean = JSON.parseObject(response,OrderRecordBean.class);
            setData(recordBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCodeError(int what, int code, String msg) {

    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }
    private void setData( OrderRecordBean data){
        Glide.with(this)
                .load(data.getPhoto())
                .placeholder(R.drawable.ic_head)
                .error(R.drawable.ic_head)
                .crossFade()
                .centerCrop()
                .into(profileImage);
        diverName.setText(TextUtils.isEmpty(data.getNickname()) ? "乘客" : data.getNickname());
        diverMoblie.setText(data.getPhone());
        orderDate.setText(data.getPlacetime());
        setOutLocation.setText(data.getAddresses());
        EsLocation.setText(data.getDown());

        tvOrderMoney.setText(getString(R.string.order_money, String.valueOf(data.getPrice())));
        tvOrderTime.setText(getString(R.string.order_time, data.getMinutes()));
        tvOrderDistance.setText(getString(R.string.order_distance, String.valueOf( data.getDistance())));
        orderNumber.setText(getString(R.string.order_number, data.getNmber()));

    }
    /**
     * Show message dialog.
     */
    public void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("联系乘客");
        builder.setMessage("打电话给乘客");
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + diverMoblie.getText().toString()));
                if (ActivityCompat.checkSelfPermission(OrderDetailsActivity.this,
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
}
