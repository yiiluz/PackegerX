package com.packager.user.Receivers;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.packager.user.Utils.NetworkUtil;

public class ConnectivityService extends IntentService {

    public ConnectivityService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int status = NetworkUtil.getConnectivityStatusString(getBaseContext());
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                sendBroadcast(new Intent("no_internet_connection"));
            }
        }
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(true);
    }
}