package com.doudou.driver.ui.profile.opinion;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RatingBar;

import com.alibaba.fastjson.JSON;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.ToastUtil;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpinionListActivity extends BaseActivity implements HttpListener {

    @BindView(R.id.mRatingBar)
    RatingBar mRatingBar;
    @BindView(R.id.thoughtRv)
    RecyclerView thoughtRv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    UserDataPreference userDataPreference;
    int page = 1;

    ThoughtGridAdapter gridAdapter;
    ThoughtPassengerAdapter passengerAdapter;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.activity_opinion_list)
    CoordinatorLayout activityOpinionList;
    private List<ThoughtDataList> mList = new ArrayList<>();


    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_opinion_list);
        ButterKnife.bind(this);
        userDataPreference = new UserDataPreference(this);
        setTitle("我的评价");
        getThought(page);
        setRefreshLayout();
    }

    private void setRefreshLayout() {

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getThought(page);
                    }
                }, 2000);
            }
        });
    }


    private void getThought(int page) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_THOUGHT)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("page", page);
        request(0, baseRequest, this, false, true);
    }

    @Override
    public void onSucceed(int what, String response) {
        try {
            ThoughtBean thoughtBean = JSON.parseObject(response, ThoughtBean.class);
            if (page > 1) {
                if (thoughtBean.getData() != null && thoughtBean.getData().size() > 0) {
                    mList.addAll(thoughtBean.getData());
                } else {
                    ToastUtil.showToast(this, "没有更多数据");
                }
                refreshLayout.finishLoadmore();
            } else {
                mRatingBar.setRating(thoughtBean.getScore());
                mList.addAll(thoughtBean.getData());
                setGridAdapter(thoughtBean);
            }
            setContentAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        if (page > 1) {
            refreshLayout.finishLoadmore();
            if (code == 205) {
                ToastUtil.showToast(this, "没有更多数据");
            }
        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    private void setGridAdapter(ThoughtBean thoughtBean) {
        gridAdapter = new ThoughtGridAdapter(thoughtBean.getThought());
        thoughtRv.setLayoutManager(new GridLayoutManager(this, 3));
        thoughtRv.setItemAnimator(new DefaultItemAnimator());
        thoughtRv.setAdapter(gridAdapter);
    }

    private void setContentAdapter() {
        passengerAdapter = new ThoughtPassengerAdapter(mList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(passengerAdapter);

    }

}
