package com.doudou.driver;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/12.
 */

public class SysApplication extends Application {
    private ArrayList<Activity> mList = new ArrayList<>();
    private static SysApplication instance;

    private SysApplication() {
    }


    public synchronized static SysApplication getInstance() {
        if (null == instance) {
            instance = new SysApplication();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }


    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
