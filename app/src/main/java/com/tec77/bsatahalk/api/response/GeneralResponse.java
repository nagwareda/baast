package com.tec77.bsatahalk.api.response;

/**
 * Created by Nagwa on 23/02/2018.
 */

public class GeneralResponse {

    private boolean success;
    private String message;
    private int code;


    public  GeneralResponse(){

    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
