package com.krescendos.web.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.krescendos.dialog.OnConfirmDialogCloseListener;
import com.krescendos.dialog.ConfirmDialog;

/**
 * Adapted from: https://stackoverflow.com/questions/25678216/android-internet-connectivity-change-listener
 * Date: 4:31 p.m. 11/11/2017
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private ConnectionLostListener connectionLostListener;

    public NetworkChangeReceiver(ConnectionLostListener connectionLostListener) {
        this.connectionLostListener = connectionLostListener;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                Log.d("NETWORK", "NOT CONNECTED");
                ConfirmDialog dialog = new ConfirmDialog(context, "Network",
                        "You need an internet connection to use Krescendos", new OnConfirmDialogCloseListener() {
                    @Override
                    public void onClose() {
                        Log.d("DIALOG_CLOSE", "onClose");
                        connectionLostListener.onNetworkConnectionLost();
                    }
                });
                dialog.show();

            } else {
                Log.d("NETWORK", "CONNECTION RESUMED");
            }
        }
    }
}
