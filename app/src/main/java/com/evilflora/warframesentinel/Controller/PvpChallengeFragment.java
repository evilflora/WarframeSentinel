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

import com.evilflora.warframesentinel.Modele.PvpChallengeClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.PvpChallengeListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PvpChallengeFragment extends Fragment {

    final String CurrentFileName = "PvpChallengeFragment";
    List<List<PvpChallengeClass>> pvpChallengeList = new ArrayList<>();
    List<PvpChallengeListView> adapterPvpChallengeList;
    JSONArray pvpChallenges;
    List<String> _tabHostContent;
    Handler hTimerPvpChallenge = new Handler();
    Handler hReloadPvpChallenge = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pvp_challenge_content, container, false);
        pvpChallengeList.clear();
        getActivity().setTitle(getString(R.string.pvp));

        // Tabs
        _tabHostContent = Arrays.asList("all", "PVPChallengeTypeCategory_DAILY", "PVPChallengeTypeCategory_WEEKLY");
        int[] _idLinear = {R.id.pvp_challenge_all, R.id.pvp_challenge_daily,R.id.pvp_challenge_weekly};
        int[] _idListView = {R.id.listView_pvp_challenge_all,R.id.listView_pvp_challenge_daily,R.id.listView_pvp_challenge_weekly};
        TabHost tabHost = view.findViewById(R.id.tabHost_pvp_challenge);
        tabHost.setup();
        TabHost.TabSpec spec;
        for (int i = 0; i < _tabHostContent.size(); i++) {
            spec = tabHost.newTabSpec(_tabHostContent.get(i));
            spec.setContent(_idLinear[i]);
            spec.setIndicator(getResources().getString(getResources().getIdentifier(_tabHostContent.get(i), "string", getActivity().getPackageName())));
            tabHost.addTab(spec);
        }
        // End tabs

        // Adapter
        adapterPvpChallengeList = new ArrayList<>(_tabHostContent.size()); // we created a list to fit the size of the number of types of pvpChallenge plus the category all
        List<ListView> listView = new ArrayList<>(_tabHostContent.size());
        pvpChallenges = MenuActivity.warframeWorldState.getPvpChallengeInstances();
        for (int i = 0; i < _tabHostContent.size(); i++) {
            pvpChallengeList.add(new ArrayList<>());
            adapterPvpChallengeList.add(i,new PvpChallengeListView(getActivity(), pvpChallengeList.get(i)));
            listView.add(i, view.findViewById(_idListView[i]));
            listView.get(i).setAdapter(adapterPvpChallengeList.get(i));
        }
        // end adapter

        // Data load
        for(int i = 0; i < pvpChallenges.length(); i++){
            try {
                PvpChallengeClass tmp = new PvpChallengeClass(getActivity(),pvpChallenges.getJSONObject(i));
                if (!tmp.get_category_code().equals("PVPChallengeTypeCategory_WEEKLY_ROOT")) // todo hum, need to enhance
                {
                    pvpChallengeList.get(0).add(tmp); // add pvpChallenge to category 'all'
                    pvpChallengeList.get(_tabHostContent.indexOf(tmp.get_category_code())).add(tmp); // add pvpChallenge to it's good category
                }
            } catch (JSONException e) {
                Log.e(CurrentFileName,"Cannot add new pvpChallenge - " + e.getMessage());
            }
        }
        // End data load

        hTimerPvpChallenge.post(runnablePvpChallenge);
        hReloadPvpChallenge.post(runnableReloadPvpChallenge);

        return view;
    }

    private Runnable runnablePvpChallenge = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < pvpChallengeList.size();i++) {
                for(int j = 0; j < pvpChallengeList.get(i).size(); j++) {
                    if (pvpChallengeList.get(i).get(j).is_end_of_pvp_challenge()) {
                        pvpChallengeList.get(i).remove(j);
                    }
                }
            }

            for (int i = 0; i < _tabHostContent.size(); i++) {
                adapterPvpChallengeList.get(i).notifyDataSetChanged();
            }
            hTimerPvpChallenge.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadPvpChallenge = new Runnable() {
        @Override
        public void run() {
            try {
                pvpChallenges = MenuActivity.warframeWorldState.getPvpChallengeInstances();

                boolean stop;
                for (int i = 0; i < pvpChallenges.length(); i++) { // we go through the new list (probably bigger than the old one)
                    stop = false; // on remet à false
                    for(int j = 0; j < pvpChallengeList.get(0).size(); j++) { // we compare to the old list
                        if(pvpChallenges.getJSONObject(i).getJSONObject("_id").getString("$oid").compareTo(pvpChallengeList.get(0).get(j).get_id()) == 0) {
                            stop = true; // we indicate that we have found one
                            break; // we break the loop because it is useless to continue
                        }
                    }
                    if (!stop) { // if we did not leave the loop then it's because this alert is new
                        PvpChallengeClass tmp = new PvpChallengeClass(getActivity(),pvpChallenges.getJSONObject(i));
                        if (!tmp.get_category_code().equals("PVPChallengeTypeCategory_WEEKLY_ROOT")) // todo hum, need to enhance
                        {
                            pvpChallengeList.get(0).add(tmp); // add pvpChallenge to category 'all'
                            pvpChallengeList.get(_tabHostContent.indexOf(tmp.get_category_code())).add(tmp); // add pvpChallenge to it's good category
                        }
                    }
                    Collections.sort(pvpChallengeList.get(0),(o1, o2) -> o1.get_category_code().compareTo(o2.get_category_code()) );
                }
            } catch (Exception ex) {
                Log.e(CurrentFileName,"Cannot add new pvpChallenge - " + ex.getMessage());
            }
            hReloadPvpChallenge.postDelayed(this, 60000);
        }
    };
}
