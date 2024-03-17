package com.example.signproject.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InvitationUtil {
    public static String CreateCode(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get MessageDigest instance", e);
        }
    }
}
