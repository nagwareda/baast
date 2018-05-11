package com.tec77.bsatahalk.listener;

import com.tec77.bsatahalk.api.response.SchoolClassesResponse;

import java.util.ArrayList;

/**
 * Created by Nagwa on 05/03/2018.
 */

public interface SchoolClassResponseListener {
    public void categories(ArrayList<SchoolClassesResponse.Category>categoryList);
}
