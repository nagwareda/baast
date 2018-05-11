package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 26/03/2018.
 */

public class UpdateFireBaseTokenRequest {
    private int user_id;
    private String oldToken;
    private String newToken;

    public UpdateFireBaseTokenRequest() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getOldToken() {
        return oldToken;
    }

    public void setOldToken(String oldToken) {
        this.oldToken = oldToken;
    }

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }
}
