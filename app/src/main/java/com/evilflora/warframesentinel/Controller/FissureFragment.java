package com.evilflora.warframesentinel.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import com.evilflora.warframesentinel.Modele.FissureClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.FissureListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FissureFragment extends Fragment {

    final String CurrentFileName = "FissureFragment";
    List<List<FissureClass>> fissureList = new ArrayList<>();
    List<FissureListView> adapterFissureList;
    JSONArray fissures;
    List<String> _tabHostContent;
    Handler hTimerFissure = new Handler();
    Handler hReloadFissure = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fissure_content, container, false);
        fissureList.clear();
        getActivity().setTitle(getString(R.string.fissures));

        // Tabs
        _tabHostContent = Arrays.asList("all", "VoidT1", "VoidT2" , "VoidT3", "VoidT4");
        int[] tab = {R.id.fissures_all, R.id.fissures_lith,R.id.fissures_meso,R.id.fissures_neo,R.id.fissures_axi};
        int[] tabContent = {R.id.listView_fissure_all,R.id.listView_fissure_lith,R.id.listView_fissure_meso,R.id.listView_fissure_neo,R.id.listView_fissure_axi};
        TabHost tabHost = view.findViewById(R.id.tabHost_fissures);
        tabHost.setup();
        TabHost.TabSpec spec;
        for (int i = 0; i < _tabHostContent.size(); i++) {
            spec = tabHost.newTabSpec(_tabHostContent.get(i));
            spec.setContent(tab[i]);
            spec.setIndicator(getResources().getString(getResources().getIdentifier(_tabHostContent.get(i), "string", getActivity().getPackageName())));
            tabHost.addTab(spec);
        }
        // End tabs

        // Adapter
        adapterFissureList = new ArrayList<>(_tabHostContent.size()); // we created a list to fit the size of the number of types of fissure plus the category all
        List<ListView> listView = new ArrayList<>(_tabHostContent.size());
        fissures = MenuActivity.warframeWorldState.getFissures();
        for (int i = 0; i < _tabHostContent.size(); i++) {
                fissureList.add(new ArrayList<>());
                adapterFissureList.add(i,new FissureListView(getActivity(), fissureList.get(i)));
                listView.add(i, view.findViewById(tabContent[i]));
                listView.get(i).setAdapter(adapterFissureList.get(i));
        }
        // end adapter

        // Data load
        for(int i = 0; i < fissures.length(); i++){
            try {
                FissureClass tmp = new FissureClass(getActivity(),fissures.getJSONObject(i));
                fissureList.get(0).add(tmp); // add fissure to category 'all'
                fissureList.get(_tabHostContent.indexOf(tmp.get_modifier())).add(tmp); // add fissure to it's good category
            } catch (JSONException e) {
                Log.e(CurrentFileName,"Cannot add new fissure - " + e.getMessage());
            }
        }
        // End data load

        hTimerFissure.post(runnableFissure);
        hReloadFissure.post(runnableReloadFissure);

        return view;
    }

    private Runnable runnableFissure = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < fissureList.size();i++) {
                for(int j = 0; j < fissureList.get(i).size(); j++) {
                    if (fissureList.get(i).get(j).is_end_of_fissure()) {
                        fissureList.get(i).remove(j);
                    }
                }
            }

            for (int i = 0; i < _tabHostContent.size(); i++) {
                adapterFissureList.get(i).notifyDataSetChanged();
            }
            hTimerFissure.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadFissure = new Runnable() {
        @Override
        public void run() {
            try {
                fissures = MenuActivity.warframeWorldState.getFissures();

                boolean stop;
                for (int i = 0; i < fissures.length(); i++) { // we go through the new list (probably bigger than the old one)
                    stop = false; // on remet à false
                    for(int j = 0; j < fissureList.get(0).size(); j++) { // we compare to the old list
                        if(fissures.getJSONObject(i).getJSONObject("_id").getString("$oid").compareTo(fissureList.get(0).get(j).get_id()) == 0) {
                            stop = true; // we indicate that we have found one
                            break; // we break the loop because it is useless to continue
                        }
                    }
                    if (!stop) { // if we did not leave the loop then it's because this alert is new
                        FissureClass tmp = new FissureClass(getActivity(),fissures.getJSONObject(i));
                        fissureList.get(0).add(tmp); // add fissure to category 'all'
                        fissureList.get(_tabHostContent.indexOf(tmp.get_modifier())).add(tmp); // add fissure to it's good category
                    }
                    Collections.sort(fissureList.get(0),(o1, o2) -> o1.get_modifier().compareTo(o2.get_modifier()) );
                }
            } catch (Exception ex) {
                Log.e(CurrentFileName,"Cannot add new fissure - " + ex.getMessage());
            }
            hReloadFissure.postDelayed(this, 60 * 1000);
        }
    };
}
