package com.doudou.driver.ui.profile.rank.adapter;

/**
 * Created by Administrator on 2017/5/12.
 * id(int): 司机id,
 * photo(string): 头像,
 * username(string): 昵称,
 * orders(int): 今日接单数
 */

public class RankBean {
    private int id;
    private String photo;
    private String username;
    private int orders;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }
}
