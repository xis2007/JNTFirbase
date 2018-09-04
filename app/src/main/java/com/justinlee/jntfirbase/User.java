package com.justinlee.jntfirbase;

public class User {
    private String key;
    private String email;
    private String name;

    public User() {
    }

    public User(String userKey, String email, String name) {
        this.key = userKey;
        this.email = email;
        this.name = name;
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
