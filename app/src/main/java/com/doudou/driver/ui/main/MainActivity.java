package com.doudou.driver.ui.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.data.UserDataPreference;
import com.doudou.driver.data.models.DriverBean;
import com.doudou.driver.data.models.OrderInfo;
import com.doudou.driver.data.models.RankingBean;
import com.doudou.driver.data.models.UpdateBean;
import com.doudou.driver.data.models.UserBean;
import com.doudou.driver.nohttp.BaseRequest;
import com.doudou.driver.nohttp.CallServer;
import com.doudou.driver.nohttp.HttpListener;
import com.doudou.driver.ui.login.LoginActivity;
import com.doudou.driver.ui.main.adapter.OrderListAdapter;
import com.doudou.driver.ui.main.task.LocationTask;
import com.doudou.driver.ui.main.task.OnLocationGetListener;
import com.doudou.driver.ui.main.task.PositionEntity;
import com.doudou.driver.ui.profile.news.NewsActivity;
import com.doudou.driver.ui.profile.opinion.OpinionListActivity;
import com.doudou.driver.ui.profile.profile.ProfileActivity;
import com.doudou.driver.ui.profile.rank.RankListActivity;
import com.doudou.driver.ui.profile.record.TripRecordActivity;
import com.doudou.driver.ui.profile.settings.SettingsActivity;
import com.doudou.driver.ui.profile.share.ShareActivity;
import com.doudou.driver.ui.profile.wallet.WalletActivity;
import com.doudou.driver.utils.ConfigUtil;
import com.doudou.driver.utils.NetWorkUtil;
import com.doudou.driver.utils.StringUtil;
import com.doudou.driver.utils.ToastUtil;
import com.doudou.driver.utils.secure.Md5;
import com.doudou.driver.view.OrderDialog;
import com.doudou.driver.view.RoundAngleImageView;
import com.orhanobut.logger.Logger;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

import static com.doudou.driver.utils.ConfigUtil.EXTRA_CODE;

