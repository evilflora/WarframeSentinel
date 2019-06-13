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

import com.evilflora.warframesentinel.Modele.PvpChallengeClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.PvpChallengeListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PvpChallengeFragment extends Fragment {

    private static String _currentFileName = "PvpChallengeFragment";
    private List<List<PvpChallengeClass>> _pvpChallengeList = new ArrayList<>();
    private List<PvpChallengeListView> _adapterPvpChallengeList;
    private JSONArray _pvpChallenges;
    private List<String> _tabHostContent;
    private Handler _hTimerPvpChallenge = new Handler();
    private Handler _hReloadPvpChallenge = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pvp_challenge_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.pvp));

        // Tabs
        _tabHostContent = Arrays.asList("all", "PVPChallengeTypeCategory_DAILY", "PVPChallengeTypeCategory_WEEKLY");
        int[] idLinear = {R.id.pvp_challenge_all, R.id.pvp_challenge_daily,R.id.pvp_challenge_weekly};
        int[] idListView = {R.id.listView_pvp_challenge_all,R.id.listView_pvp_challenge_daily,R.id.listView_pvp_challenge_weekly};
        TabHost tabHost = view.findViewById(R.id.tabHost_pvp_challenge);
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
        _adapterPvpChallengeList = new ArrayList<>(_tabHostContent.size()); // we created a list to fit the size of the number of types of pvpChallenge plus the category all
        List<ListView> listView = new ArrayList<>(_tabHostContent.size());
        _pvpChallenges = MenuActivity.warframeWorldState.getPvpChallengeInstances();
        for (int i = 0; i < _tabHostContent.size(); i++) {
            _pvpChallengeList.add(new ArrayList<>());
            _adapterPvpChallengeList.add(i,new PvpChallengeListView(getActivity(), _pvpChallengeList.get(i)));
            listView.add(i, view.findViewById(idListView[i]));
            listView.get(i).setAdapter(_adapterPvpChallengeList.get(i));
        }
        // end adapter

        // Handlers
        _hTimerPvpChallenge.post(runnablePvpChallenge);
        _hReloadPvpChallenge.post(runnableReloadPvpChallenge);
        // end handlers

        return view;
    }

    private Runnable runnablePvpChallenge = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < _pvpChallengeList.size();i++) {
                for(int j = 0; j < _pvpChallengeList.get(i).size(); j++) {
                    if (_pvpChallengeList.get(i).get(j).isEndOfPvpChallenge()) {
                        _pvpChallengeList.get(i).remove(j);
                    }
                }
            }

            for (int i = 0; i < _tabHostContent.size(); i++) {
                if (_adapterPvpChallengeList.get(i).getCount() > 0) _adapterPvpChallengeList.get(i).notifyDataSetChanged();
            }
            _hTimerPvpChallenge.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadPvpChallenge = new Runnable() {
        @Override
        public void run() {
            try {
                _pvpChallenges = MenuActivity.warframeWorldState.getPvpChallengeInstances();

                boolean stop;
                for (int i = 0; i < _pvpChallenges.length(); i++) { // we go through the new list (probably bigger than the old one)
                    stop = false;
                    for(int j = 0; j < _pvpChallengeList.get(0).size(); j++) { // we compare to the old list
                        if(_pvpChallenges.getJSONObject(i).getJSONObject("_id").getString("$oid").compareTo(_pvpChallengeList.get(0).get(j).getId()) == 0) {
                            stop = true; // we indicate that we have found one
                            break; // we break the loop because it is useless to continue
                        }
                    }
                    if (!stop) { // if we did not leave the loop then it's because this pvp challenge is new
                        PvpChallengeClass tmp = new PvpChallengeClass(getActivity(),_pvpChallenges.getJSONObject(i));
                        if(_tabHostContent.contains(tmp.getCategoryCode())) // si il est dans notre liste, alors on l'autorise à être afficher
                        {
                            _pvpChallengeList.get(0).add(tmp); // add pvpChallenge to category 'all'
                            _pvpChallengeList.get(_tabHostContent.indexOf(tmp.getCategoryCode())).add(tmp); // add pvpChallenge to it's good category
                        }
                    }
                    Collections.sort(_pvpChallengeList.get(0),(o1, o2) -> o1.getCategoryCode().compareTo(o2.getCategoryCode()) );
                }
            } catch (Exception ex) {
                Log.e(_currentFileName,"Cannot add new pvpChallenge - " + ex.getMessage());
            }
            _hReloadPvpChallenge.postDelayed(this, 60000);
        }
    };
}