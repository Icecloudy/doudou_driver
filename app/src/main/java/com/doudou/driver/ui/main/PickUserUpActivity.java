package com.doudou.driver.ui.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.bumptech.glide.Glide;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.data.models.OrderCost;
import com.doudou.driver.data.models.OrderInfo;
import com.doudou.driver.data.models.OrderRule;
import com.doudou.driver.data.models.PassengerUser;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.ui.login.LoginActivity;
import com.doudou.driver.ui.main.task.LocationTask;
import com.doudou.driver.ui.main.task.OnLocationGetListener;
import com.doudou.driver.ui.main.task.PositionEntity;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.StringUtil;
import com.doudou.driver.utils.ToastUtil;
import com.doudou.driver.view.RoundAngleImageView;
import com.orhanobut.logger.Logger;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickUserUpActivity extends BaseActivity implements
        AMapNaviListener, HttpListener, AMap.OnMapLoadedListener, OnLocationGetListener {


    @BindView(R.id.imgNavigation)
    ImageView imgNavigation;
    @BindView(R.id.profile_image)
    RoundAngleImageView profileImage;
    @BindView(R.id.diverName)
    TextView diverName;
    @BindView(R.id.currCost)
    TextView currCost;
    @BindView(R.id.imgCallDiver)
    ImageView imgCallDiver;
    @BindView(R.id.diverMoblie)
    TextView diverMoblie;
    @BindView(R.id.setOutLocation)
    TextView setOutLocation;
    @BindView(R.id.EsLocation)
    TextView EsLocation;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvDistanceTips)
    TextView tvDistanceTips;
    @BindView(R.id.tvReady)
    TextView tvReady;
    @BindView(R.id.map)
    MapView mMapView;
    private AMapNavi mAMapNavi;
    private AMap mAmap;
    //地图对象
    private Marker mStartMarker;
    private Marker mEndMarker;
    private NaviLatLng startLatlng = new NaviLatLng();
    private NaviLatLng endLatlng = new NaviLatLng();
    private List<NaviLatLng> currList = new ArrayList<>();
    private List<NaviLatLng> startList = new ArrayList<>();
    //途径点坐标集合
    private List<NaviLatLng> wayList = new ArrayList<>();
    //终点坐标集合［建议就一个终点］
    private List<NaviLatLng> endList = new ArrayList<>();
    //保存当前算好的路线
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<>();

    OrderInfo orderInfo = new OrderInfo();
    double orderCost;
    UserDataPreference userDataPreference;
    private LocationTask mLocationTask;
    private String mCity;
    private double distance;
    private double distancePre;
    private double star;//起步公里数
    private double base;//里程费
    private double duratio;//时长费
    private double remote;//远途费
    private List<OrderRule.Remote> remotes = new ArrayList<>();
    int state;
    int orderId;
    public static boolean isForeground = false;
    OrderRule orderRule;
    String passengerMobile;
    private boolean calculateSuccess = false;
    boolean isUpdateLocation = true;

    private LatLng newEntity;
    private LatLng oldEntity;
    private boolean isFirst = true;
    private int pickUser = 0;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_pick_user_up);
        ButterKnife.bind(this);
        setTitle("待接乘客");
        setRightTextColor(R.color.colorPrimary);
        userDataPreference = new UserDataPreference(this);
        mLocationTask = LocationTask.getInstance(getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mLocationTask.startLocate();
        initView(savedInstanceState);

        setRightText(R.string.cancel_order, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickUserUpActivity.this, CancelOrderActivity.class);
                intent.putExtra("id", orderId);
                startActivity(intent);
            }
        });
        if (getIntent().getExtras() != null) {
            orderId = getIntent().getExtras().getInt("orderId");
            getOrderRule(orderId);
            getOrderDetail(orderId);
        }
        registerMessageReceiver();
        if (!TextUtils.isEmpty(userDataPreference.getDistance(orderId))) {
            distance = Double.parseDouble(userDataPreference.getDistance(orderId));
        }
    }

    private void initView(Bundle savedInstanceState) {


        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        mAmap.getUiSettings().setZoomControlsEnabled(false);
        mAmap.setOnMapLoadedListener(this);

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAmap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mAmap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        // 初始化Marker添加到地图
        mStartMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.ic_start_marker))));
        mEndMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.ic_end_marker))));

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);

    }


    //status(int): 订单状态[1未接,2已接,3载客中,4已到达,5已结单,6已评价,7取消,8司机已就位],
    private void setOrderState(OrderInfo info, int state) {
        PassengerUser user = info.getUser();
        Glide.with(this)
                .load(user.getPhoto())
                .placeholder(R.drawable.ic_head)
                .error(R.drawable.ic_head)
                .crossFade()
                .centerCrop()
                .into(profileImage);
        diverName.setText(TextUtils.isEmpty(user.getNickname()) ? "乘客" : user.getNickname());
        diverMoblie.setText(user.getPhone());
        passengerMobile = user.getPhone();
        startLatlng.setLatitude(info.getLatitude());
        startLatlng.setLongitude(info.getLongitude());
        endLatlng.setLatitude(info.getLatitudel());
        endLatlng.setLongitude(info.getLongitudel());
        setOutLocation.setText(getString(R.string.set_out_location, info.getAddresses()));
        EsLocation.setText(getString(R.string.get_off_location, info.getDown()));

        mStartMarker.setPositionByPixels(mMapView.getWidth() / 2,
                mMapView.getHeight() / 5);
        CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(startLatlng.getLatitude(), startLatlng.getLongitude()), 15);
        mAmap.animateCamera(cameraUpate);

        //描起点
        mStartMarker.setPosition(new LatLng(startLatlng.getLatitude(), startLatlng.getLongitude()));
        startList.clear();
        startList.add(startLatlng);
        //描终点
        mEndMarker.setPosition(new LatLng(endLatlng.getLatitude(), endLatlng.getLongitude()));
        endList.clear();
        endList.add(endLatlng);
        mLocationTask.startLocate();//定位
        if (state == 2) {
            isUpdateLocation = true;
            if (info.getType() == 1) {
                tvReady.setText(R.string.ready_state_a);
            } else {
                if (userDataPreference.getSetOffNotify(orderInfo.getId())) {
                    tvReady.setText(R.string.ready_state_a);
                } else {
                    tvReady.setText(R.string.ready_state_d);
                }
            }
        } else if (state == 8 || state == 3) {
            if (state == 3) {
                isUpdateLocation = false;
                tvReady.setText(R.string.ready_state_c);
                setTitle("服务中");
                setRightTextGone();
            } else {
                isUpdateLocation = true;
                tvReady.setText(R.string.ready_state_b);
                if (!userDataPreference.getNotify(orderInfo.getId())) {
                    driverReady(orderInfo.getId());
                    userDataPreference.setNotify(orderInfo.getId(), true);
                }
            }
            //路径规划
            clearRoute();
            int strategyFlag = 0;
            try {
                //再次强调，最后一个参数为true时代表多路径，否则代表单路径
                strategyFlag = mAMapNavi.strategyConvert(false, false, false, false, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strategyFlag >= 0) {
                mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
            }
            if (calculateSuccess) {
                mAMapNavi.selectRouteId(routeOverlays.keyAt(0));
            }

        }
    }


    @OnClick({R.id.imgNavigation, R.id.imgCallDiver, R.id.imgCallDiverVoice, R.id.tvReady})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgNavigation:
                if (calculateSuccess) {
                    Intent gpsintent = new Intent(getApplicationContext(), RouteNaviActivity.class);
                    gpsintent.putExtra("gps", true);
                    startActivity(gpsintent);
                } else {
                    showMsg("路径规划中，请稍后");
                }
                break;
            case R.id.imgCallDiver:
                showMessageDialog();
                break;
            case R.id.imgCallDiverVoice:
                break;
            case R.id.tvReady:
                if (tvReady.getText().toString().equals(getString(R.string.ready_state_d))) {
                    //去接乘客
                    userDataPreference.setWorkModel(2);
                    isUpdateLocation = true;
                    setSetOff(orderId);
                    userDataPreference.setSetOffNotify(orderInfo.getId(), true);
                } else if (tvReady.getText().toString().equals(getString(R.string.ready_state_a))) {
                    userDataPreference.setWorkModel(2);
                    //到达上车地点  告诉乘客不再获取位置
                    isUpdateLocation = true;
                    tvReady.setText(R.string.ready_state_b);
                    setTitle("等待乘客上车");
                    //路径规划
                    clearRoute();
                    int strategyFlag = 0;
                    try {
                        strategyFlag = mAMapNavi.strategyConvert(false, false, false, false, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (strategyFlag >= 0) {
                        mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
                    }
                    if (calculateSuccess) {
                        mAMapNavi.selectRouteId(routeOverlays.keyAt(0));
                    }
                    if (!userDataPreference.getNotify(orderInfo.getId())) {
                        driverReady(orderInfo.getId());
                        userDataPreference.setNotify(orderInfo.getId(), true);
                    }

                } else if (tvReady.getText().toString().equals(getString(R.string.ready_state_b))) {
                    //开始收费
                    isUpdateLocation = false;
                    startCharging(orderInfo.getId());
                    clearRoute();
                    int strategyFlag = 0;
                    try {
                        strategyFlag = mAMapNavi.strategyConvert(false, false, false, false, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (strategyFlag >= 0) {
                        mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
                    }
                    if (calculateSuccess) {
                        mAMapNavi.selectRouteId(routeOverlays.keyAt(0));
                    }
                } else {
                    isUpdateLocation = false;
                    //生成订单
                    if (orderCost > 0) {
                        Intent intent = new Intent(this, ConfirmOrderActivity.class);
                        intent.putExtra("cost", orderCost);
                        intent.putExtra("distance", distance);
                        intent.putExtra("orderInfo", JSON.toJSONString(orderInfo));
                        startActivity(intent);
                    } else {
                        ToastUtil.showToast(PickUserUpActivity.this, "正在生成订单，请稍候...");
                    }
                }
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
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + passengerMobile));
                if (ActivityCompat.checkSelfPermission(PickUserUpActivity.this,
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
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onLocationGet(PositionEntity entity) {
        Log.e("PickUserUpActivity", "onLocationGet：" + entity.address);
        mCity = entity.city;
        newEntity = new LatLng(entity.latitue, entity.longitude);
        if (isFirst) {
            isFirst = false;
            oldEntity = newEntity;
        }
        if (isUpdateLocation) {
            currCost.setVisibility(View.GONE);
            imgCallDiver.setVisibility(View.VISIBLE);
            tvDistanceTips.setText("距离");
            updateLocation(entity.city, entity.latitue, entity.longitude, 2, orderInfo.getId(), 0);
            //路径规划
            if (pickUser == 0) {
                currList.clear();
                currList.add(new NaviLatLng(entity.latitue, entity.longitude));
                clearRoute();
                int strategyFlag = 0;
                try {
                    strategyFlag = mAMapNavi.strategyConvert(false, false, false, false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (strategyFlag >= 0) {
                    mAMapNavi.calculateDriveRoute(currList, startList, wayList, strategyFlag);
                }
                if (calculateSuccess) {
                    mAMapNavi.selectRouteId(routeOverlays.keyAt(0));
                    pickUser++;
                    showMsg("本次导航为前往乘客上车地点");
                }
            }
        } else {
            tvDistanceTips.setText("已行");
            imgCallDiver.setVisibility(View.GONE);
            if (oldEntity != newEntity) {
                //计算两点的距离，判断总距离是否大于起步公里
                distancePre = AMapUtils.calculateLineDistance(oldEntity, newEntity) / 1000;
                distance += distancePre;//单位 公里
                userDataPreference.setDistance(orderId, String.valueOf(distance));
                Log.e("PickUserUpActivity", "distance：" + distance);
                String longitude = newEntity.longitude+"";
                String latitude = newEntity.latitude+"";
                getOrderCharge(orderId, distance, longitude, latitude);

                /*double qbCost = star * base + duratio;
                tvDistance.setText(StringUtil.doubleFormat(distance / 1000));
                if (StringUtil.doubleFormatD(distance / 1000) < star) {
                    orderCost = StringUtil.doubleFormatD(qbCost + orderRule.getOil() + orderRule.getAdditional());
                } else {
                    if ((int) (distance / 1000) / 10 > 5) {
                        remote = remotes.get(remotes.size() - 1).getExtraCost();
                    } else {
                        remote = remotes.get((int) distance / 10).getExtraCost();
                    }
                    int time = (int) StringUtil.compareDate(StringUtil.getHttpRequestTimestamp(), userDataPreference.getOrderStartTime());
                    orderCost = StringUtil.doubleFormatD(((distance / 1000) - star) * base + qbCost + remote + orderRule.getOil()
                            + orderRule.getAdditional() + time * duratio);
                }
                currCost.setVisibility(View.VISIBLE);
                currCost.setText(getString(R.string.curr_cost, String.valueOf(orderCost)));*/
                oldEntity = newEntity;
            }
        }
    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
//        Intent intent = new Intent();
//        intent.setAction(ConfigUtil.FILTER_CODE);
//        intent.putExtra(ConfigUtil.EXTRA_CODE, ConfigUtil.UPDATE_UNFINISH_ORDER);
//        sendBroadcast(intent);

        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        mMapView.onDestroy();
        mLocationTask.onDestroy();

        startList.clear();
        wayList.clear();
        endList.clear();
        routeOverlays.clear();
        /**
         * 当前页面只是展示地图，activity销毁后不需要再回调导航的状态
         */
        mAMapNavi.removeAMapNaviListener(this);
        mAMapNavi.destroy();


    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        mAmap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(mAmap, path, this);
        routeOverLay.setTrafficLine(false);
        routeOverLay.addToMap();
        routeOverLay.zoomToSpan();
        routeOverlays.put(routeId, routeOverLay);
    }


    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        routeOverlays.clear();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
    }

    @Override
    public void onCalculateRouteSuccess() {
        /**
         * 清空上次计算的路径列表。
         */
        routeOverlays.clear();
        AMapNaviPath path = mAMapNavi.getNaviPath();
        /**
         * 单路径不需要进行路径选择，直接传入－1即可
         */
        drawRoutes(-1, path);
    }

    private static final int DRIVER_READY = 0x001;
    private static final int GET_ORDER_RULE = 0x002;
    private static final int REPORT_LOCATION = 0x003;
    private static final int START_CHARGING = 0x004;
    private static final int GET_ORDER_DETAIL = 0x005;
    private static final int SET_OFF = 0x006;
    private static final int GET_ORDER_CHARGE = 0x007;

    //------------------------开始收费start------------------------------
    private void startCharging(int orderid) {
        BaseRequest request = new BaseRequest(ConfigUtil.START_CHARGING)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("orderid", orderid);
        request(START_CHARGING, request, this, false, true);
    }

    private void setSetOff(int orderid) {
        BaseRequest request = new BaseRequest(ConfigUtil.SET_OFF)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("orderid", orderid);
        request(SET_OFF, request, this, false, true);
    }

    //上报司机位置
    private void updateLocation(String city, double lat, double lng, int status, int orderid, int price) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.REPORT_LOCATION)
                .add("id", userDataPreference.getId())
                .add("token", userDataPreference.getToken())
                .add("city", city)
                .add("dlat", lat)
                .add("dlng", lng)
                .add("status", status)//[0上班,1下班(默认),2载客中]
                .add("orderid", orderid)//状态为“载客中”时所需]
                .add("price", price);//当前订单价钱[状态为“载客中”时所需]
        request(REPORT_LOCATION, baseRequest, this, false, false);
    }

    private void driverReady(int orderid) {
        BaseRequest request = new BaseRequest(ConfigUtil.DRIVER_READY)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("orderid", orderid);
        request(DRIVER_READY, request, this, false, true);
    }

    private void getOrderDetail(int orderid) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_ORDER_DETAIL)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("orderid", orderid);
        request(GET_ORDER_DETAIL, baseRequest, this, false, true);
    }

    private void getOrderRule(int orderid) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_RULE)
                .add("orderid", orderid);
        request(GET_ORDER_RULE, baseRequest, this, false, false);
    }

    private void getOrderCharge(int orderid, double distance, String longitude, String latitude) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_ORDER_CHARGE)
                .add("orderid", orderid)
                .add("distance", distance)
                .add("longitude", longitude)
                .add("latitude", latitude)
                .add("token", userDataPreference.getToken());

        request(GET_ORDER_CHARGE, baseRequest, this, false, false);
    }

    /**
     * HTTP回调
     *
     * @param what
     * @param response status(int): 订单状态[1未接,2已接,3载客中,4已到达,5已结单,6已评价,7取消,8司机已就位],
     */
    @Override
    public void onSucceed(int what, String response) {
        if (what == GET_ORDER_DETAIL) {
            try {
                orderInfo = JSON.parseObject(response, OrderInfo.class);
                state = orderInfo.getStatus();
                if (state == 7) {
                    OrderhasCancel();
                } else {
                    setOrderState(orderInfo, state);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (what == START_CHARGING) {
            userDataPreference.setOrderStartTime();
            tvReady.setText(R.string.ready_state_c);
            setTitle("服务中");
            setRightTextGone();
        } else if (what == SET_OFF) {
            tvReady.setText(R.string.ready_state_a);
        } else if (what == GET_ORDER_RULE) {
            Log.e("PickUserUpActivity", "GET_ORDER_RULE response：" + response);
            orderRule = JSON.parseObject(response, OrderRule.class);
            star = orderRule.getBegan();
            base = orderRule.getMileage();
            duratio = orderRule.getDuration();
            remotes = orderRule.getRemote();
        } else if (what == GET_ORDER_CHARGE) {
            Log.e("PickUserUpActivity", "GET_ORDER_CHARGE response：" + response);

            OrderCost cost = JSON.parseObject(response, OrderCost.class);
            tvDistance.setText(StringUtil.doubleFormat(distance));
            currCost.setVisibility(View.VISIBLE);
            currCost.setText(getString(R.string.curr_cost, String.valueOf(cost.getTotal_charge())));
            orderCost = cost.getTotal_charge();
        }
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        showMsg(msg);
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.doudou.driver.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction(ConfigUtil.FILTER_CODE);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String extras = intent.getStringExtra(KEY_EXTRAS);
                setCostomMsg(extras);
            }
        }
    }

    private void setCostomMsg(String extraInfo) {
        Logger.d("====================\n" + extraInfo);
        orderInfo = JSON.parseObject(extraInfo, OrderInfo.class);
        if (orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_cancelOrder)) {
            OrderhasCancel();
        } else if (orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_LOGOUT)) {
            userDataPreference.removeData(UserDataPreference.TOKEN);
            startActivity(new Intent(this, LoginActivity.class));
            SysApplication.getInstance().exit();
        }
    }

    private void OrderhasCancel() {
        showMsg("乘客已取消订单");
        Intent intent = new Intent();
        intent.setAction(ConfigUtil.FILTER_CODE);
        intent.putExtra(ConfigUtil.EXTRA_CODE, ConfigUtil.UPDATE_UNFINISH_ORDER);
        sendBroadcast(intent);
        finish();
    }


    /**
     * ************************************************** 在算路页面，以下接口全不需要处理，在以后的版本中我们会进行优化***********************************************************************************************
     **/

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        calculateSuccess = false;
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }


    /**
     * ************************************************** 在算路页面，以下接口全不需要处理，在以后的版本中我们会进行优化***********************************************************************************************
     **/

}
