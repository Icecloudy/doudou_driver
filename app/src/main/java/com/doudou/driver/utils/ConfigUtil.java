package com.doudou.driver.utils;

public class ConfigUtil {

    public static final String SERVER_URL = "http://doudou.zzwjhy.com/index.php";
    // public static final String SERVER_URL = "http://car.lexingcar.com/index.php";
    public static final int SUCCESS_CODE = 200;


    //------------------------推送标识  start
    /**
     * payload(string):'neworder' 新订单，用户下单后推送                         司机端
     * payload(string):'shipping' 司机正前来接驾，司机接单后会推送                乘客端
     * payload(string):'driverReady' 司机已就位                                乘客端
     * payload(string):'startCharging' 开始收费                                乘客端
     * payload(string):'cancelOrder' 取消订单                                  司机端或乘客端
     * payload(string):'debit' 取消订单后的扣款通知                              乘客端
     * payload(string):'payment' 提醒乘客前往支付订单                            乘客端
     * payload(string):'txt' 其他推送，只用于推送文字内容，无需任何后继操作         司机端或乘客端
     */
    public static final String PAYLOAD_neworder = "neworder";
    public static final String PAYLOAD_shipping = "shipping";
    public static final String PAYLOAD_driverReady = "driverReady";
    public static final String PAYLOAD_startCharging = "startCharging";
    public static final String PAYLOAD_cancelOrder = "cancelOrder";
    public static final String PAYLOAD_debit = "debit";
    public static final String PAYLOAD_payment = "payment";
    public static final String PAYLOAD_txt = "txt";
    public static final String PAYLOAD_hasorder = "hasorder";
    public static final String PAYLOAD_LOGOUT = "offLine";
    public static final String WECHAT_PAY_APPID = "wxf2c7303cae0cda0e";
    public static final String QQ_SHARE_APPID = "1106379008";
    /**
     * broadcast start
     */
    public static final String EXTRA_CODE = "broadcast_intent";
    public static final String FILTER_CODE = "broadcast_filter";
    public static final String UPDATE_USER = "update_user";//租客登记后通知房屋房里更新数据
    public static final String UPDATE_UNFINISH_ORDER = "update_unfinish_order";//租客登记后通知房屋房里更新数据
    //更新待办事项列表
    /**
     * broadcast end
     */
    public static final String GET_CODE = "/App/Api/verifiDriver";
    public static final String LOGIN = "/App/Api/loginDriver";
    public static final String DRIVER_REGISTER = "/App/Api/registerDriver";
    public static final String REPORT_LOCATION = "/App/Driver/reportPosition";

    public static final String SET_DRIVER_STATE = "/App/Driver/setWorkStatus";
    public static final String GET_DRIVER_STATE = "/App/Driver/getDriverStatus";
    public static final String ACCEPT_ORDER = "/App/Place/acceptOrder";

    public static final String DRIVER_READY = "/App/DriverCenter/driverReady";
    public static final String DRIVER_ARRIVE = "/App/Place/Arrive";
    public static final String GET_PRICE = "/App/AppSystem/getBasePrice";
    public static final String GET_DRIVER_DATA = "/App/DriverCenter/getDriverInfo";

    public static final String GET_DRIVER_RANKING = "/App/DriverCenter/getMyRanking";
    public static final String START_CHARGING = "/App/Place/startCharging";

    public static final String GET_ORDER_LIST = "/App/DriverCenter/getUnFinishOrderList";
    public static final String CANCEL_ORDER = "/App/Place/CancelOrder";
    public static final String GET_TRIP_RECORD = "/App/MyCenter/getTravelRecord";
    public static final String GET_TRIP_RECORD_DETAILS = "/App/MyCenter/getTravelRecordDetail";

    public static final String GET_ORDER_DETAIL = "/App/DriverCenter/getOrderDetailByDriver";

    public static final String UPDATA_USERINFO = "/App/Api/personalUpdate";

    public static final String GET_DRIVER_INFO = "/App/Api/personalSelect";
    public static final String GET_BILL_DETAIL = "/App/AppSystem/getTransactionInfo";
    public static final String WITHDRAW = "/App/Driver/withdraw";

    public static final String GET_SYSTEM_AdV = "/App/AppSystem/getAdvertisement";
    //发送找回密码验证码
    //地址：/App/Api/retrieveVerifi
    public static final String FIND_PWD_BACK = "/App/Api/retrieveVerifi";
    //修改密码:修改密码后的token会变更，会返回新的token
    // 地址：/App/Api/retrieveDriver
    public static final String FIND_PWD_SET = "/App/Api/retrieveDriver";
    public static final String MESSAGE = "/App/Message/message";
    public static final String CLEAN_NEWS = "/App/Message/clear";


    public static final String CHANGE_PWD = "/App/Driver/pwdUpdate";

    public static final String GET_SYSTEM_AGREEMENT = "/App/AppSystem/getAppSystemInfo";

    public static final String GET_RANK = "/App/DriverCenter/getRankingList";

    public static final String GET_THOUGHT = "/App/DriverCenter/getThoughtList";


    public static final String FEEDBACK = "/App/MyCenter/setFeedback";

    public static final String APP_UPDATE = "/App/App/index";

    public static final String SET_OFF = "/App/DriverCenter/setOff";

    public static final String EDIT_MODEL = "/App/DriverCenter/editMode";

    public static final String GET_RULE = "/App/DriverCenter/getRule";
    public static final String GET_ORDER_CHARGE = "/App/Place/orderCharge";
    public static final String GET_SHARE_DATA = "/App/MyCenter/shareHistory";

}
