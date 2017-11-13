package com.doudou.driver.ui.profile.record;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.bumptech.glide.Glide;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.ui.main.task.DrivingRouteOverLay;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.view.RoundAngleImageView;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripDetailsActivity extends BaseActivity implements HttpListener,RouteSearch.OnRouteSearchListener{



    @BindView(R.id.profile_image)
    RoundAngleImageView profileImage;
    @BindView(R.id.diverName)
    TextView diverName;
    @BindView(R.id.diverMoblie)
    TextView diverMoblie;
    @BindView(R.id.setOutLocation)
    TextView setOutLocation;
    @BindView(R.id.EsLocation)
    TextView EsLocation;
    @BindView(R.id.tvOrderClose)
    TextView tvOrderClose;
    @BindView(R.id.tvOrderMoney)
    TextView tvOrderMoney;
    @BindView(R.id.tvCheckDetails)
    TextView tvCheckDetails;

    UserDataPreference userDataPreference;
    OrderRecordBean recordBean;
    @BindView(R.id.map)
    MapView mapView;
    private AMap aMap;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;

    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;

    //地图对象
    private Marker mStartMarker;
    private Marker mEndMarker;
    private int orderId;
    int state;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_trip_details);
        ButterKnife.bind(this);
        setTitle("行程结束");

        userDataPreference = new UserDataPreference(this);

        init(savedInstanceState);

        if (getIntent().getExtras() != null) {
            orderId = getIntent().getExtras().getInt("orderId");
            getOrderRecord(orderId);
        }
    }


    /**
     * 初始化AMap对象
     */
    private void init(Bundle savedInstanceState) {
        mContext = this.getApplicationContext();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);

        // 初始化Marker添加到地图
        mStartMarker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.ic_start_marker))));
        mEndMarker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.ic_end_marker))));
    }

    @OnClick({R.id.imgCallDiver, R.id.tvCheckDetails})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCallDiver:
                showMessageDialog();
                break;
            case R.id.tvCheckDetails:
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra("orderId", orderId);
                startActivity(intent);
                break;
        }
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
                if (ActivityCompat.checkSelfPermission(TripDetailsActivity.this,
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
    /**
     * * @param int orderid 订单id
     *  userid 用户id / 司机id
     */
    private void getOrderRecord(int id) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_TRIP_RECORD_DETAILS)
                .add("orderid", id)
                .add("usertype", 2)
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
        setOutLocation.setText(data.getAddresses());
        EsLocation.setText(data.getDown());
        tvOrderMoney.setText(getString(R.string.record_order_money, String.valueOf(data.getPrice())));

        mStartPoint = new LatLonPoint(data.getLatitude(),data.getLongitude());
        mEndPoint =  new LatLonPoint(data.getLatitudel(),data.getLongitudel());
        mStartMarker.setPosition(new LatLng(mStartPoint.getLatitude(),mStartPoint.getLongitude()));
        mEndMarker.setPosition(new LatLng(mEndPoint.getLatitude(),mEndPoint.getLongitude()));
        state = data.getStatus();
        if (data.getStatus()==7){
            tvOrderMoney.setVisibility(View.GONE);
            tvCheckDetails.setVisibility(View.GONE);
            tvOrderClose.setVisibility(View.VISIBLE);
        }else{
            tvOrderMoney.setVisibility(View.VISIBLE);
            tvCheckDetails.setVisibility(View.VISIBLE);
            tvOrderClose.setVisibility(View.GONE);
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null,
                null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int i) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);

                    DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);

                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    if (state==7){
                        drivingRouteOverlay.addStartAndEndMarker();
                    }else{
                        drivingRouteOverlay.addToMap();
                    }
                    drivingRouteOverlay.zoomToSpan();
                }
            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
