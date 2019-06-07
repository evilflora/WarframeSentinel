package com.evilflora.warframesentinel.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.evilflora.warframesentinel.Modele.BountiesClass;
import com.evilflora.warframesentinel.Modele.ProjectConstructionClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.ProjectConstructionView;
import com.evilflora.warframesentinel.Vue.WorldCyclesView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventFragment extends Fragment {

    final String CurrentFileName = "EventFragment";
    ProjectConstructionClass _projectPct;
    BountiesClass _cetusDayNight;
    Handler hTimerConstructionStatus = new Handler();
    Handler hTimerWorldCycle = new Handler();
    WorldCyclesView adapterWorldCycles;
    ProjectConstructionView adapterProjectConstruction;
    List<ListView> listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.events));

        View view = inflater.inflate(R.layout.event_content, container, false);

        int[] tabContent = {R.id.ListView_construction_project,R.id.ListView_world_cycle};

        listView = new ArrayList<>(tabContent.length);

        try {
            JSONArray _ProjectPct = MenuActivity.warframeWorldState.getProjectPct();
            JSONArray _CetusDayNight = MenuActivity.warframeWorldState.getCetusMissions();

            _projectPct = new ProjectConstructionClass(_ProjectPct);
            _cetusDayNight = new BountiesClass(getActivity(),_CetusDayNight, Arrays.asList("Day", "Night", "Indeterminate"));

            adapterProjectConstruction = new ProjectConstructionView(getActivity(), _projectPct);
            adapterWorldCycles = new WorldCyclesView(getActivity(), _cetusDayNight);

            for (int i = 0; i < tabContent.length; i++) {
                listView.add(i, view.findViewById(tabContent[i]));
            }

            listView.get(0).setAdapter(adapterProjectConstruction);
            listView.get(1).setAdapter(adapterWorldCycles);


        } catch (Exception ex) {
            Log.e(CurrentFileName,"Cannot read events - " + ex.getMessage());
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
                Log.e(CurrentFileName,"Cannot update sortie timer | " + ex.getMessage());
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

}
