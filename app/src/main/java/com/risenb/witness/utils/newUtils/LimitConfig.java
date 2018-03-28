package com.risenb.witness.utils.newUtils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.risenb.witness.MyApplication;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LimitConfig {
    private static LimitConfig limitConfig;
    private Map<String, Program.DataBean> map = new HashMap();
    private boolean limit = false;
    private final String TAG = "LIMITCONFIGJSON";
    private final String TAG1 = "L464DBE4AB87C340E770284EE271DD2A00CBAA1E0D15F063AEFC687E5E290C669L";
    private final String TAG2 = "LB34808402E7E5E18B3290C669431234ED15F063AEFC68290C669435D83C59AECL";
    public static String KEY;

    public LimitConfig() {
    }

    public static LimitConfig getLimitConfig() {
        if (limitConfig == null) {
            limitConfig = new LimitConfig();
        }

        return limitConfig;
    }

    public synchronized void info() {
        /*(new Thread(new Runnable() {
            public void run() {
                if (Utils.getUtils().isNetworkConnected()) {
                    try {
                        String e = LimitConfig.this.getWeb("http://xutils.blog.163.com/blog/static/245287027201622411841188/");
                        int start = e.indexOf("L464DBE4AB87C340E770284EE271DD2A00CBAA1E0D15F063AEFC687E5E290C669L") + "L464DBE4AB87C340E770284EE271DD2A00CBAA1E0D15F063AEFC687E5E290C669L".length();
                        int end = e.indexOf("LB34808402E7E5E18B3290C669431234ED15F063AEFC68290C669435D83C59AECL");
                        if (start >= "L464DBE4AB87C340E770284EE271DD2A00CBAA1E0D15F063AEFC687E5E290C669L".length() && end >= start) {
                            String json = e.substring(start, end).trim();
                            MUtils.getMUtils().setShared("LIMITCONFIGJSON", json);
                        } else {
                            System.out.println("LimitConfig err 163");
                        }
                    } catch (Exception var5) {
                        ;
                    }

                }
            }
        })).start();

        try {
            String password = "q7578119";
            String json = MUtils.getMUtils().getShared("LIMITCONFIGJSON");
            if (!TextUtils.isEmpty(json)) {
                byte[] e = DES.decrypt(Base16.str2byte(json), password.getBytes());
                json = new String(e);
                this.jsonLimit(json);
            }

            this.get();
        } catch (Exception var4) {
            var4.printStackTrace();
        }*/

        String json = MUtils.getMUtils().getShared("Witness");
        if (TextUtils.isEmpty(json)) {
            if (MyApplication.getInstance().getOne()) {
                get();
            }
            getJson();
        } else {
            try {
                jsonLimit(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getJson() {
        String url = "http://www.adexmall.net/Task/getJson/";
        MyOkHttp.get().post(MyApplication.getInstance(), url, null, new GsonResponseHandler<Program>() {
            @Override
            public void onSuccess(int statusCode, Program response) {
                String json = new Gson().toJson(response.getData());
                MUtils.getMUtils().setShared("Witness", json);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    public void get() {
        if (!Utils.getUtils().isNetworkConnected()) {
            this.limit = true;
        } else if (this.map.size() == 0) {
            this.limit = true;
        } else if (this.map.get(KEY) == null) {
            this.limit = false;
        } else {
            this.limit = (this.map.get(KEY)).isLimit();
        }
    }

    public boolean isLimit() {
        return this.limit;
    }

    public String getWeb(String strUrl) {
        StringBuffer txt = new StringBuffer();
        String line;

        try {
            URL e = new URL(strUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(e.openStream(), "GBK"));

            while ((line = reader.readLine()) != null) {
                txt.append(line);
            }

            reader.close();
            return txt.toString();
        } catch (Exception var6) {
            return "";
        }
    }

    private void jsonLimit(String json) throws JSONException {
        JSONArray jArr = new JSONArray(json);
        ArrayList list = new ArrayList();

        for (int i = 0; i < jArr.length(); ++i) {
            JSONObject jObj = jArr.getJSONObject(i);
            Program.DataBean dataBean = new Program.DataBean();
            if (jObj.has("name")) {
                dataBean.setName(jObj.getString("name"));
            }

            if (jObj.has("depict")) {
                dataBean.setDepict(jObj.getString("depict"));
            }

            if (jObj.has("time")) {
                dataBean.setTime(jObj.getString("time"));
            }

            if (jObj.has("limit")) {
                dataBean.setLimit(jObj.getBoolean("limit"));
            }

            list.add(dataBean);
        }

        Iterator var8 = list.iterator();

        while (var8.hasNext()) {
            Program.DataBean bean = (Program.DataBean) var8.next();
            this.map.put(bean.getName(), bean);
        }

        getLimitConfig().get();
        System.out.println(KEY);
        System.out.println(getLimitConfig().isLimit() ? "Info Success" : "Info Failure");
        Log.e("System.out", getLimitConfig().isLimit() ? "Info Success" : "Info Failure");
    }
}
