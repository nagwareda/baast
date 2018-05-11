package com.tec77.bsatahalk.listener;

import com.tec77.bsatahalk.api.response.TopTenResponse;

import java.util.ArrayList;

/**
 * Created by Nagwa on 01/03/2018.
 */

public interface TopTenListResponseListener {
    public void topTenList(ArrayList<TopTenResponse.TopTenModel> topTenList);
}
