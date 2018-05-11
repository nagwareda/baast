package com.tec77.bsatahalk.api.response.notification;

/**
 * Created by ahmed on 10/15/17.
 */

public class NotificationResponse {
    private String message_id;

    public NotificationResponse(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
}
