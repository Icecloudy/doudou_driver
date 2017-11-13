package com.doudou.driver.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doudou.driver.R;
import com.doudou.driver.data.models.OrderInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/4.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {

    private List<OrderInfo> mList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void clickListener(View view, int i);
    }

    public OrderListAdapter(List<OrderInfo> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unfinish_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.setData(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.orderType)
        TextView orderType;
        @BindView(R.id.orderName)
        TextView orderName;
        @BindView(R.id.setOutLocation)
        TextView setOutLocation;
        @BindView(R.id.EsLocation)
        TextView EsLocation;
        @BindView(R.id.bookTime)
        TextView bookTime;
        int position;

        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        // status(int) 订单状态[1未接,2已接,3载客中,4已到达,5已结单,6已评价,7取消]
        private void setData(OrderInfo data, int position) {
            this.position = position;
            if (data.getType()==1){
                orderType.setBackgroundResource(R.drawable.ic_type_time);
                if (data.getStatus()==2){
                    bookTime.setText("去接乘客");
                }else if (data.getStatus()==3){
                    bookTime.setText("行程中");
                }else if (data.getStatus()==8){
                    bookTime.setText("等待乘客上车");
                }
            }else {
                orderType.setBackgroundResource(R.drawable.shape_order_type);
                if (data.getType()==2){
                    orderType.setText("预约");
                }else if (data.getType()==3){
                    orderType.setText("接机");
                }else if (data.getType()==4){
                    orderType.setText("送机");
                }else if (data.getType()==5){
                    orderType.setText("代叫");
                }
                String time = data.getBooktime();
                if (!TextUtils.isEmpty(time)){
                    time = time.substring(5,time.length()-3);
                    time = "请于"+time+"前到达上车点";
                    bookTime.setText(time);
                    orderName.setVisibility(View.GONE);
                }
               if (data.getStatus()==3){
                    bookTime.setText("行程中");
                }else if (data.getStatus()==8){
                    bookTime.setText("等待乘客上车");
                }
            }
            setOutLocation.setText(data.getAddresses());
            EsLocation.setText(data.getDown());
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.clickListener(v, position);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
