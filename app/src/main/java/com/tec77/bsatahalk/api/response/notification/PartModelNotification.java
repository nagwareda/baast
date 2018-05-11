package com.tec77.bsatahalk.api.response.notification;

/**
 * Created by Mahmoud Shaeer on 02/10/2017.
 */

public class PartModelNotification {

    private int notification_id;
    private String notification_content;
    private long notification_date;
    private String notification_type;
    private int order_id;
    private int client_id;


    public PartModelNotification() {
    }

    public PartModelNotification(int notification_id, String notification_content, long notification_date, String notification_type, int order_id) {
        this.notification_id = notification_id;
        this.notification_content = notification_content;
        this.notification_date = notification_date;
        this.notification_type = notification_type;
        this.order_id = order_id;
    }



    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_content() {
        return notification_content;
    }

    public void setNotification_content(String notification_content) {
        this.notification_content = notification_content;
    }

    public long getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(long notification_date) {
        this.notification_date = notification_date;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }
}
