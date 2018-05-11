package com.tec77.bsatahalk.api.response.notification;

import java.util.ArrayList;

/**
 * Created by Mahmoud Shaeer on 02/10/2017.
 */

public class ResponseNotifications {

    private boolean success;
    private int code;
    private int pages;
    private ArrayList<PartModelNotification> notifications = new ArrayList<>();

    public ResponseNotifications() {
    }

    public ResponseNotifications(boolean success, int code, int pages, ArrayList<PartModelNotification> notifications) {
        this.success = success;
        this.code = code;
        this.pages = pages;
        this.notifications = notifications;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public ArrayList<PartModelNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<PartModelNotification> notifications) {
        this.notifications = notifications;
    }
}
