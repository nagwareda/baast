package com.tec77.bsatahalk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by index-pc on 1/2/18.
 */

public class CheckConnection {
    private static volatile CheckConnection checkConnection = new CheckConnection();

    //private constructor.
    private CheckConnection() {
    }

    public static CheckConnection getInstance() {
        return checkConnection;
    }


    // check networkConnection.
    public boolean checkInternetConnection(Context context) {
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
