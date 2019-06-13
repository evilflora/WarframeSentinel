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
import com.evilflora.warframesentinel.Modele.ProjectConstructionClass;
import com.evilflora.warframesentinel.Modele.WorldCycleClass;
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
    private List<AlertClass> _alerts = new ArrayList<>();
    private Handler _hTimerConstructionStatus = new Handler();
    private Handler _hTimerWorldCycle = new Handler();
    private Handler _hTimerAlerts = new Handler();
    private Handler _hReloadAlerts = new Handler();
    private WorldCyclesView _adapterWorldCycles;
    private ProjectConstructionView _adapterProjectConstruction;
    private AlertListView _adapterAlert;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.events));

        int[] tabContent = {R.id.ListView_alert, R.id.ListView_construction_project, R.id.ListView_world_cycle};
        List<ListView> listView = new ArrayList<>(tabContent.length);

        try {
            _adapterAlert = new AlertListView(getActivity(), _alerts);

            listView.add(0, view.findViewById(tabContent[0]));
            listView.get(0).setAdapter(_adapterAlert);

            _hReloadAlerts.post(runnableReloadAlerts); // On rafraichis toutes les secondes les timers
            _hTimerAlerts.post(runnableAlerts); // On rafraichis toutes les secondes les timers

        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot read alerts - " + ex.getMessage());
        }

        try {
            JSONArray project = MenuActivity.warframeWorldState.getProjectPct();

            _adapterProjectConstruction = new ProjectConstructionView(getActivity(), new ProjectConstructionClass(project));

            listView.add(1, view.findViewById(tabContent[1]));
            listView.get(1).setAdapter(_adapterProjectConstruction);

        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot read construction project - " + ex.getMessage());
        }

        try {
            JSONArray cetus = MenuActivity.warframeWorldState.getCetusMissions();
            JSONArray orbVallis = MenuActivity.warframeWorldState.getOrbVallisMissions();


            _adapterWorldCycles = new WorldCyclesView(getActivity(), Arrays.asList(new WorldCycleClass(getActivity(), cetus, 50, "cetus_cycle"),
                                                                                   new WorldCycleClass(getActivity(), orbVallis, 50, "orb_vallis_cycle")));

            listView.add(2, view.findViewById(tabContent[2]));
            listView.get(2).setAdapter(_adapterWorldCycles);


        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot read world status - " + ex.getMessage());
        }

        _hTimerConstructionStatus.post(runnableConstructionStatus); // On rafraichis toutes les secondes les timers
        _hTimerWorldCycle.post(runnableWorldCycle); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableConstructionStatus = new Runnable() {
        @Override
        public void run() {
            if (_adapterProjectConstruction != null) {
                if(_adapterProjectConstruction.getCount() > 0) _adapterProjectConstruction.notifyDataSetChanged();
            } else {
                Log.e(_currentFileName,"Cannot update construction project ");
            }
            _hTimerConstructionStatus.postDelayed(this, 300000); // 5 * 60 * 10000
        }
    };

    private Runnable runnableWorldCycle = new Runnable() {
        @Override
        public void run() {
            if (_adapterWorldCycles != null) {
                if(_adapterWorldCycles.getCount() > 0) _adapterWorldCycles.notifyDataSetChanged();
            } else {
                Log.e(_currentFileName,"Cannot update nodes cycle ");
            }
            _hTimerWorldCycle.postDelayed(this, 1000);
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
            if (_adapterAlert != null) {
                if(_adapterAlert.getCount() > 0) _adapterAlert.notifyDataSetChanged();
            } else {
                Log.e(_currentFileName,"Cannot update alertes ");
            }
            _hTimerAlerts.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadAlerts = new Runnable() {
        @Override
        public void run() {
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
            _hReloadAlerts.postDelayed(this, 60000);
        }
    };
}
