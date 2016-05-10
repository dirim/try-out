package com.saifproject.term;

public class ExampleTokenStore {
    private static ExampleTokenStore sInstance;
    private String token;

    public static ExampleTokenStore get() {
        if (sInstance == null) {
            sInstance = new ExampleTokenStore();
        }

        return sInstance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
