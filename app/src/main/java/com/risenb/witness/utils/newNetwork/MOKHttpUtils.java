package com.risenb.witness.utils.newNetwork;

import com.alibaba.fastjson.JSONObject;
import com.risenb.witness.utils.newNetwork.okhttp.OkHttpUtils;
import com.risenb.witness.utils.newNetwork.okhttp.builder.GetBuilder;
import com.risenb.witness.utils.newNetwork.okhttp.builder.PostFormBuilder;
import com.risenb.witness.utils.newNetwork.okhttp.callback.StringCallback;
import com.risenb.witness.utils.newNetwork.okhttp.request.RequestCall;
import com.risenb.witness.utils.newUtils.Log;
import com.risenb.witness.utils.newUtils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MOKHttpUtils<C extends MutilsBaseBean> {
    private Class<C> cla;
    private NetMethod netMethod;
    private Map<String, Boolean> urlMap = new HashMap();
    private Map<String, Integer> urlProgre = new HashMap();
    protected static List<String> listHttp = new ArrayList();
    private static List<String> listHttp2 = new ArrayList();
    private int progreNum = 0;
    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

    public MOKHttpUtils(Class<C> cla, NetMethod netMethod, long readTimeOut, long writeTimeOut, long connTimeOut) {
        this.cla = cla;
        this.netMethod = netMethod;
        this.readTimeOut = readTimeOut;
        this.writeTimeOut = writeTimeOut;
        this.connTimeOut = connTimeOut;
    }

    protected void putUrl(String key) {
        this.urlMap.put(key, Boolean.valueOf(true));
    }

    public <T> void send(String url, ReqParams params, HttpBack<T> httpBack) {
        this.send(url, params, this.netMethod, httpBack);
    }

    public <T> void send(String url, ReqParams params, NetMethod netMethod, HttpBack<T> httpBack) {
        if (!Utils.getUtils().isNetworkConnected()) {
            httpBack.onFailure("请检查网络连接是否正常", "请检查网络连接是否正常");
            httpBack.onHttpOver();
        } else {
            if (!this.urlMap.containsKey(url)) {
                listHttp.add(url);
                listHttp2.add(url);
            }

            switch (netMethod.ordinal()) {
                case 1:
                    this.get(url, params, httpBack);
                    break;
                case 2:
                    this.post(url, params, httpBack);
            }

        }
    }

    private <T> void post(String url, ReqParams params, HttpBack<T> httpBack) {
        PostFormBuilder builder = OkHttpUtils.post();
        builder.url(url);
        builder.params(params.getParam());
        builder.headers(params.getHead());
        Iterator requestCall = params.getFile().entrySet().iterator();

        while (requestCall.hasNext()) {
            Map.Entry entry = (Map.Entry) requestCall.next();
            builder.addFile((String) entry.getKey(), ((File) entry.getValue()).getName(), (File) entry.getValue());
        }

        RequestCall requestCall1 = builder.build();
        requestCall1.readTimeOut(this.readTimeOut);
        requestCall1.writeTimeOut(this.writeTimeOut);
        requestCall1.connTimeOut(this.connTimeOut);
        requestCall1.execute(new MOKHttpUtils.MyStringCallback(url, params, httpBack));
    }

    private <T> void get(String url, ReqParams params, HttpBack<T> httpBack) {
        GetBuilder builder = OkHttpUtils.get();
        builder.url(url);
        builder.params(params.getParam());
        builder.headers(params.getHead());
        RequestCall requestCall = builder.build();
        requestCall.readTimeOut(this.readTimeOut);
        requestCall.writeTimeOut(this.writeTimeOut);
        requestCall.connTimeOut(this.connTimeOut);
        requestCall.execute(new MOKHttpUtils.MyStringCallback(url, params, httpBack));
    }

    private class MyStringCallback<T> extends StringCallback {
        private String url;
        private HttpBack<T> httpBack;
        private String value = "";

        public MyStringCallback(String var1, ReqParams params, HttpBack<T> httpBack) {
            this.url = var1;
            this.httpBack = httpBack;

            Map.Entry entry;
            for (Iterator var5 = params.getParam().entrySet().iterator(); var5.hasNext(); this.value = this.value + "[" + (String) entry.getKey() + "=" + (String) entry.getValue() + "]\n") {
                entry = (Map.Entry) var5.next();
            }

        }

        public void inProgress(float progress, long total, int id) {
            super.inProgress(progress, total, id);
            if (!MOKHttpUtils.this.urlMap.containsKey(this.url)) {
                MOKHttpUtils.this.urlProgre.put(this.url, Integer.valueOf((int) (progress * 100.0F)));
                if (MOKHttpUtils.listHttp2.size() != 0) {
                    int progre = 0;
                    Iterator var6 = MOKHttpUtils.listHttp2.iterator();

                    while (var6.hasNext()) {
                        String key = (String) var6.next();
                        if (MOKHttpUtils.this.urlProgre.get(key) != null) {
                            progre += ((Integer) MOKHttpUtils.this.urlProgre.get(key)).intValue();
                        }
                    }

                    progre /= MOKHttpUtils.listHttp2.size();
                    if (MOKHttpUtils.this.progreNum < progre && progre < 100) {
                        MOKHttpUtils.this.progreNum = progre;
                        this.httpBack.onProgre(MOKHttpUtils.this.progreNum);
                    }
                } else {
                    this.httpBack.onProgre(99);
                }
            }

        }

        public void onError(Call call, Exception e, int id) {
            MOKHttpUtils.listHttp.remove(this.url);
            Log.e("HTTP", "\nurl=" + this.url + "\n" + this.value);
            e.printStackTrace();
            this.httpBack.onHttpOver(MOKHttpUtils.listHttp);
            if (MOKHttpUtils.listHttp.size() == 0) {
                this.httpBack.onHttpOver();
                MOKHttpUtils.listHttp2.clear();
                MOKHttpUtils.this.progreNum = 0;
            }

        }

        public void onResponse(String result, int id) {
            MOKHttpUtils.listHttp.remove(this.url);
            Log.e(result);
            Log.e("HTTP", "\nurl=" + this.url + "\n" + this.value + "\n" + result);
            if (this.httpBack.newInst() instanceof String) {
                this.httpBack.onString(result);
            }

            MutilsBaseBean bean = JSONObject.parseObject(result, MOKHttpUtils.this.cla);
            if (bean.isStatus()) {
                if (this.httpBack.newInst() instanceof MutilsBaseBean) {
                    this.httpBack.onSuccess((T) bean);
                } else if (this.httpBack.newInst() instanceof String) {
                    this.httpBack.onSuccess((T) result);
                } else if (bean.getData().charAt(0) == 123) {
                    this.httpBack.onSuccess(JSONObject.parseObject(bean.getData(), this.httpBack.getTClass()));
                } else {
                    this.httpBack.onSuccess(JSONObject.parseArray(bean.getData(), this.httpBack.getTClass()));
                }
            } else {
                this.httpBack.onFailure("", bean.getMsg());
            }

            this.httpBack.onHttpOver(MOKHttpUtils.listHttp);
            if (MOKHttpUtils.listHttp.size() == 0) {
                this.httpBack.onHttpOver();
                MOKHttpUtils.listHttp2.clear();
                MOKHttpUtils.this.progreNum = 0;
            }

        }
    }
}
