package com.krescendos.web.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.krescendos.dialog.QuickDialog;

/**
 * Adapted from: https://stackoverflow.com/questions/25678216/android-internet-connectivity-change-listener
 * Date: 4:31 p.m. 11/11/2017
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(status==NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
                QuickDialog dialog = new QuickDialog(context, "Network", "You need an internet connection to use Krescendos");
                dialog.show();
            }else{
                QuickDialog dialog = new QuickDialog(context, "Network", "Network connection resumed");
                dialog.show();
            }
        }
    }
}
