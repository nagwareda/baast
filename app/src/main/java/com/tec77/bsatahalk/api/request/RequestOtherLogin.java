package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 09/03/2018.
 */

public class RequestOtherLogin {

    public RequestOtherLogin(){

    }
    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String facebook_token;

    public String getFacebookToken() { return this.facebook_token; }

    public void setFacebookToken(String facebook_token) { this.facebook_token = facebook_token; }

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

    public String getUserImage() { return this.user_image; }

    public void setUserImage(String user_image) { this.user_image = user_image; }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
