package com.risenb.witness.utils.newUtils;

public class IDcardUtils {
    private static IDcardUtils idCardUtils;
    public int symbols;

    public static IDcardUtils getIdCardUtils() {
        if(idCardUtils == null) {
            idCardUtils = new IDcardUtils();
        }

        return idCardUtils;
    }

    public void info(int symbols) {
        this.symbols = symbols;
    }
}
