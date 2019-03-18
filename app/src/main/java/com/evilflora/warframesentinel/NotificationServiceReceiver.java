package com.evilflora.warframesentinel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppSettings settings = new AppSettings(context);
        Intent myService = new Intent(context, NotificationServiceClass.class);
        if (settings.get_activate_notification()) {
            context.startService(myService);
        } else {
            context.stopService(myService);
        }
    }
}