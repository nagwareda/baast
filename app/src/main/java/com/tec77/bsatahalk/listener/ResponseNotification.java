package com.tec77.bsatahalk.listener;


import com.tec77.bsatahalk.api.response.notification.PartModelNotification;

import java.util.ArrayList;

/**
 * Created by Mahmoud Shaeer on 02/10/2017.
 */
//used as a listener to "getAllNotification" api
public interface ResponseNotification {

    /**
     * @param allNotification list of notification
     * @param page number of pages of the response
     */
    public void allNotification(ArrayList<PartModelNotification> allNotification, int page);
}
