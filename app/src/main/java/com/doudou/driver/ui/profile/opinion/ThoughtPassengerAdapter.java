package com.doudou.driver.ui.profile.opinion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doudou.driver.R;
import com.doudou.driver.view.RoundAngleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ThoughtPassengerAdapter extends RecyclerView.Adapter<ThoughtPassengerAdapter.PsgerViewHolder> {


    private List<ThoughtDataList> mList;
    private Context context;

    public ThoughtPassengerAdapter(List<ThoughtDataList> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }


    @Override
    public PsgerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passenger_thought, parent, false);
        return new PsgerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PsgerViewHolder holder, int position) {
        holder.setData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class PsgerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_image)
        RoundAngleImageView profileImage;
        @BindView(R.id.mRatingBar)
        RatingBar mRatingBar;
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.userOpinion)
        TextView userOpinion;
        @BindView(R.id.time)
        TextView time;

        public PsgerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(ThoughtDataList data) {
            Glide.with(context)
                    .load(data.getPhoto())
                    .placeholder(R.drawable.ic_head)
                    .error(R.drawable.ic_head)
                    .crossFade()
                    .centerCrop()
                    .into(profileImage);
            mRatingBar.setRating(data.getScore());
            userName.setText(data.getNickname());
            String content;
            if (TextUtils.isEmpty(data.getContent())) {
                content = data.getThought();
            } else {
                content = data.getContent();
            }
            userOpinion.setText(content);
            if (!TextUtils.isEmpty(data.getCreatetime())){
                if (data.getCreatetime().length()>10){
                    time.setText(data.getCreatetime().substring(0,10));
                }else{
                    time.setText(data.getCreatetime());
                }
            }

        }
    }
}
