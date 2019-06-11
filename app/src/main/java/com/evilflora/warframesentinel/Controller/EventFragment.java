package com.evilflora.warframesentinel.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.evilflora.warframesentinel.Modele.AlertClass;
import com.evilflora.warframesentinel.Modele.BountiesClass;
import com.evilflora.warframesentinel.Modele.ProjectConstructionClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.AlertListView;
import com.evilflora.warframesentinel.Vue.ProjectConstructionView;
import com.evilflora.warframesentinel.Vue.WorldCyclesView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EventFragment extends Fragment {

    private static String _currentFileName = "EventFragment";
    ProjectConstructionClass _projectPct;
    BountiesClass _cetus;
    BountiesClass _orbVallis;
    List<AlertClass> _alerts = new ArrayList<>();
    Handler hTimerConstructionStatus = new Handler();
    Handler hTimerWorldCycle = new Handler();
    Handler hTimerAlerts = new Handler();
    Handler hReloadAlerts = new Handler();
    WorldCyclesView adapterWorldCycles;
    ProjectConstructionView adapterProjectConstruction;
    AlertListView adaterAlert;
    List<ListView> listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.events));

        View view = inflater.inflate(R.layout.event_content, container, false);

        int[] tabContent = {R.id.ListView_alert, R.id.ListView_construction_project, R.id.ListView_world_cycle};

        listView = new ArrayList<>(tabContent.length);

        try {
            JSONArray alerts = MenuActivity.warframeWorldState.getAlerts();

            //_alerts = new AlertClass(alerts);

            adaterAlert = new AlertListView(getActivity(), _alerts);

            listView.add(0, view.findViewById(tabContent[0]));
            listView.get(0).setAdapter(adaterAlert);

            hReloadAlerts.post(runnableReloadAlerts); // On rafraichis toutes les secondes les timers
            hTimerAlerts.post(runnableAlerts); // On rafraichis toutes les secondes les timers

        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot read alerts - " + ex.getMessage());
        }
        try {
            JSONArray project = MenuActivity.warframeWorldState.getProjectPct();

            _projectPct = new ProjectConstructionClass(project);

            adapterProjectConstruction = new ProjectConstructionView(getActivity(), _projectPct);

            listView.add(1, view.findViewById(tabContent[1]));
            listView.get(1).setAdapter(adapterProjectConstruction);

        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot read construction project - " + ex.getMessage());
        }

        try {
            JSONArray cetus = MenuActivity.warframeWorldState.getCetusMissions();
            JSONArray orbVallis = MenuActivity.warframeWorldState.getOrbVallisMissions();

            _cetus = new BountiesClass(getActivity(), cetus,50, Arrays.asList("day", "night", "indeterminate"));
            _orbVallis = new BountiesClass(getActivity(), orbVallis,20, Arrays.asList("warm", "cold", "indeterminate"));

            adapterWorldCycles = new WorldCyclesView(getActivity(), Arrays.asList(_cetus,_orbVallis));

            listView.add(2, view.findViewById(tabContent[2]));
            listView.get(2).setAdapter(adapterWorldCycles);


        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot read world status - " + ex.getMessage());
        }

        hTimerConstructionStatus.post(runnableConstructionStatus); // On rafraichis toutes les secondes les timers
        hTimerWorldCycle.post(runnableWorldCycle); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableConstructionStatus = new Runnable() {
        @Override
        public void run() {
            try {
                adapterProjectConstruction.notifyDataSetChanged();
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot update sortie timer | " + ex.getMessage());
            }
            hTimerConstructionStatus.postDelayed(this, 300000); // 5 * 60 * 10000
        }
    };

    private Runnable runnableWorldCycle = new Runnable() {
        @Override
        public void run() {
            adapterWorldCycles.notifyDataSetChanged();
            hTimerWorldCycle.postDelayed(this, 1000);
        }
    };

    private Runnable runnableAlerts = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < _alerts.size();i++) {
                if (_alerts.get(i).isEndOfAlert()) {
                    _alerts.remove(i);
                }
            }
            try {
                if(adaterAlert.getCount() > 0)  adaterAlert.notifyDataSetChanged();
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot update alert timer | " + ex.getMessage());
            }
            hTimerAlerts.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadAlerts = new Runnable() {
        @Override
        public void run() {
            load();
            hReloadAlerts.postDelayed(this, 60000);
        }
    };

    void load() {
        try {
            JSONArray alertsUpdate = MenuActivity.warframeWorldState.getAlerts(); // on récupère la liste des alertes

            if (alertsUpdate.length() > _alerts.size()) {
                boolean stop;
                for (int i = 0; i < alertsUpdate.length(); i++) { // on parcours la nouvelle liste (surement plus grande que l'ancienne)
                    stop = false; // on remet à false
                    AlertClass alert = new AlertClass(getActivity(), alertsUpdate.getJSONObject(i));
                    for(int j = 0; j < _alerts.size(); j++) { // on compare à l'ancienne liste
                        if(alert.getId().compareTo(_alerts.get(j).getId()) == 0) {
                            stop = true; // on indique que l'on en a trouvé une
                            break; // on casse la boucle car inutile de continuer
                        }
                    }
                    if (!stop && !alert.isEndOfAlert()) { // si on n'a pas quitté la boucle alors c'est que cette alerte est nouvelle
                        Log.i(_currentFileName,"Added new alert id: " + alert.getId());
                        _alerts.add(alert); // on l'ajoute à la liste
                        Collections.sort(_alerts,(o1, o2) -> Long.compare(o1.getTimeLeft(),o2.getTimeLeft()));
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot add new alert | " + ex.getMessage());
        }
    }

}
