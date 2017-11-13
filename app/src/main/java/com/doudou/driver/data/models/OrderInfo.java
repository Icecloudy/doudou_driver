package com.doudou.driver.data.models;

/**
 * Created by Administrator on 2017/4/25.
 * id(int) 订单id
 * nmber(string) 订单号
 * addresses(string) 上车地址
 * latitude(double) 上车纬度
 * longitude(double) 上车经度
 * down(string) 下车地址
 * latitudel(double) 上车纬度
 * longitudel(double) 上车经度
 * uid(int) 乘客id
 * did(int) 司机id
 * status(int) 订单状态[1未接,2已接,3载客中,4已到达,5已结单,6已评价,7取消]
 * <p>
 * type(int) [1实时,2预约,3指派]
 * <p>
 * gotime(string) 开始收费时间(2017-05-02 18:40:08)
 * price(double) 车费
 * placetime(string) 下单时间(2017-05-02 18:40:08)
 * overtime(string) 结束收费时间(2017-05-02 18:40:08)
 * cost(double) 预估费用
 * distance(double) 行驶距离(公里)
 * <p>
 * 当status=2或status=3时，还返回：dlat(double) 司机纬度,dlng(double) 司机经度
 */

public class OrderInfo {
    private String payload;
    private int type;
    private int id;
    private String nmber;
    private String start;
    private String end;
    private double startlat;
    private double startlng;

    private double endlat;
    private double endlng;
    private String cost;
    private PassengerUser user;

    private String addresses;
    private double latitude;
    private double longitude;
    private String down;
    private double latitudel;
    private double longitudel;

    private String gotime;
    private int status;

    private String booktime;

    public String getBooktime() {
        return booktime;
    }

    public void setBooktime(String booktime) {
        this.booktime = booktime;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public double getLatitudel() {
        return latitudel;
    }

    public void setLatitudel(double latitudel) {
        this.latitudel = latitudel;
    }

    public double getLongitudel() {
        return longitudel;
    }

    public void setLongitudel(double longitudel) {
        this.longitudel = longitudel;
    }

    public String getGotime() {
        return gotime;
    }

    public void setGotime(String gotime) {
        this.gotime = gotime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PassengerUser getUser() {
        return user;
    }

    public void setUser(PassengerUser user) {
        this.user = user;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNmber() {
        return nmber;
    }

    public void setNmber(String nmber) {
        this.nmber = nmber;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public double getStartlat() {
        return startlat;
    }

    public void setStartlat(double startlat) {
        this.startlat = startlat;
    }

    public double getStartlng() {
        return startlng;
    }

    public void setStartlng(double startlng) {
        this.startlng = startlng;
    }

    public double getEndlat() {
        return endlat;
    }

    public void setEndlat(double endlat) {
        this.endlat = endlat;
    }

    public double getEndlng() {
        return endlng;
    }

    public void setEndlng(double endlng) {
        this.endlng = endlng;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
