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
import org.json.JSONException;

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

            _hReloadAlerts.post(runnableReloadAlerts);
            _hTimerAlerts.post(runnableAlerts);

        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot read alerts - " + ex.getMessage());
        }

        try {
            JSONArray project = MenuActivity.getWarframeWorldState().getProjectPct();

            _adapterProjectConstruction = new ProjectConstructionView(getActivity(), new ProjectConstructionClass(getActivity(), project));

            listView.add(1, view.findViewById(tabContent[1]));
            listView.get(1).setAdapter(_adapterProjectConstruction);

        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot read construction project - " + ex.getMessage());
        }

        try {
            JSONArray cetus = MenuActivity.getWarframeWorldState().getCetusMissions();
            JSONArray orbVallis = MenuActivity.getWarframeWorldState().getOrbVallisMissions();

            _adapterWorldCycles = new WorldCyclesView(getActivity(), Arrays.asList(new WorldCycleClass(getActivity(), cetus, 50, "cetus_cycle"),
                                                                                   new WorldCycleClass(getActivity(), orbVallis, 50, "orb_vallis_cycle")));

            listView.add(2, view.findViewById(tabContent[2]));
            listView.get(2).setAdapter(_adapterWorldCycles);


        } catch (Exception ex) {
            Log.e(_currentFileName,"Cannot read world status - " + ex.getMessage());
        }

        _hTimerConstructionStatus.post(runnableConstructionStatus);
        _hTimerWorldCycle.post(runnableWorldCycle);

        return view;
    }

    private Runnable runnableConstructionStatus = new Runnable() {
        @Override
        public void run() {
            if (_adapterProjectConstruction != null) {
                if(_adapterProjectConstruction.getCount() > 0) _adapterProjectConstruction.notifyDataSetChanged();
            } else {
                Log.e(_currentFileName,"Cannot update Construction Project ");
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
                Log.e(_currentFileName,"Cannot update World Cycles");
            }
            _hTimerWorldCycle.postDelayed(this, 1000);
        }
    };

    private Runnable runnableAlerts = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < _alerts.size();i++) {
                if (_alerts.get(i).isEnd()) {
                    _alerts.remove(i);
                }
            }
            if (_adapterAlert != null) {
                if(_adapterAlert.getCount() > 0) _adapterAlert.notifyDataSetChanged();
            } else {
                Log.e(_currentFileName,"Cannot update Alerts ");
            }
            _hTimerAlerts.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadAlerts = new Runnable() {
        @Override
        public void run() {
            try {
                JSONArray alertsUpdate = MenuActivity.getWarframeWorldState().getAlerts();

                if (alertsUpdate.length() > _alerts.size()) {
                    boolean stop;
                    for (int i = 0; i < alertsUpdate.length(); i++) {
                        stop = false; // on remet à false
                        AlertClass alert = new AlertClass(getActivity(), alertsUpdate.getJSONObject(i));
                        for(int j = 0; j < _alerts.size(); j++) {
                            if(alert.getId().compareTo(_alerts.get(j).getId()) == 0) {
                                stop = true;
                                break;
                            }
                        }
                        if (!stop && !alert.isEnd()) {
                            Log.i(_currentFileName,"Added new alert id: " + alert.getId());
                            _alerts.add(alert);
                            Collections.sort(_alerts,(o1, o2) -> Long.compare(o1.getTimeLeft(),o2.getTimeLeft()));
                        }
                    }
                }
            }catch (JSONException json) {
                Log.e(_currentFileName,"Cannot get JSONObject from alertsUpdate : " + json.getMessage());
            } catch (Exception ex) {
                Log.e(_currentFileName,"Unknown Exception : " + ex.getMessage());
            }
            _hReloadAlerts.postDelayed(this, 60000);
        }
    };
}
