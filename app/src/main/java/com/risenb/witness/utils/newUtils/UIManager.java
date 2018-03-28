package com.risenb.witness.utils.newUtils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UIManager {
    private static volatile UIManager instance;
    private List<Activity> list = new ArrayList();

    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }

        return instance;
    }

    public UIManager() {
    }

    public void pushActivity(Activity activity) {
        if (LimitConfig.getLimitConfig().isLimit()) {
            this.list.add(activity);
        }
    }

    public void popActivity(Class<?> cls) {
        ArrayList list2 = new ArrayList();
        Iterator var3 = this.list.iterator();

        while (var3.hasNext()) {
            Activity activity = (Activity) var3.next();
            if (cls.equals(activity.getClass())) {
                activity.finish();
            } else {
                list2.add(activity);
            }
        }

        this.list = list2;
    }

    public void popOtherActivity(Class... cls) {
        if (null != cls) {
            boolean i = false;
            ArrayList list2 = new ArrayList();
            Iterator var4 = this.list.iterator();

            while (var4.hasNext()) {
                Activity activity = (Activity) var4.next();

                int var6;
                for (var6 = 0; var6 < cls.length && !activity.getClass().equals(cls[var6]); ++var6) {
                    ;
                }

                if (var6 == cls.length) {
                    activity.finish();
                } else {
                    list2.add(activity);
                }
            }

            this.list = list2;
        }
    }

    public void popAllActivity() {
        Iterator var1 = this.list.iterator();

        while (var1.hasNext()) {
            Activity activity = (Activity) var1.next();
            activity.finish();
        }

        this.list.clear();
    }
}
