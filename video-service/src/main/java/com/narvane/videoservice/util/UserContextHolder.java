package com.narvane.videoservice.util;

public class UserContextHolder {
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    public static String getUserId() {
        return userIdHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
    }
}
