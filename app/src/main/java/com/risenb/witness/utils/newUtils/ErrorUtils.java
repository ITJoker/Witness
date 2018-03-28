package com.risenb.witness.utils.newUtils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class ErrorUtils {
    private static ErrorUtils errorUtils;
    private Map<String, String> map = new HashMap();

    public ErrorUtils() {

    }

    public static Map<String, String> getMap() {
        if(errorUtils == null) {
            errorUtils = new ErrorUtils();
        }

        return errorUtils.map;
    }

    public static String get(String key) {
        String value = (String)getMap().get(key);
        if(TextUtils.isEmpty(value)) {
            value = key;
        }

        return value;
    }
}
