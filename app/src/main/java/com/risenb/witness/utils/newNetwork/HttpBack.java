package com.risenb.witness.utils.newNetwork;

import android.text.TextUtils;
import android.widget.Toast;

import com.risenb.witness.utils.newUtils.ErrorUtils;
import com.risenb.witness.utils.newUtils.Log;
import com.risenb.witness.utils.newUtils.MUtils;
import com.risenb.witness.utils.newUtils.Utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class HttpBack<T> {
    private Class<T> entityClass;

    public void onFailure(String tag, final String msg) {
        if (!TextUtils.isEmpty(tag)) {
            Log.e("HTTP", tag);
        }

        MUtils.getMUtils().getHandler().post(new Runnable() {
            public void run() {
                Toast.makeText(MUtils.getMUtils().getApplication(), ErrorUtils.get(msg), 0).show();
            }
        });
    }

    public void onHttpOver() {
        Utils.getUtils().dismissDialog();
    }

    public void onString(String result) {
    }

    public void onSuccess(T result) {
    }

    public void onSuccess(List<T> list) {
    }

    public void onHttpOver(List<String> listHttp) {
        if (listHttp.size() == 0) {
            Log.e("http over");
        } else {
            Log.e("http num：" + listHttp.size());
        }

    }

    public void onProgre(int progre) {
        Utils.getUtils().setCurrentValues(progre);
    }

    public HttpBack() {
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.entityClass = (Class) params[0];
    }

    public Class<T> getTClass() {
        return this.entityClass;
    }

    public T newInst() {
        try {
            return this.entityClass.newInstance();
        } catch (InstantiationException var2) {
            var2.printStackTrace();
        } catch (IllegalAccessException var3) {
            var3.printStackTrace();
        }

        return null;
    }
}
