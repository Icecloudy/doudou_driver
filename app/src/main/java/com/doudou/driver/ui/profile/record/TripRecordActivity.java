package com.doudou.driver.ui.profile.record;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.ui.profile.record.adapter.TripRecordAdapter;
import com.doudou.driver.ui.profile.record.adapter.TripRecordBean;
import com.doudou.driver.utils.ConfigUtil;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripRecordActivity extends BaseActivity implements HttpListener {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    private List<TripRecordBean> mList = new ArrayList<>();
    UserDataPreference userDataPreference;
    TripRecordAdapter tripRecordAdapter;

    int page =1;


    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_trip_record);
        setTitle(R.string.nav_record);
        ButterKnife.bind(this);
        userDataPreference = new UserDataPreference(this);
        getOrderRecord(page);

        setRefreshLayout();
    }

    private void setRefreshLayout(){
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        getOrderRecord(page);

                    }
                },2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page ++;
                        getOrderRecord(page);

                    }
                },2000);
            }
        });
    }
    private void getOrderRecord(int page) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_TRIP_RECORD)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("usertype", 2)
                .add("page", page);
        request(0, baseRequest, this, false, true);

    }

    @Override
    public void onSucceed(int what, String response) {
        try {
            List<TripRecordBean> beanList = JSON.parseArray(response, TripRecordBean.class);
            if (page>1){
                mList.addAll(beanList);
                refreshLayout.finishLoadmore();
            }else{
                if (mList!=null){
                    mList.clear();
                }
                mList.addAll(beanList);
                refreshLayout.finishRefreshing();
            }
            setTripRecordAdapter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        if (page>1){
            refreshLayout.finishLoadmore();
            if (code==205){
                showMsg("没有更多数据");
            }
        }else{
            refreshLayout.finishRefreshing();
            emptyView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onFailed(int what, Response<String> response) {
        if (page>1){
            refreshLayout.finishLoadmore();
        }else{
            refreshLayout.finishRefreshing();
        }
    }

    private void setTripRecordAdapter() {
        tripRecordAdapter = new TripRecordAdapter(mList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(tripRecordAdapter);
    }

}
