package com.risenb.witness.utils.newUtils;

public class Exceptions {
    public Exceptions() {

    }

    public static void illegalArgument(String msg, Object... params) {
        throw new IllegalArgumentException(String.format(msg, params));
    }
}
