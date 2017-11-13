package com.doudou.driver.ui.profile.record.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doudou.driver.R;
import com.doudou.driver.ui.profile.record.TripDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/13.
 */

public class TripRecordAdapter extends RecyclerView.Adapter<TripRecordAdapter.BillViewHolder> {


    private List<TripRecordBean> mList;
    private Context context;

    public TripRecordAdapter(List<TripRecordBean> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_record, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        holder.setData(mList.get(position), position);
    }


    class BillViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.orderDate)
        TextView orderDate;
        @BindView(R.id.tvIsDone)
        TextView tvIsDone;
        @BindView(R.id.setOutLocation)
        TextView setOutLocation;
        @BindView(R.id.EsLocation)
        TextView EsLocation;
        @BindView(R.id.layoutTrip)
        LinearLayout layoutTrip;


        public BillViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(TripRecordBean data, int position) {
            layoutTrip.setTag(position);
            orderDate.setText(data.getPlacetime());
            setOutLocation.setText(data.getAddresses());
            EsLocation.setText(data.getDown());
            if (data.getStatus() == 7) {//1取消 2完成
                tvIsDone.setText("已关闭");
            } else if (data.getStatus() == 5 || data.getStatus() == 6) {//1取消 2完成
                tvIsDone.setText("已完成");
            }

        }

        @OnClick(R.id.layoutTrip)
        public void onViewClicked(View view) {
            int i = (int) view.getTag();
            Intent intent = new Intent(context, TripDetailsActivity.class);
            intent.putExtra("orderId", mList.get(i).getOrderid());
            context.startActivity(intent);
        }


    }
}
