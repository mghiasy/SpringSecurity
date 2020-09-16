package com.example.demo.jwt;

public class UserPassAuthenticationRequest {
    private String username;
    private String password;

    public UserPassAuthenticationRequest() {
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
