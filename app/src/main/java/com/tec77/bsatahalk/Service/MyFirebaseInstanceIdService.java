package com.tec77.bsatahalk.Service;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.UpdateFireBaseTokenRequest;
import com.tec77.bsatahalk.database.SharedPref;

/**
 * Created by Nagwa on 05/10/2017.
 */


/**
 * used when token of the user updated
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private SharedPref sharedPref;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sharedPref = SharedPref.getInstance(getApplicationContext());
        if (!sharedPref.getString("old_token").isEmpty()) {
            UpdateFireBaseTokenRequest body = new UpdateFireBaseTokenRequest();
            body.setNewToken(FirebaseInstanceId.getInstance().getToken());
            body.setUser_id(sharedPref.getInt("id"));
            body.setOldToken(sharedPref.getString("old_token"));
            new FastNetworkManger(getApplicationContext()).updateFireBaseToken(body);
        }
    }
}
