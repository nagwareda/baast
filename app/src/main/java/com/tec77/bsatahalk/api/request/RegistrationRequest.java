package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 23/02/2018.
 */

public class RegistrationRequest {
    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String email;

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    private String password;

    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }

    private String phone;

    public String getPhone() { return this.phone; }

    public void setPhone(String phone) { this.phone = phone; }

    private String user_image;

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
