package com.doudou.driver.ui.profile.record.adapter;

/**
 * Created by Administrator on 2017/4/18.
 * data {
 * orderid(int) 订单id,
 * addresses(string) 出发地,
 * down(string) 终点,
 * status(int) 订单状态[1未接,2已接,3载客中,4已到达,5已结单,6已评价,7取消],
 * gotime(string) 开始收费时间(2017-05-02 18:40:08)
 * type(int) 订单类型 [1实时,2预约,3指派]
 * }
 */

public class TripRecordBean {
    private int orderid;
    private String placetime;
    private String down;
    private String addresses;
    private int status;//1取消 2完成
    private int type;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }


    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPlacetime() {
        return placetime;
    }

    public void setPlacetime(String placetime) {
        this.placetime = placetime;
    }
}
