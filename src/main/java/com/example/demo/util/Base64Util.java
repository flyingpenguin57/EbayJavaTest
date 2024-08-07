package com.example.demo.util;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {

    public static String decodeBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

    public static String encodeBase64(String rawString) {
        byte[] encodedBytes = Base64.getEncoder().encode(rawString.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes);
    }
}
