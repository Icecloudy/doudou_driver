package com.doudou.driver.data.models;

/**
 * Created by Administrator on 2017/5/8.
 * {
 * photo 头像,
 * sex(int) 性别[1男 0女],
 * username 姓名,
 * phone 手机,
 * brand 车型,
 * carnum 车牌号,
 * color(string) 颜色
 * }
 */

public class DriverInfo {
    private String photo;
    private int sex;
    private String username;
    private String phone;

    private String brand;
    private String carnum;
    private String color;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
