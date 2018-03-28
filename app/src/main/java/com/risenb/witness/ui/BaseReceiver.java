package com.risenb.witness.ui;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

@SuppressLint("CommitTransaction")
public class BaseReceiver extends BroadcastReceiver {
    private WeakReference<FragmentActivity> ref;
    private OnSetFragment onSetFragment;

    public BaseReceiver(FragmentActivity activity) {
        ref = new WeakReference<>(activity);
    }

    public void setOnSetFragment(OnSetFragment onSetFragment) {
        this.onSetFragment = onSetFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        FragmentActivity activity = ref.get();
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int id = intent.getIntExtra("key", 0);
        if (onSetFragment != null && 0 != id) {
            onSetFragment.onSetFragment(id, transaction);
        }
    }

    public interface OnSetFragment {
        void onSetFragment(int id, FragmentTransaction transaction);
    }
}
