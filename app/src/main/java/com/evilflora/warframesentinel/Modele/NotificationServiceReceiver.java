package com.evilflora.warframesentinel.Modele;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppSettings settings = new AppSettings(context);
        Intent myService = new Intent(context, NotificationServiceClass.class);
        if (settings.isNotificationEnabled()) {
            context.startService(myService);
        } else {
            context.stopService(myService);
        }
    }
}