public class MainActivity extends AppCompatActivity implements HttpListener,
        AMap.OnMapLoadedListener, OnLocationGetListener {

    @BindView(R.id.imgPersonal)
    ImageView imgPersonal;
    @BindView(R.id.imgNews)
    ImageView imgNews;
    @BindView(R.id.imgProcess)
    ImageView imgProcess;
    @BindView(R.id.imgProcess2)
    ImageView imgProcess2;
    @BindView(R.id.getOffModel)
    TextView getOffModel;
    @BindView(R.id.imgHead)
    RoundAngleImageView imgHead;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.workModel)
    RelativeLayout workModel;

    UserDataPreference userDataPreference;
    private static final int REPORT_LOCATION = 0x001;
    private static final int GET_STATE = 0x002;
    private static final int SET_STATE = 0x003;
    private static final int ACCEPT_ORDER = 0x004;
    private static final int GET_DRIVER_DATA = 0x005;
    private static final int GET_ORDER_LIST = 0x006;
    private static final int GET_APP_UPDATE = 0x007;
    private static final int UPDATE_USER = 0x008;
    private static final int GET_DRIVER_RANK = 0x009;

    @BindView(R.id.layoutDriver1)
    LinearLayout layoutDriver1;
    @BindView(R.id.layoutDriver2)
    LinearLayout layoutDriver2;
    @BindView(R.id.nav_wallet)
    TextView nav_wallet;
    @BindView(R.id.tvRankInfo)
    TextView tvRankInfo;

    @BindView(R.id.profile_image)
    RoundAngleImageView profileImage;
    @BindView(R.id.diverName)
    TextView diverName;
    @BindView(R.id.tvDriverType)
    TextView diverType;
    @BindView(R.id.rating_star)
    TextView ratingStar;
    @BindView(R.id.tvBillCount)
    TextView tvBillCount;
    @BindView(R.id.carNum)
    TextView carNum;
    @BindView(R.id.carType)
    TextView carType;
    @BindView(R.id.toDayOrderCount)
    TextView toDayOrderCount;
    @BindView(R.id.serviceScore)
    TextView serviceScore;
    @BindView(R.id.cancelP)
    TextView cancelP;
    @BindView(R.id.tvOrderCount)
    TextView tvOrderCount;
    @BindView(R.id.orderRecyclerView)
    RecyclerView orderRecyclerView;
    @BindView(R.id.layoutOrderList)
    LinearLayout layoutOrderList;

    @BindView(R.id.layoutEmpty)
    RelativeLayout layoutEmpty;
    @BindView(R.id.btnReload)
    Button btnReload;
    private Animation operatingAnim;
    private boolean isBack; // 是否连续点击返回键
    boolean typeModel;//上下班模式 false 下班   true上班

    MapView mMapView;
    private AMap mAmap;

    private LocationTask mLocationTask;
    String cityName;

    public static boolean isForeground = false;
    OrderInfo orderInfo = new OrderInfo();

    private List<OrderInfo> mList = new ArrayList<>();

    OrderListAdapter listAdapter;
    int orderId;
    private int status;
    int orderType;//订单类型


    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";


    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    String hotLine;
    AnimationDrawable animationDrawable;
    double distance = 500;
    boolean isGoToPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SysApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userDataPreference = new UserDataPreference(this);
        String userInfo = userDataPreference.getUserInfo();
        UserBean userBean = JSON.parseObject(userInfo, UserBean.class);
        setNavData(userBean);

        mLocationTask = LocationTask.getInstance(getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        initView(savedInstanceState);

        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

        registerMessageReceiver();
        checkPermission();

        if (NetWorkUtil.isNetworkAvailable(this)){
            getWorkModel();
            getDriverRanking();
            getAppUpdata();
            getOrderList();
            btnReload.setVisibility(View.GONE);
        }else{
            btnReload.setVisibility(View.VISIBLE);
        }
        getDriverData();//获取司机信息
        showOrderDialog();
        getUserInfo();
    }

    private void initView(Bundle savedInstanceState) {
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        mMapView = new MapView(this);
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        mAmap.getUiSettings().setZoomControlsEnabled(false);
        mAmap.setOnMapLoadedListener(this);

        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void setNavData(UserBean userBean) {
        hotLine = userBean.getHotline();
        Glide.with(this)
                .load(userBean.getPhoto())
                .placeholder(R.drawable.ic_head)
                .error(R.drawable.ic_head)
                .crossFade()
                .centerCrop()
                .into(imgHead);
        if (!TextUtils.isEmpty(userBean.getName())) {
            nickname.setText(userBean.getName());
        } else {
            nickname.setText(TextUtils.isEmpty(userBean.getUsername()) ? "司机" + userBean.getId() : userBean.getUsername());
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS},
                    1);//自定义的code
        }
    }

    private void showOrderDialog() {
        if (null != getIntent().getExtras()) {
            Bundle bundle = getIntent().getExtras();
            String extraInfo = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                orderInfo = JSON.parseObject(extraInfo, OrderInfo.class);
                orderId = orderInfo.getId();
                orderType = orderInfo.getType();
                if (!TextUtils.isEmpty(userDataPreference.getPosition())) {
                    PositionEntity entity = JSON.parseObject(userDataPreference.getPosition(), PositionEntity.class);
                    distance = AMapUtils.calculateLineDistance(new LatLng(entity.latitue, entity.longitude), new LatLng(orderInfo.getStartlat(), orderInfo.getStartlng()));
                }
                if (orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_neworder)) {
                    if (userDataPreference.getAcceptModel() == 0) {
                        new OrderDialog(this, orderInfo).builder()
                                .setOrderDistance(StringUtil.doubleFormat(distance / 1000))
                                .setReceiveClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        acceptOrder(orderInfo.getId());
                                    }
                                }).show();
                    } else {
                        acceptOrder(orderInfo.getId());
                    }
                } else if (orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_hasorder) ||
                        orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_cancelOrder)) {
                    getOrderList();
                } else if (orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_LOGOUT)) {
                    userDataPreference.removeData(UserDataPreference.TOKEN);
                    startActivity(new Intent(this, LoginActivity.class));
                    SysApplication.getInstance().exit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.imgPersonal, R.id.imgNews, R.id.orderModel, R.id.workModel, R.id.getOffModel,
            R.id.layoutHead, R.id.nav_record, R.id.nav_wallet, R.id.nav_task, R.id.nav_share,
            R.id.nav_massage, R.id.layoutRank, R.id.nav_service, R.id.nav_setting, R.id.nav_thought, R.id.btnReload})
    public void onViewClicked(View view) {
        Intent intent = null;
        int requestCode = 0;
        switch (view.getId()) {
            case R.id.imgPersonal:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.imgNews:
                intent = new Intent(MainActivity.this, NewsActivity.class);
                break;
            case R.id.orderModel:
                intent = new Intent(MainActivity.this, AcceptOrderModelActivity.class);
                requestCode = 1000;
                break;
            case R.id.workModel:
                status = 0;
                setWorkModel(0);
                break;
            case R.id.getOffModel:
                if (userDataPreference.getWorkModel() == 2) {
                    ToastUtil.showToast(this, "请先完成订单再下班");
                } else {
                    status = 1;
                    setWorkModel(1);
                }
                break;
            case R.id.layoutHead:
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                break;
            case R.id.nav_record:
                intent = new Intent(MainActivity.this, TripRecordActivity.class);
                break;
            case R.id.nav_wallet:
                intent = new Intent(MainActivity.this, WalletActivity.class);
                break;
            case R.id.nav_task:
                break;
            case R.id.nav_share:
                intent = new Intent(MainActivity.this, ShareActivity.class);
                break;
            case R.id.nav_massage:
                intent = new Intent(MainActivity.this, NewsActivity.class);
                break;
            case R.id.layoutRank:
                intent = new Intent(MainActivity.this, RankListActivity.class);
                break;
            case R.id.nav_service:
                showMessageDialog();
                break;
            case R.id.nav_setting:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                break;
            case R.id.nav_thought:
                intent = new Intent(MainActivity.this, OpinionListActivity.class);
                break;
            case R.id.btnReload:
                getWorkModel();
                getDriverData();//获取司机信息
                getDriverRanking();
                getAppUpdata();
                break;
        }
        if (intent != null) {
            if (requestCode != 0) {
                startActivityForResult(intent, requestCode);
            } else {
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                getUserInfo();
            }
        }
    }

    private void getUserInfo() {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.LOGIN)
                .add("clientid", JPushInterface.getRegistrationID(this))
                .add("phone", userDataPreference.getAccount())
                .add("password", Md5.getMd5(userDataPreference.getPassword()));

        CallServer.getRequestInstance().add(this, UPDATE_USER, baseRequest, this, false, false);
    }

    /******************
     * 下班
     ****************/
    private void getOffModel() {
        typeModel = false;
        //imgProcess.clearAnimation();
        workModel.setEnabled(true);
        getOffModel.setVisibility(View.INVISIBLE);
        //停止定位
        imgProcess.setVisibility(View.GONE);
        imgProcess2.setVisibility(View.VISIBLE);
        if (animationDrawable != null) {
            animationDrawable.stop();
            mLocationTask.stopLocate();
        }
    }

    /******************
     * 上班模式
     ****************/
    private void workingModel() {
        typeModel = true;
        imgProcess2.setVisibility(View.GONE);
        imgProcess.setVisibility(View.VISIBLE);
        //showFrame();
        workModel.setEnabled(false);
        getOffModel.setVisibility(View.VISIBLE);
        //开始定位
        mLocationTask.startLocate();
        //0上班,1下班(默认),2载客中
    }

    private void showFrame() {
        imgProcess.setImageResource(R.drawable.frame_animation);
        animationDrawable = (AnimationDrawable) imgProcess.getDrawable();
        animationDrawable.start();
    }


    /**
     * Show message dialog.
     */
    public void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.nav_service);
        builder.setMessage("确定拨打400客服电话");
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + hotLine));
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //若当前不在主页，则先返回主页
                // 双击返回桌面，默认返回true，调用finish()
                if (!isBack) {
                    isBack = true;
                    ToastUtil.showToast(this, "再按一次返回键回到桌面");
                    return false;
                }

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
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
        CallServer.getRequestInstance().add(this, REPORT_LOCATION, baseRequest, this, false, false);
    }

    private void setWorkModel(int status) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.SET_DRIVER_STATE)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken())
                .add("status", status);//[0上班,1下班(默认),2载客中]
        CallServer.getRequestInstance().add(this, SET_STATE, baseRequest, this, false, false);
    }

    private void getWorkModel() {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_DRIVER_STATE)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken());
        CallServer.getRequestInstance().add(this, GET_STATE, baseRequest, this, false, false);
    }

    /*

    * @param app_id 安装包类型[1安卓乘客,2安卓司机,3苹果乘客,4苹果司机]
	 * @param version_id 当前版本号 [1.0/1.0.1/10.0.1等都可以]

     */
    private void getAppUpdata() {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.APP_UPDATE)
                .add("app_id", 2)
                .add("version_id", StringUtil.getPackageVersionName(this));
        CallServer.getRequestInstance().add(this, GET_APP_UPDATE, baseRequest, this, false, false);
    }

    private void acceptOrder(int orderid) {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.ACCEPT_ORDER)
                .add("did", userDataPreference.getId())
                .add("token", userDataPreference.getToken())
                .add("orderid", orderid);
        CallServer.getRequestInstance().add(this, ACCEPT_ORDER, baseRequest, this, false, true);
    }

    private void getDriverData() {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_DRIVER_DATA)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken());
        baseRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        CallServer.getRequestInstance().add(this, GET_DRIVER_DATA, baseRequest, this, false, false);
    }

    private void getDriverRanking() {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_DRIVER_RANKING)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken());
        CallServer.getRequestInstance().add(this, GET_DRIVER_RANK, baseRequest, this, false, false);
    }

    private void getOrderList() {
        BaseRequest baseRequest = new BaseRequest(ConfigUtil.GET_ORDER_LIST)
                .add("phone", userDataPreference.getAccount())
                .add("token", userDataPreference.getToken());
        CallServer.getRequestInstance().add(this, GET_ORDER_LIST, baseRequest, this, false, false);
    }


    @Override
    public void onSucceed(int what, String response) {
        if (what == GET_STATE) {
            btnReload.setVisibility(View.GONE);
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject.containsKey("status")) {
                userDataPreference.setWorkModel(jsonObject.getInteger("status"));
                if (jsonObject.getInteger("status") == 0 || jsonObject.getInteger("status") == 2) {
                    workingModel();
                } else {
                    getOffModel();
                }
            }
        } else if (what == GET_DRIVER_DATA) {
            try {
                DriverBean driverBean = JSON.parseObject(response, DriverBean.class);
                setDriverData(driverBean);
                layoutDriver1.setVisibility(View.VISIBLE);
                layoutDriver2.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (what == GET_DRIVER_RANK) {
            try {
                RankingBean rankingBean = JSON.parseObject(response, RankingBean.class);
                tvRankInfo.setText(getString(R.string.nav_rank_list_sub, rankingBean.getRanking(), rankingBean.getOrders()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (what == ACCEPT_ORDER) {
            ToastUtil.showToast(this, "接单成功");
            if (!userDataPreference.getVoice()) {
                playVoice("接单成功");
            }
            getOrderList();
            isGoToPick = true;

        } else if (what == GET_ORDER_LIST) {
            Log.e("444", response);
            try {
                mLocationTask.stopLocate();
                mList = JSON.parseArray(response, OrderInfo.class);
                tvOrderCount.setText(getString(R.string.unfinish_order_count, mList.size()));
                listAdapter = new OrderListAdapter(mList, this);
                orderRecyclerView.setAdapter(listAdapter);
                listAdapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() {
                    @Override
                    public void clickListener(View view, int i) {

                        Intent intent = new Intent(MainActivity.this, PickUserUpActivity.class);
                        intent.putExtra("orderId", mList.get(i).getId());
                        if (distance != 0) {
                            intent.putExtra("distance", distance);
                        }
                        startActivity(intent);
                    }
                });
                setEmptyView(false);
                if (orderType == 1 && isGoToPick) {
                    userDataPreference.setWorkModel(2);
                    isGoToPick = false;
                    Intent intent = new Intent(this, PickUserUpActivity.class);
                    intent.putExtra("orderId", orderId);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (what == SET_STATE) {
            userDataPreference.setWorkModel(status);
            if (status == 0) {
                workingModel();
                ToastUtil.showToast(this, "上班成功");
                if (!userDataPreference.getVoice()) {
                    playVoice("上班了");
                }
            } else if (status == 1) {
                getOffModel();
                ToastUtil.showToast(this, "下班成功");
                if (!userDataPreference.getVoice()) {
                    playVoice("下班成功");
                }

            }
        } else if (what == GET_APP_UPDATE) {
            UpdateBean updateBean = JSON.parseObject(response, UpdateBean.class);
            if (updateBean.getStatus() == 0) {//	status(int) [0不用更新,1可更新,2强制更新]
            } else {
                showUpdateDialog(updateBean);
            }
        } else if (what == UPDATE_USER) {
            userDataPreference.setUserInfo(response);
        }
    }

    @Override
    public void onCodeError(int what, int code, String msg) {
        if (what == REPORT_LOCATION) {
            Log.i(MainActivity.class.getName(), "上报位置失败");
        } else if (what == GET_ORDER_LIST) {
            //setEmpty
            setEmptyView(true);
        } else if (what == GET_STATE) {
            getOffModel();
        } else if (what == GET_DRIVER_DATA) {
            layoutDriver1.setVisibility(View.GONE);
            layoutDriver2.setVisibility(View.GONE);
        } else if (what == UPDATE_USER) {
            if (code == 206) {
                userDataPreference.removeData(UserDataPreference.TOKEN);
                startActivity(new Intent(this, LoginActivity.class));
                SysApplication.getInstance().exit();
            }

            ToastUtil.showToast(this, msg);
        } else {
            ToastUtil.showToast(this, msg);
        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        if (what == GET_STATE) {
            getOffModel();
        } else if (what == GET_DRIVER_DATA) {
            layoutDriver1.setVisibility(View.GONE);
            layoutDriver2.setVisibility(View.GONE);
        }
    }

    private void setEmptyView(boolean isEmpty) {
        if (isEmpty) {
            layoutOrderList.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            layoutOrderList.setVisibility(View.VISIBLE);
        }
    }

    private void setDriverData(DriverBean data) {
        Glide.with(this)
                .load(data.getPhoto())
                .placeholder(R.drawable.ic_head)
                .error(R.drawable.ic_head)
                .crossFade()
                .centerCrop()
                .into(profileImage);
        diverName.setText(TextUtils.isEmpty(data.getUsername()) ? "司机" : data.getUsername());
        ratingStar.setText(String.valueOf(data.getScore()));
        tvBillCount.setText(data.getCountorders() + "单");
        carNum.setText(data.getCarnum());
        carType.setText(data.getBrand());
        toDayOrderCount.setText(data.getOrders() + "");
        cancelP.setText(data.getCanselrate());
        serviceScore.setText(StringUtil.doubleFormat(data.getService()));
        if (data.getType() == 1) {
            nav_wallet.setVisibility(View.VISIBLE);
            diverType.setText("社会车");//车辆分类[1社会车,2自营专车,3高端车]
        } else if (data.getType() == 2) {
            nav_wallet.setVisibility(View.GONE);
            diverType.setText("自营专车");//车辆分类[1社会车,2自营专车,3高端车]
        } else {
            nav_wallet.setVisibility(View.GONE);
            diverType.setText("高端车");
        }
    }


    @Override
    public void onMapLoaded() {
        mLocationTask.startSingleLocate();
    }

    @Override
    public void onLocationGet(PositionEntity entity) {
        userDataPreference.setPosition(JSON.toJSONString(entity));
        if (typeModel) {
            //上报位置
            Log.d("123", "心跳上报:" + entity.address);
            cityName = entity.city;
            updateLocation(entity.city, entity.latitue, entity.longitude, 0, 0, 0);
            //显示订单
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
        isForeground = true;
        super.onResume();
        mMapView.onResume();
        getOrderList();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        mMapView.onPause();
        mLocationTask.stopLocate();
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
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        mMapView.onDestroy();
        mLocationTask.onDestroy();
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
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
            } else if (ConfigUtil.FILTER_CODE.equals(intent.getAction())) {
                String extra = intent.getStringExtra(EXTRA_CODE);
                if (extra.equals(ConfigUtil.UPDATE_USER)) {
                    String userInfo = userDataPreference.getProfile();
                    UserBean userBean = JSON.parseObject(userInfo, UserBean.class);
                    setNavData(userBean);
                    getDriverData();//获取司机信息
                } else if (extra.equals(ConfigUtil.UPDATE_UNFINISH_ORDER)) {
                    getOrderList();
                }
            }
        }
    }

    private void setCostomMsg(String extraInfo) {
        Log.d("MainAty", "msg:" + extraInfo);
        orderInfo = JSON.parseObject(extraInfo, OrderInfo.class);
        orderId = orderInfo.getId();
        orderType = orderInfo.getType();
        if (!TextUtils.isEmpty(userDataPreference.getPosition())) {
            PositionEntity entity = JSON.parseObject(userDataPreference.getPosition(), PositionEntity.class);
            distance = AMapUtils.calculateLineDistance(new LatLng(entity.latitue, entity.longitude), new LatLng(orderInfo.getStartlat(), orderInfo.getStartlng()));
        }
        Logger.d("====================\n" + extraInfo);
        if (orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_neworder)) {
            if (userDataPreference.getAcceptModel() == 0) {
                new OrderDialog(this, orderInfo).builder()
                        .setOrderDistance(StringUtil.doubleFormat(distance / 1000))
                        .setReceiveClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TO
                                acceptOrder(orderInfo.getId());
                            }
                        }).show();
            } else {
                acceptOrder(orderInfo.getId());
            }
        } else if (orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_hasorder) ||
                orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_cancelOrder)) {
            getOrderList();
        } else if (orderInfo.getPayload().equals(ConfigUtil.PAYLOAD_LOGOUT)) {
            userDataPreference.removeData(UserDataPreference.TOKEN);
            startActivity(new Intent(this, LoginActivity.class));
            SysApplication.getInstance().exit();
        }
    }

    private void showUpdateDialog(final UpdateBean updateBean) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新提示");
        builder.setMessage(updateBean.getContent());
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(updateBean.getApk_url());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
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
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
//                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void showTip(String str) {
        ToastUtil.showToast(this, str);
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "55");
            //设置音量
            mTts.setParameter(SpeechConstant.VOLUME, "tts_volume");
            //设置语调
            mTts.setParameter(SpeechConstant.PITCH, "tts_pitch");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    // 语记安装助手类
    private void playVoice(String content) {
        // 设置参数
        setParam();
        mTts.startSpeaking(content, mTtsListener);
    }
}
