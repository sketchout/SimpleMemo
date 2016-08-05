package com.zeiyu.simplememo.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by admin on 2016-07-25.
 */
public class NetUtil {

    public static boolean verifyConnection(Context context) {
        boolean connected;

        ConnectivityManager conMgr =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();

        return connected;
    }
}
