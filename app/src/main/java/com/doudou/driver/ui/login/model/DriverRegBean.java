package com.doudou.driver.ui.login.model;

/**
 * Created by Administrator on 2017/4/20.*
 * phone 手机号
 * username 真实姓名
 * drivingn 驾驶证号
 * startdate 驾驶证初次领取日期(0000-00-00)
 * driving 驾驶证照片
 * carnum 车牌号 选择+输入
 * brand 车型 如野马T80
 * holder 车辆所有人
 * carregdate 车辆注册时间(0000-00-00)
 * color 车身颜色
 * carphoto 车辆照片
 * paypass 密码
 *
 */

public class DriverRegBean {
    private String phone;
    private String username;
    private String drivingn;
    private String startdate;
    private String driving;
    private String carnum;
    private String brand;
    private String holder;
    private String carregdate;
    private String color;
    private String carphoto;
    private String license;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDrivingn() {
        return drivingn;
    }

    public void setDrivingn(String drivingn) {
        this.drivingn = drivingn;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getDriving() {
        return driving;
    }

    public void setDriving(String driving) {
        this.driving = driving;
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getCarregdate() {
        return carregdate;
    }

    public void setCarregdate(String carregdate) {
        this.carregdate = carregdate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCarphoto() {
        return carphoto;
    }

    public void setCarphoto(String carphoto) {
        this.carphoto = carphoto;
    }

}
