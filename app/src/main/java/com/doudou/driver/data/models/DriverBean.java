package com.doudou.driver.data.models;

/**
 * Created by Administrator on 2017/4/28.
 * photo(string) 司机头像
 * username 司机昵称
 * score(double) 评分
 * countorders(int) 接单总数
 * orders(int) 今日接单
 * carnum(string) 车牌号
 * color(string) 颜色
 * brand(string) 车型
 * type(int) 车辆分类[1社会车,2自营专车,3高端车]
 * phone(string) 司机电话
 * service(double) 服务分
 * canselrate(string) 取消率
 */

public class DriverBean {
    private String photo;
    private String username;
    private double score;
    private int countorders;
    private int orders;
    private String carnum;
    private String color;
    private String brand;
    private int type;
    private double service;
    private String canselrate;
    private String phone;

    private int sex;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getCountorders() {
        return countorders;
    }

    public void setCountorders(int countorders) {
        this.countorders = countorders;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getService() {
        return service;
    }

    public void setService(double service) {
        this.service = service;
    }

    public String getCanselrate() {
        return canselrate;
    }

    public void setCanselrate(String canselrate) {
        this.canselrate = canselrate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
