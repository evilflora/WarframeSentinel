package com.evilflora.warframesentinel.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.evilflora.warframesentinel.Modele.BountiesClass;
import com.evilflora.warframesentinel.Modele.ProjectConstructionClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.ProjectConstructionView;
import com.evilflora.warframesentinel.Vue.WorldCyclesView;

import org.json.JSONArray;

import java.util.Arrays;

public class EventFragment extends Fragment {

    final String CurrentFileName = "EventFragment";
    ProjectConstructionClass _projectPct;
    BountiesClass _cetusDayNight;
    View v_ProjectConstruction;
    View v_WorldCycle;
    Handler hTimerConstructionStatus = new Handler();
    Handler hTimerWorldCycle = new Handler();
    LinearLayout event_contrusction_project ;
    LinearLayout event_world_cycle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.events));

        View view = inflater.inflate(R.layout.event_content, container, false);
        event_contrusction_project = view.findViewById(R.id.LinearLayout_construction_project);
        event_world_cycle = view.findViewById(R.id.LinearLayout_world_cycle);

        try {
            JSONArray _ProjectPct = MenuActivity.warframeWorldState.getProjectPct();
            JSONArray _CetusDayNight = MenuActivity.warframeWorldState.getCetusMissions();

            _projectPct = new ProjectConstructionClass(_ProjectPct);
            _cetusDayNight = new BountiesClass(getActivity(),_CetusDayNight, Arrays.asList("Day", "Night", "Indeterminate"));

            v_ProjectConstruction = new ProjectConstructionView(getContext(), _projectPct).getView(0,null,event_contrusction_project);
            v_WorldCycle = new WorldCyclesView(getContext(), _cetusDayNight).getView(0,null,event_world_cycle);

            event_contrusction_project.addView(v_ProjectConstruction);
            event_world_cycle.addView(v_WorldCycle);

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
                // todo  we need to refresh progressbar and value
            } catch (Exception ex){
                Log.e(CurrentFileName,"Cannot update sortie timer | " + ex.getMessage());
            }
            hTimerConstructionStatus.postDelayed(this, 5 * 60 * 1000);
        }
    };

    private Runnable runnableWorldCycle = new Runnable() {
        @Override
        public void run() {
            // todo we neeed to refresh timers
            hTimerWorldCycle.postDelayed(this, 1000);
        }
    };

}
