package com.doudou.driver.data.models;

import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 * began(double) 起步公里数(公里),
 * mileage(double) 里程费（元）,
 * duration(double) 时长费（元）,
 * remote(array) 远途费(10-20不含20公里，同理，40-50不含50公里,period=50指50公里及以上)
 * 例：[
 * {"period": "10-20",
 * "extraCost": 3}],
 * peak(double) 高峰费（元）,
 * times(array) 高峰时段(整点可选,0个或多个时间段)
 * 例：["interval": "06-09"}],
 * night(double) 夜间费（元）,
 * oil(double) 燃油补贴费燃油补贴费（元）,
 * additional(double)	附加费（元，默认0元）,
 * atNight(string): 夜间时段(23:00-5:00)
 */

public class OrderRule {
    private double began;
    private double mileage;
    private double duration;
    private List<Remote> remote;
    private double peak;
    private List<Times> times;
    private double night;
    private double oil;
    private double additional;
    private String atNight;

    public double getBegan() {
        return began;
    }

    public void setBegan(double began) {
        this.began = began;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public List<Remote> getRemote() {
        return remote;
    }

    public void setRemote(List<Remote> remote) {
        this.remote = remote;
    }

    public double getPeak() {
        return peak;
    }

    public void setPeak(double peak) {
        this.peak = peak;
    }

    public List<Times> getTimes() {
        return times;
    }

    public void setTimes(List<Times> times) {
        this.times = times;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getOil() {
        return oil;
    }

    public void setOil(double oil) {
        this.oil = oil;
    }

    public double getAdditional() {
        return additional;
    }

    public void setAdditional(double additional) {
        this.additional = additional;
    }

    public String getAtNight() {
        return atNight;
    }

    public void setAtNight(String atNight) {
        this.atNight = atNight;
    }

    public static class Remote {
        private String period;
        private int extraCost;

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public int getExtraCost() {
            return extraCost;
        }

        public void setExtraCost(int extraCost) {
            this.extraCost = extraCost;
        }
    }

    public static class Times {
        private String interval;

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }
    }
}
