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
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.data.models.OrderCancelBean;
import com.doudou.driver.data.models.PassengerUser;
import com.doudou.driver.view.RoundAngleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

import static com.doudou.driver.R.id.diverMoblie;

public class OrderCancelDetailActivity extends BaseActivity {


    @BindView(R.id.profile_image)
    RoundAngleImageView profileImage;
    @BindView(R.id.diverName)
    TextView diverName;
    @BindView(diverMoblie)
    TextView diverMobile;
    @BindView(R.id.tvCancelReason)
    TextView tvCancelReason;

    String phone;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_cancel_detail);
        setTitle("取消原因");
        ButterKnife.bind(this);

        showOrderDialog();
        setBackToMainAtyFromCancel();
    }

    private void showOrderDialog() {
        if (null != getIntent().getExtras()) {
            Bundle bundle = getIntent().getExtras();
            String extraInfo = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                OrderCancelBean cancelBean = JSON.parseObject(extraInfo, OrderCancelBean.class);
                String cancelReason = cancelBean.getReason();
                tvCancelReason.setText(getString(R.string.cancel_reason, cancelReason));
                setData(cancelBean.getUser());
                SysApplication.getInstance().exit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setData(PassengerUser user) {
        phone = user.getPhone();
        Glide.with(this)
                .load(user.getPhoto())
                .placeholder(R.drawable.ic_head)
                .error(R.drawable.ic_head)
                .crossFade()
                .centerCrop()
                .into(profileImage);
        diverName.setText(TextUtils.isEmpty(user.getNickname()) ? "乘客" : user.getNickname());
        diverMobile.setText(user.getPhone());

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
     * Show message dialog.
     */
    public void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("打电话联系乘客");
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(OrderCancelDetailActivity.this,
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
