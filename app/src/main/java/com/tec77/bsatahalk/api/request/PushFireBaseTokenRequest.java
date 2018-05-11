package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 26/03/2018.
 */

public class PushFireBaseTokenRequest {
    private int user_id;
    private String token;

    public PushFireBaseTokenRequest() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
