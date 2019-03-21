package com.evilflora.warframesentinel;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * Created by guill on 14/11/2017 for WarframeSentinel
 */

/* Add declaration of this service into the AndroidManifest.xml inside application tag*/

public class NotificationServiceClass extends Service {

    //private static final String TAG = "NotificationService";
    private short NOTIFICATION_ID = 1;
    Handler hReloadWarframeWorldState = new Handler();
    WarframeWorldState warframeWorldState;
    Context context;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        hReloadWarframeWorldState.post(runnableReloadWarframeWorldState); // On rafraichis toutes les secondes les timers
    }

    private Runnable runnableReloadWarframeWorldState = new Runnable() {
        @Override
        public void run() {
            try {

                if (MenuActivity.warframeWorldState != null) {
                    warframeWorldState = MenuActivity.warframeWorldState;
                } else {
                    warframeWorldState = new WarframeWorldState(new AppSettings(context).get_platform_code());
                }

                /*RemoteViews bigView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_content_big);
                bigView.setTextViewText(R.id.notif_app_name,getString(R.string.app_name));
                bigView.setTextViewText(R.id.notif_date, "date");*/

                final Builder builder = new Builder(context);
                    builder.setContentTitle(getString(R.string.app_name))               // titre
                        .setSmallIcon(R.mipmap.ic_launcher)                 // icone
                        .setOngoing(true)                                   // true = non closable
                        .setContentText(warframeWorldState.getAlertsLenght() + " alerts (expand to see all)")    // contenu
                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MenuActivity.class), PendingIntent.FLAG_UPDATE_CURRENT)) // lance l'activité au clic
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(warframeWorldState.getAlertsLenght() + " alerts" + "\n" +
                                         warframeWorldState.getInvasionsCurrentLenght() + " invasions" + "\n" +
                                         warframeWorldState.getFissuresLenght() + " fissures"));

                final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.notify(NOTIFICATION_ID, builder.build());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                warframeWorldState = null; // réduit 0.37 mo de ram
            }
            hReloadWarframeWorldState.postDelayed(this, 60 * 1000);
        }
    };

    @Override
    public void onDestroy() {
        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(NOTIFICATION_ID);
        }
    }
}
