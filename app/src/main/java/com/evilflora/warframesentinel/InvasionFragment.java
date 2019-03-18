package com.evilflora.warframesentinel;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class InvasionFragment extends Fragment {

    final String CurrentFileName = "InvasionFragment";
    List<InvasionClass> invasionListCurrent = new ArrayList<>(); // Liste des invasions
    List<InvasionClass> invasionListCompleted = new ArrayList<>(); // Liste des invasions
    Handler hTimerInvasionCurrent = new Handler();
    InvasionListView adapterCurrent;
    InvasionListView adapterCompleted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.invasions_content, container, false);
        invasionListCurrent.clear();
        invasionListCompleted.clear();
        getActivity().setTitle(getString(R.string.invasions));

        TabHost tabHost = view.findViewById(R.id.tabHost_invasions);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.current));
        spec.setContent(R.id.invasions_current);
        spec.setIndicator(getString(R.string.current));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getString(R.string.completed));
        spec.setContent(R.id.invasions_completed);
        spec.setIndicator(getString(R.string.completed));
        tabHost.addTab(spec);

        ListView listViewCurrent = view.findViewById(R.id.list_invasion_current);
        ListView listViewCompleted = view.findViewById(R.id.list_invasion_completed);
        adapterCurrent = new InvasionListView(getActivity(), invasionListCurrent);
        adapterCompleted = new InvasionListView(getActivity(), invasionListCompleted);
        listViewCurrent.setAdapter(adapterCurrent);
        listViewCompleted.setAdapter(adapterCompleted);

        hTimerInvasionCurrent.post(runnableLoadInvasionCurrent); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableLoadInvasionCurrent = new Runnable() {
        @Override
        public void run() {
            load();
            hTimerInvasionCurrent.postDelayed(this, 60 * 1000 * 5);
        }
    };

    void load() {
        try {
            JSONArray invasions = MenuActivity.warframeWorldState.getInvasions();
            invasionListCurrent.clear();
            invasionListCompleted.clear();

            for (int i = 0; i < invasions.length(); i++) {
                InvasionClass tmpInvasion = new InvasionClass(invasions.getJSONObject(i));
                if (!tmpInvasion.get_completed()) {
                    Log.i(CurrentFileName,"Added new current invasion id: " + tmpInvasion.get_id());
                    invasionListCurrent.add(tmpInvasion); // Instancie l'alerte et l'ajoute dans la liste
                }else {
                    Log.i(CurrentFileName,"Added new completed invasion id: " + tmpInvasion.get_id());
                    invasionListCompleted.add(tmpInvasion); // Instancie l'alerte et l'ajoute dans la liste
                }
            }

            if(adapterCurrent.getCount() > 0)
                adapterCurrent.notifyDataSetChanged();
            if(adapterCompleted.getCount() > 0)
                adapterCompleted.notifyDataSetChanged();


        } catch (Exception ex){
            Log.e(CurrentFileName,"Cannot read invasion | " + ex.getMessage());
        }
    }
}
