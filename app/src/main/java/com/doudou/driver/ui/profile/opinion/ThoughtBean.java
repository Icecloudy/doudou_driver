package com.doudou.driver.ui.profile.opinion;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ThoughtBean {
    private int score;
    private List<ThoughtDataList> data;
    private List<ThoughtList> thought;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<ThoughtDataList> getData() {
        return data;
    }

    public void setData(List<ThoughtDataList> data) {
        this.data = data;
    }

    public List<ThoughtList> getThought() {
        return thought;
    }

    public void setThought(List<ThoughtList> thought) {
        this.thought = thought;
    }
}
