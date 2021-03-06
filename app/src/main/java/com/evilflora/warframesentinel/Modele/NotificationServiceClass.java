package com.evilflora.warframesentinel.Modele;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

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
            /*try {

                if (MenuActivity.getWarframeWorldState() != null) {
                    warframeWorldState = MenuActivity.getWarframeWorldState();
                } else {
                    warframeWorldState = new WarframeWorldState(new AppSettings(context).getPlatformCode());
                }

                // RemoteViews bigView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_content_big);
                // bigView.setTextViewText(R.id.notif_app_name,getString(R.string.app_name));
                // bigView.setTextViewText(R.id.notif_date, "date");

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
            }*/
            hReloadWarframeWorldState.postDelayed(this, 60000);
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
