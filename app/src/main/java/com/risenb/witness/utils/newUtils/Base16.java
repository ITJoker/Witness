package com.risenb.witness.utils.newUtils;

public class Base16 {

    private static String[] str16 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public Base16() {

    }

    public static String encode(int num) {
        String str = str16[num >> 4];
        str = str + str16[num & 15];
        return str;
    }

    public static String encode(byte[] encrypted) {
        String str = "";
        byte[] var3 = encrypted;
        int var4 = encrypted.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            boolean num = false;
            int var7 = b & 255;
            str = str + encode(var7);
        }

        return str;
    }

    public static byte[] str2byte(String str) {
        byte[] result = new byte[str.length() / 2];

        try {
            int e = 0;

            for (int j = 0; e < result.length; j += 2) {
                result[e] = (byte) (result[e] | Integer.parseInt(str.charAt(j) + String.valueOf(str.charAt(j + 1)), 16));
                ++e;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }
}
