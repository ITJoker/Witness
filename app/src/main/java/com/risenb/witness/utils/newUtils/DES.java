package com.risenb.witness.utils.newUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {
    public DES() {

    }

    public static byte[] encrypt(byte[] datasource, byte[] key) {
        try {
            SecureRandom e = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, e);
            return cipher.doFinal(datasource);
        } catch (Throwable var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, random);
        return cipher.doFinal(src);
    }
}
