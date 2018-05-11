package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 24/02/2018.
 */

public class LoginRequest {
    private String email;

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    private String password;

    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }
}
