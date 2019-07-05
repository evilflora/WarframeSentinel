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
import android.widget.TabHost;

import com.evilflora.warframesentinel.Modele.FissureClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.FissureListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FissureFragment extends Fragment {

    private static String _currentFileName = "FissureFragment";
    private List<List<FissureClass>> _fissureList = new ArrayList<>();
    private List<FissureListView> _adapterFissureList;
    private List<String> _tabHostContent;
    private Handler _hTimerFissure = new Handler();
    private Handler _hReloadFissure = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fissure_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.fissures));

        // Tabs
        int[] tab = {R.id.fissures_all, R.id.fissures_lith,R.id.fissures_meso,R.id.fissures_neo,R.id.fissures_axi};
        int[] tabContent = {R.id.listView_fissure_all, R.id.listView_fissure_lith, R.id.listView_fissure_meso, R.id.listView_fissure_neo, R.id.listView_fissure_axi};
        _tabHostContent = Arrays.asList("all", "VoidT1", "VoidT2" , "VoidT3", "VoidT4");
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
        _adapterFissureList = new ArrayList<>(_tabHostContent.size()); // we created a list to fit the size of the number of types of fissure plus the category all
        List<ListView> listView = new ArrayList<>(_tabHostContent.size());
        for (int i = 0; i < _tabHostContent.size(); i++) {
                _fissureList.add(new ArrayList<>());
                _adapterFissureList.add(i,new FissureListView(getActivity(), _fissureList.get(i)));
                listView.add(i, view.findViewById(tabContent[i]));
                listView.get(i).setAdapter(_adapterFissureList.get(i));
        }
        // end adapter

        // Handler
        _hTimerFissure.post(runnableFissure);
        _hReloadFissure.post(runnableReloadFissure);
        // end handlers

        return view;
    }

    private Runnable runnableFissure = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < _fissureList.size();i++) {
                for(int j = 0; j < _fissureList.get(i).size(); j++) {
                    if (_fissureList.get(i).get(j).isEndOfFissure()) {
                        _fissureList.get(i).remove(j);
                    }
                }
            }

            for (int i = 0; i < _tabHostContent.size(); i++) {
                if(_adapterFissureList.get(i).getCount() > 0) _adapterFissureList.get(i).notifyDataSetChanged();
            }
            _hTimerFissure.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadFissure = new Runnable() {
        @Override
        public void run() {
            try {
                JSONArray fissures = MenuActivity.getWarframeWorldState().getFissures();

                boolean stop;
                for (int i = 0; i < fissures.length(); i++) { // we go through the new list (probably bigger than the old one)
                    stop = false;
                    for(int j = 0; j < _fissureList.get(0).size(); j++) { // we compare to the old list
                        if(fissures.getJSONObject(i).getJSONObject("_id").getString("$oid").compareTo(_fissureList.get(0).get(j).getId()) == 0) {
                            stop = true; // we indicate that we have found one
                            break; // we break the loop because it is useless to continue
                        }
                    }
                    if (!stop) { // if we did not leave the loop then it's because this alert is new
                        FissureClass tmp = new FissureClass(getActivity(), fissures.getJSONObject(i));
                        _fissureList.get(0).add(tmp); // add fissure to category 'all'
                        _fissureList.get(_tabHostContent.indexOf(tmp.getModifier())).add(tmp); // add fissure to it's good category
                    }

                }

                for(int j = 0; j < _tabHostContent.size(); j++)
                {
                    if (_adapterFissureList.get(j).getCount() >0) _adapterFissureList.get(j).notifyDataSetChanged(); // we update the view
                    Collections.sort(_fissureList.get(j),(o1, o2) -> o1.getModifier().compareTo(o2.getModifier()) | Long.compare(o1.getTimeLeft(), o2.getTimeLeft())); // Filter by fissure type and by time left
                }
            } catch (Exception ex) {
                Log.e(_currentFileName,"Cannot add new fissure - " + ex.getMessage());
            }
            _hReloadFissure.postDelayed(this, 60000);
        }
    };

}