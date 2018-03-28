package com.risenb.witness.utils;

import android.app.Activity;

import com.lidroid.xutils.DbUtils;
import com.risenb.witness.MyApplication;

public class DbHelp {
    private static DbUtils db;

    public static DbUtils getDb(Activity activity) {
        if (db == null) {
            MyApplication application = (MyApplication) activity.getApplication();
            db = DbUtils.create(activity, application.getPath(), "zhongrun.db");
        }
        return db;
    }
}
