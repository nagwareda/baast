package com.tec77.bsatahalk.api.response;

/**
 * Created by Nagwa on 24/02/2018.
 */

public class LoginResponse {

    private boolean success;

    public boolean getSuccess() { return this.isSuccess(); }

    public void setSuccess(boolean success) { this.success = success; }

    private PartModelStudent student;

    private String message;

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    private int code;

    public int getCode() { return this.code; }

    public void setCode(int code) { this.code = code; }

    public boolean isSuccess() {
        return success;
    }

    public PartModelStudent getStudent() {
        return student;
    }

    public void setStudent(PartModelStudent student) {
        this.student = student;
    }
}
