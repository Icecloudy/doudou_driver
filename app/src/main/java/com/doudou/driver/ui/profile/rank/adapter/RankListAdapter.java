package com.doudou.driver.ui.profile.rank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doudou.driver.R;
import com.doudou.driver.view.RoundAngleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/13.
 */

public class RankListAdapter extends RecyclerView.Adapter<RankListAdapter.BillViewHolder> {



    private List<RankBean> mList;
    private Context context;

    public RankListAdapter(List<RankBean> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);
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
        @BindView(R.id.rank)
        TextView rank;
        @BindView(R.id.profile_image)
        RoundAngleImageView profileImage;
        @BindView(R.id.diverName)
        TextView diverName;
        @BindView(R.id.billCount)
        TextView billCount;

        public BillViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(RankBean data, int position) {
            if (position==0){
                rank.setBackgroundResource(R.drawable.ic_rank_one);
            }else  if (position==1){
                rank.setBackgroundResource(R.drawable.ic_rank_two);
            }else  if (position==2){
                rank.setBackgroundResource(R.drawable.ic_rank_three);
            }else{
                rank.setText(String.valueOf(position+1));
            }
            Glide.with(context)
                    .load(data.getPhoto())
                    .placeholder(R.drawable.ic_head)
                    .error(R.drawable.ic_head)
                    .crossFade()
                    .centerCrop()
                    .into(profileImage);
            diverName.setText(data.getUsername());
            billCount.setText(data.getOrders()+"单");

        }


    }
}
