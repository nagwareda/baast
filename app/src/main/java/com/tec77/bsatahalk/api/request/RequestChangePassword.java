package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 10/05/2018.
 */

public class RequestChangePassword {
    public RequestChangePassword() {
    }

    private int user_id;

    public int getUserId() { return this.user_id; }

    public void setUserId(int user_id) { this.user_id = user_id; }

    private String oldPassword;

    public String getOldPassword() { return this.oldPassword; }

    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    private String newPassword;

    public String getNewPassword() { return this.newPassword; }

    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
