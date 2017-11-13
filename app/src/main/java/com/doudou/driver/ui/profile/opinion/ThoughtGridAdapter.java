package com.doudou.driver.ui.profile.opinion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doudou.driver.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ThoughtGridAdapter extends RecyclerView.Adapter<ThoughtGridAdapter.GridViewHolder> {

    private List<ThoughtList> mList;

    public ThoughtGridAdapter(List<ThoughtList> mList) {
        this.mList = mList;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_thought, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        holder.setData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.size)
        TextView size;
        public GridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        private void setData(ThoughtList data){
            content.setText(data.getContent());
            size.setText(String.valueOf(data.getSize()));

        }
    }
}
