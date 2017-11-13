package com.doudou.driver.data.models;

/**
 * Created by Administrator on 2017/4/24.
 */

public class Price {
    private double star;
    private double duration;
    private double nadd;
    private double wait;
    private int base;

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getNadd() {
        return nadd;
    }

    public void setNadd(double nadd) {
        this.nadd = nadd;
    }

    public double getWait() {
        return wait;
    }

    public void setWait(double wait) {
        this.wait = wait;
    }
}
