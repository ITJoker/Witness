package com.risenb.witness.enums;

import java.util.HashMap;
import java.util.Map;

public class EnumUtils {
    private static EnumUtils enumUtils;
    private Map<String, EnumTAB> mapEnumTAB = new HashMap<>();

    public static EnumUtils getEnumUtils() {
        if (enumUtils == null) {
            enumUtils = new EnumUtils();
        }
        return enumUtils;
    }

    public EnumUtils() {
        EnumTAB[] enumArr = EnumTAB.values();
        for (int i = 0; i < enumArr.length; i++) {
            mapEnumTAB.put(enumArr[i].getTag(), enumArr[i]);
        }
    }

    public Map<String, EnumTAB> getMapEnumTAB() {
        return mapEnumTAB;
    }
}
