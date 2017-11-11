package com.krescendos.web.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Retrieved from: https://stackoverflow.com/questions/25678216/android-internet-connectivity-change-listener
 * Date: 4:31 p.m. 11/11/2017
 */

public class NetworkUtil {
    public static final int NETWORK_STATUS_NOT_CONNECTED = 0, NETWORK_STAUS_WIFI = 1, NETWORK_STATUS_MOBILE = 2;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    private static Map<Context, NetworkChangeReceiver> receivers = new HashMap<>();

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        int status = 0;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = NETWORK_STAUS_WIFI;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = NETWORK_STATUS_MOBILE;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }

    public static void registerConnectivityReceiver(Context context, NetworkChangeReceiver receiver) {
        final String action = "android.net.conn.CONNECTIVITY_CHANGE";

        IntentFilter filter = new IntentFilter(action);
        context.registerReceiver(receiver, filter);

        receivers.put(context, receiver);
    }

    public static void unregisterConnectivityReceiver(Context context) {
        if (!receivers.containsKey(context)) {
            return;
        }
        context.unregisterReceiver(receivers.get(context));
        receivers.remove(context);
    }
}
