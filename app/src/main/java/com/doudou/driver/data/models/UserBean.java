package com.doudou.driver.data.models;

/**
 * Created by Administrator on 2017/4/20.
 * data{
 * name(string) 真实姓名,
 * username(string) 昵称,
 * sex(int) 性别[1男,0女],
 * phone(string),
 * photo(string) 头像,
 * playzfb(string) 支付宝账号,
 * playwx(string) 微信支付账号,
 * ptype(int) 提现类型[1支付宝,2微信],
 * balance(double) 余额,
 * status(int) 车辆状态[0在线,1离线],
 * driversta(int) 司机状态[0正常,1封停],
 * audtimeid(int) 车辆认证表id
 * authstatus(int) [审核状态0待审,1通过,2不通过]
 * price {
 * star(double) 起步价
 * duration(double) 里程价
 * nadd(double) 豆豆公司分佣百分比
 * wait(double) 等候价
 * }
 * }
 */

public class UserBean {
    private String username;
    private int id;
    private int sex;
    private String photo;
    private String phone;
    private String playzfb;
    private String playwx;
    private int ptype;
    private double balance;
    private int status;
    private int driversta;
    private String name;
    private int audtimeid;
    private int authstatus;
    private String token;
    private Price price;
    private int mode;
    private String hotline;

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlayzfb() {
        return playzfb;
    }

    public void setPlayzfb(String playzfb) {
        this.playzfb = playzfb;
    }

    public String getPlaywx() {
        return playwx;
    }

    public void setPlaywx(String playwx) {
        this.playwx = playwx;
    }

    public int getPtype() {
        return ptype;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDriversta() {
        return driversta;
    }

    public void setDriversta(int driversta) {
        this.driversta = driversta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAudtimeid() {
        return audtimeid;
    }

    public void setAudtimeid(int audtimeid) {
        this.audtimeid = audtimeid;
    }

    public int getAuthstatus() {
        return authstatus;
    }

    public void setAuthstatus(int authstatus) {
        this.authstatus = authstatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }


}
