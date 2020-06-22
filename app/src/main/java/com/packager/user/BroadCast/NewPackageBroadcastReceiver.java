package com.packager.user.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.packager.user.R;
import com.packager.user.Utils.NetworkUtil;


public class NewPackageBroadcastReceiver extends BroadcastReceiver {
    private String CHANNEL_ID = "BroadcastReceiver";
    private static int packageCount = 0;

    private  NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null){
            notificationManager = NotificationManagerCompat.from(context);
            switch (intent.getAction()){
                case "new_package_service":
                    builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_new_package)
                            .setContentTitle("Update on your Packages!")
                            .setContentText("You receive new package")
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    notificationManager.notify(packageCount++, builder.build());
                    break;
                case "android.net.conn.CONNECTIVITY_CHANGE":
                case "android.net.wifi.WIFI_STATE_CHANGED":
                    int status = NetworkUtil.getConnectivityStatusString(context);
                    if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_sync)
                                .setContentTitle("No internet connection")
                                .setContentText("Check your connection")
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_MAX);
                        notificationManager.cancel(8081);
                        notificationManager.notify(8080, builder.build());
                    }
                    else {
                        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_sync)
                                .setContentTitle("Back On Line !")
                                .setContentText("Internet connection is Back")
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_MAX);
                        notificationManager.cancel(8080);
                        notificationManager.notify(8081, builder.build());
                    }
                    break;
            }
        }
    }
}
