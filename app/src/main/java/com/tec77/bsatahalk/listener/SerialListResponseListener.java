package com.tec77.bsatahalk.listener;

import com.tec77.bsatahalk.api.response.SerialListResponse;

import java.util.ArrayList;

/**
 * Created by Nagwa on 01/03/2018.
 */

public interface SerialListResponseListener {
    public void serialList(ArrayList<SerialListResponse.LessonPartModel>serialList,int maxPage);
}
