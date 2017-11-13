package com.doudou.driver.view;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doudou.driver.R;
import com.doudou.driver.data.models.OrderInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/25.
 */

public class OrderDialog {
    //    ImageView orderType;
//    TextView tvOrderDistance;
//    TextView tvOrderMoney;
//    ImageView imgLeft;
//    TextView orderDate;
//    TextView setOutLocation;
//    TextView EsLocation;
//    TextView tvReceive;
//    TextView countTimer;
//    RelativeLayout receiveOrder;
    @BindView(R.id.orderType)
    ImageView orderType;
    @BindView(R.id.tvOrderDistance)
    TextView tvOrderDistance;
    @BindView(R.id.tvOrderMoney)
    TextView tvOrderMoney;
    @BindView(R.id.imgLeft)
    ImageView imgLeft;
    @BindView(R.id.orderDate)
    TextView orderDate;
    @BindView(R.id.setOutLocation)
    TextView setOutLocation;
    @BindView(R.id.EsLocation)
    TextView EsLocation;
    @BindView(R.id.tvReceive)
    TextView tvReceive;
    @BindView(R.id.countTimer)
    TextView countTimer;
    @BindView(R.id.receiveOrder)
    RelativeLayout receiveOrder;
    @BindView(R.id.btnCancel)
    TextView cancel;
    @BindView(R.id.centerTextView)
    TextView centerTv;
    private Context context;
    private Dialog dialog;
    private Display display;
    OrderInfo info;
    private TimeCount time;

    public OrderDialog(Context context, OrderInfo orderInfo) {
        this.context = context;
        info = orderInfo;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public OrderDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order, null);
        LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        ButterKnife.bind(this, view);
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        mainLayout.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LinearLayout.LayoutParams.FILL_PARENT));

        setOrderInfo();
        time = new TimeCount(20000, 1000);// 构造CountDownTimer对象
        time.start();
        return this;
    }

    public void setOrderInfo() {//[1立即用车,2预约]
        tvOrderMoney.setText(info.getCost());
        if (info.getType() == 1) {
            imgLeft.setImageResource(R.drawable.flash);
            orderDate.setText("立即用车");
            orderType.setImageResource(R.drawable.ic_real_time);
        } else if (info.getType() == 3) {
            imgLeft.setImageResource(R.drawable.flash);
            orderDate.setText("立即用车");
            orderType.setImageResource(R.drawable.ic_send_for);
        } else if (info.getType() == 2) {
            imgLeft.setImageResource(R.drawable.ic_history);
            orderDate.setText(info.getBooktime());
            orderType.setImageResource(R.drawable.ic_order);
        }
        setOutLocation.setText(context.getString(R.string.set_out_location, info.getStart()));
        EsLocation.setText(context.getString(R.string.get_off_location, info.getEnd()));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public OrderDialog setOrderDistance(String s) {
        tvOrderDistance.setText(context.getString(R.string.dialog_order_distance, s));
        return this;
    }


    public OrderDialog setReceiveClickListener(final View.OnClickListener listener) {
        receiveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });

        return this;
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    // 定时器，倒计时
    @SuppressWarnings("deprecation")
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            countTimer.setVisibility(View.GONE);
            tvReceive.setVisibility(View.GONE);
            centerTv.setVisibility(View.VISIBLE);

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            countTimer.setText(millisUntilFinished / 1000 + "");
        }
    }
}
