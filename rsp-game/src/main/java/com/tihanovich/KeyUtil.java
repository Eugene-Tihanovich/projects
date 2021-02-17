package com.tihanovich;

import java.security.SecureRandom;

public class KeyUtil {

    public static byte[] getKey(int bytes) {
        byte[] key = new byte[bytes];
        new SecureRandom().nextBytes(key);
        return key;
    }

    public static String bytesToHex(byte[] key) {
        StringBuilder sb = new StringBuilder();
        for (byte b : key) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}