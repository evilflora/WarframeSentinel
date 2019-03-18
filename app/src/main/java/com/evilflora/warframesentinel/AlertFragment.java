package com.evilflora.warframesentinel;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlertFragment extends Fragment {

    final String CurrentFileName = "AlertFragment";
    List<AlertClass> alertList = new ArrayList<>(); // Liste des alertes
    AlertListView adapterAlerts; // La liste customisé basé sur le layout alerte_element
    Handler hTimerAlerts = new Handler();
    Handler hReloadAlerts = new Handler();
    ListView listViewAlerts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.alert_content, container, false);

        alertList.clear();
        getActivity().setTitle(getString(R.string.alerts));

        listViewAlerts = view.findViewById(R.id.listView_alerts);
        adapterAlerts = new AlertListView(getActivity(), alertList);
        listViewAlerts.setAdapter(adapterAlerts);

        hReloadAlerts.post(runnableReloadAlerts); // On rafraichis toutes les secondes les timers
        hTimerAlerts.post(runnableAlerts); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableAlerts = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < alertList.size();i++) {
                if (alertList.get(i).end_of_alert()) {
                    alertList.remove(i);
                }
            }
            try {
                if(adapterAlerts.getCount() > 0)
                    adapterAlerts.notifyDataSetChanged();
            } catch (Exception ex){
                Log.e(CurrentFileName,"Cannot update alert timer | " + ex.getMessage());
            }
            hTimerAlerts.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadAlerts = new Runnable() {
        @Override
        public void run() {
            load();
            hReloadAlerts.postDelayed(this, 60 * 1000);
        }
    };

    void load() {
        try {
            JSONArray alerts_update = MenuActivity.warframeWorldState.getAlerts(); // on récupère la liste des alertes

            if (alerts_update.length() > alertList.size()) {
                boolean stop;
                for (int i = 0; i < alerts_update.length(); i++) { // on parcours la nouvelle liste (surement plus grande que l'ancienne)
                    stop = false; // on remet à false
                    AlertClass alert_tmp = new AlertClass(alerts_update.getJSONObject(i));
                    for(int j = 0; j < alertList.size(); j++) { // on compare à l'ancienne liste
                        if(alert_tmp.get_id().compareTo(alertList.get(j).get_id()) == 0) {
                            stop = true; // on indique que l'on en a trouvé une
                            break; // on casse la boucle car inutile de continuer
                        }
                    }
                    if (!stop && !alert_tmp.end_of_alert()) { // si on n'a pas quitté la boucle alors c'est que cette alerte est nouvelle
                        Log.i(CurrentFileName,"Added new alert id: " + alert_tmp.get_id());
                        alertList.add(alert_tmp); // on l'ajoute à la liste
                        Collections.sort(alertList,(o1, o2) -> Long.compare(o1.get_time_before_end_of_alert(),o2.get_time_before_end_of_alert()));
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(CurrentFileName,"Cannot add new alert | " + ex.getMessage());
        }
    }
}
