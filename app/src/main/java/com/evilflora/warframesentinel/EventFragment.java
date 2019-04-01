package com.evilflora.warframesentinel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;

public class EventFragment extends Fragment {

    final String CurrentFileName = "EventFragment";
    ProjectConstructionClass _projectPct;
    BountiesClass _cetusDayNight;
    View v_ProjectConstruction;
    View v_WorldCycle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.alerts));

        View view = inflater.inflate(R.layout.event_content, container, false);
        LinearLayout event = view.findViewById(R.id.LinearLayour_events);


        try {
            JSONArray _ProjectPct = MenuActivity.warframeWorldState.getProjectPct();
            JSONArray _CetusDayNight = MenuActivity.warframeWorldState.getCetusMissions();

            _projectPct = new ProjectConstructionClass(_ProjectPct);
            _cetusDayNight = new BountiesClass(_CetusDayNight);

            v_ProjectConstruction = new ProjectConstructionView(getContext(), _projectPct).getView(0,null,event);
            v_WorldCycle = new WorldCyclesView(getContext(), _cetusDayNight).getView(0,null,event);

            event.addView(v_ProjectConstruction);
            event.addView(v_WorldCycle);

        } catch (Exception ex) {
            Log.e(CurrentFileName,"Cannot add events | " + ex.getMessage());
        }

        return view;
    }


}
