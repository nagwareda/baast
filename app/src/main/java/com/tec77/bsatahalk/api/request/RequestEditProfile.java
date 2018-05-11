package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 09/05/2018.
 */

public class RequestEditProfile {

    public RequestEditProfile() {
    }
    private int user_id;

    public int getUserId() { return this.user_id; }

    public void setUserId(int user_id) { this.user_id = user_id; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String email;

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    private String phone;

    public String getPhone() { return this.phone; }

    public void setPhone(String phone) { this.phone = phone; }

    private String user_image;

    public String getUserImage() { return this.user_image; }

    public void setUserImage(String user_image) { this.user_image = user_image; }
}
