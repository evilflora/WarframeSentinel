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
import android.widget.TextView;

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
    private Handler _hTimerFissure = new Handler();
    private Handler _hReloadFissure = new Handler();
    private List<List<FissureClass>> _fissureList = new ArrayList<>();
    private List<FissureListView> _adapterFissureList;
    private List<String> _tabCode;
    private int[] _tabTitle;
    private TabHost tabHost;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fissure_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.fissures));

        // Tabs
        int[] listListView = {R.id.listView_fissure_all, R.id.listView_fissure_lith, R.id.listView_fissure_meso, R.id.listView_fissure_neo, R.id.listView_fissure_axi};
        int[] tabId = {R.id.fissures_all, R.id.fissures_lith, R.id.fissures_meso, R.id.fissures_neo, R.id.fissures_axi};
        _tabTitle = new int[]{R.string.all, R.string.VoidT1, R.string.VoidT2, R.string.VoidT3, R.string.VoidT4};
        _tabCode = Arrays.asList("all", "VoidT1", "VoidT2" , "VoidT3", "VoidT4");
        tabHost = view.findViewById(R.id.tabHost_fissures);
        tabHost.setup();
        TabHost.TabSpec spec;
        for (int i = 0; i < _tabCode.size(); i++) {
            spec = tabHost.newTabSpec(_tabCode.get(i));
            spec.setContent(tabId[i]);
            spec.setIndicator(getString(_tabTitle[i]));
            tabHost.addTab(spec);
        }
        // End tabs

        // Adapter
        _adapterFissureList = new ArrayList<>(_tabCode.size()); // we created a list to fit the size of the number of types of fissure plus the category all
        List<ListView> listView = new ArrayList<>(_tabCode.size());
        for (int i = 0; i < _tabCode.size(); i++) {
                _fissureList.add(new ArrayList<>());
                _adapterFissureList.add(i,new FissureListView(getActivity(), _fissureList.get(i)));
                listView.add(i, view.findViewById(listListView[i]));
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
            int fissureLength = _fissureList.get(0).size();
            for (int i = 0; i < _fissureList.size();i++) {
                for(int j = 0; j < _fissureList.get(i).size(); j++) {
                    _fissureList.get(i).removeIf(FissureClass::isEnd);
                }
            }
            if (fissureLength != _fissureList.get(0).size()) { // only update if one is deleted or added
                updateTabs();
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
                boolean update = false;
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
                        _fissureList.get(_tabCode.indexOf(tmp.getModifier())).add(tmp); // add fissure to it's good category
                        update = true;
                    }

                }

                if(update) { // it's useless to sort if no new fissures were added
                    Collections.sort(_fissureList.get(0),(o1, o2) -> { // need to have double sort
                        if (o1.getModifier().compareTo(o2.getModifier()) == 0) {
                            return Long.compare(o1.getTimeLeft(), o2.getTimeLeft());
                        }
                        return o1.getModifier().compareTo(o2.getModifier());
                    });
                    if (_adapterFissureList.get(0).getCount() > 0) _adapterFissureList.get(0).notifyDataSetChanged(); // we update the view

                    for(int j = 1; j < _tabCode.size(); j++) { // classic sort
                        Collections.sort(_fissureList.get(j),(o1, o2) -> Long.compare(o1.getTimeLeft(), o2.getTimeLeft())); // Filter by fissure type and by time left
                    }
                    updateTabs();
                }
            } catch (Exception ex) {
                Log.e(_currentFileName,"Cannot add new fissure - " + ex.getMessage());
            }
            _hReloadFissure.postDelayed(this, 60000);
        }
    };

    void updateTabs() {
        if (getActivity() != null){
            for(int i = 0; i < _tabCode.size(); i++) {
                TextView text = tabHost.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.title);
                text.setText(getResources().getString(getResources().getIdentifier("tab_name_quantity", "string", getActivity().getPackageName()), getResources().getString(_tabTitle[i]), _fissureList.get(i).size()));
                if (_adapterFissureList.get(i).getCount() > 0) _adapterFissureList.get(i).notifyDataSetChanged(); // we update the view
            }
        }
    }

}