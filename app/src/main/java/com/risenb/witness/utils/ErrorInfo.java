package com.risenb.witness.utils;

import com.risenb.witness.utils.newUtils.ErrorUtils;

public class ErrorInfo {
    public static void info() {
        ErrorUtils.getMap().put("000", "接口调用失败");
    }
}
