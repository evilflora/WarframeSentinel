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

import com.evilflora.warframesentinel.Modele.SyndicateClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.SyndicateListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyndicateFragment extends Fragment {

    private static String _currentFileName = "SyndicateFragment";
    private List<List<SyndicateClass>> _syndicateList = new ArrayList<>();
    private List<SyndicateListView> _adapterSyndicateList;
    private JSONArray _syndicate;
    private List<String> _tabHostContent;
    private Handler _hTimerSyndicate = new Handler();
    private Handler _hReloadSyndicate = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.syndicate_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.syndicate));

        // Tabs
        _tabHostContent = Arrays.asList("all", "SteelMeridianSyndicate", "ArbitersSyndicate" , "CephalonSudaSyndicate", "PerrinSyndicate", "RedVeilSyndicate", "NewLokaSyndicate");
        int[] idLinear = {R.id.syndicate_all, R.id.syndicate_steel_meridian,R.id.syndicate_arbiter_of_hexis,R.id.syndicate_cephalon_suda,R.id.syndicate_perrin_sequence,R.id.syndicate_red_veil,R.id.syndicate_new_loka};
        int[] idListView = {R.id.listView_all,R.id.listView_steel_meridian,R.id.listView_arbiter_of_hexis, R.id.listViewcephalon_suda,  R.id.listView_perrin_sequence, R.id.listView_red_veil, R.id.listView_new_loka};
        TabHost tabHost = view.findViewById(R.id.tabHost_syndicate);
        tabHost.setup();
        TabHost.TabSpec spec;
        for (int i = 0; i < _tabHostContent.size(); i++) {
            spec = tabHost.newTabSpec(_tabHostContent.get(i));
            spec.setContent(idLinear[i]);
            spec.setIndicator(getResources().getString(getResources().getIdentifier(_tabHostContent.get(i), "string", getActivity().getPackageName())));
            tabHost.addTab(spec);
        }
        // End tabs

        // Adapter
        _adapterSyndicateList = new ArrayList<>(_tabHostContent.size()); // we created a list to fit the size of the number syndicates plus the category all
        List<ListView> listView = new ArrayList<>(_tabHostContent.size());
        _syndicate = MenuActivity.getWarframeWorldState().getShipSyndicateMissions();
        for (int i = 0; i < _tabHostContent.size(); i++) {
            _syndicateList.add(new ArrayList<>());
            _adapterSyndicateList.add(i,new SyndicateListView(getActivity(), _syndicateList.get(i)));
            listView.add(i, view.findViewById(idListView[i]));
            listView.get(i).setAdapter(_adapterSyndicateList.get(i));
        }
        // end adapter

        // Handlers
        _hTimerSyndicate.post(runnableSyndicate);
        _hReloadSyndicate.post(runnableReloadSyndicate);
        // end handlers

        return view;
    }

    private Runnable runnableSyndicate = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < _syndicateList.size();i++) {
                for(int j = 0; j < _syndicateList.get(i).size(); j++) {
                    if (_syndicateList.get(i).get(j).isEnd()) {
                        _syndicateList.get(i).remove(j);
                    }
                }
            }

            for (int i = 0; i < _tabHostContent.size(); i++) {
                if(_adapterSyndicateList.get(i).getCount() > 0) _adapterSyndicateList.get(i).notifyDataSetChanged();
            }
            _hTimerSyndicate.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadSyndicate = new Runnable() {
        @Override
        public void run() {
            try {
                _syndicate = MenuActivity.getWarframeWorldState().getShipSyndicateMissions();
                boolean stop;
                for (int i = 0; i < _syndicate.length(); i++) { // we go through the new list (probably bigger than the old one)
                    stop = false;
                    for(int j = 0; j < _syndicateList.get(0).size(); j++) { // we compare to the old list
                        if(_syndicate.getJSONObject(i).getJSONObject("_id").getString("$oid").compareTo(_syndicateList.get(0).get(j).getId()) == 0) {
                            stop = true; // we indicate that we have found one
                            break; // we break the loop because it is useless to continue
                        }
                    }
                    if (!stop) { // if we did not leave the loop then it's because this alert is new
                        SyndicateClass tmp = new SyndicateClass(getActivity(), _syndicate.getJSONObject(i));
                        for(int j = 0; j < tmp.getNodeSize(); j++) {
                            _syndicateList.get(0).add(tmp); // add syndicate mission to category 'all'
                            _syndicateList.get(_tabHostContent.indexOf(tmp.getTag())).add(tmp); // add syndicate mission to it's good category
                        }
                    }
                }
                for(int j = 0; j < _tabHostContent.size(); j++)
                {
                    if (_adapterSyndicateList.get(j).getCount() > 0) _adapterSyndicateList.get(j).notifyDataSetChanged(); // we update the view
                }
            } catch (Exception ex) {
                Log.e(_currentFileName,"Cannot add new syndicate | " + ex.getMessage());
            }
            _hReloadSyndicate.postDelayed(this, 60000);
        }
    };
}
