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

import com.evilflora.warframesentinel.Modele.LegionChallengeClass;
import com.evilflora.warframesentinel.Modele.LegionClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.LegionListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LegionFragment extends Fragment {

    private static String _currentFileName = "LegionFragment";
    private List<List<LegionChallengeClass>> _legionChallengeList = new ArrayList<>();
    private List<LegionListView> _adapterLegionList;
    private List<String> _tabHostContent;
    private Handler _hTimerLegion = new Handler();
    private Handler _hReloadLegion = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.legion_content, container, false);
        if (getActivity() != null)getActivity().setTitle(getString(R.string.nightwave));

        // Tabs
        int[] tab = {R.id.challenges_all, R.id.challenges_daily, R.id.challenges_weekly, R.id.challenges_weekly_hard};
        int[] tabContent = {R.id.listView_challenge_all, R.id.listView_challenge_daily, R.id.listView_challenge_weekly, R.id.listView_challenge_weekly_hard};
        //int[] pullList = {R.id.pullToRefreshAll, R.id.pullToRefreshDaily, R.id.pullToRefreshHard, R.id.pullToRefreshWeekly};
        _tabHostContent = Arrays.asList("all", "Daily", "Weekly", "WeeklyHard");
        TabHost tabHost = view.findViewById(R.id.tabHost_legions);
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
        _adapterLegionList = new ArrayList<>(_tabHostContent.size()); // we created a list to fit the size of the number of types of legion plus the category all
        List<ListView> listView = new ArrayList<>(_tabHostContent.size());
        //List<SwipeRefreshLayout> pullToRefresh  = new ArrayList<>(_tabHostContent.size());
        for (int i = 0; i < _tabHostContent.size(); i++) {
            _legionChallengeList.add(new ArrayList<>());
            _adapterLegionList.add(i,new LegionListView(getActivity(), _legionChallengeList.get(i)));
            listView.add(i, view.findViewById(tabContent[i]));
            listView.get(i).setAdapter(_adapterLegionList.get(i));
            //pullToRefresh.add(view.findViewById(pullList[i]));
        }
        // end adapter

        // swipe down to refresh todo it's not possible to edit variable in lambda, so is it possible to put the same id for xml layout and bind it one time for tabs ?
        /*
        for (int i = 0; i < _tabHostContent.size(); i++) {
            if (pullToRefresh.get(i) != null) { // if layout isn't setup
                pullToRefresh.get(i).setOnRefreshListener(() -> {
                    load();
                    if(_adapterLegionList.get(i).getCount() > 0) _adapterLegionList.get(i).notifyDataSetChanged();
                    pullToRefresh.get(i).setRefreshing(false);
                });
            }
        }*/

        // Handler
        _hTimerLegion.post(runnableLegion);
        _hReloadLegion.post(runnableReloadLegion);
        // end handlers

        return view;
    }

    private Runnable runnableLegion = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < _legionChallengeList.size();i++) {
                for(int j = 0; j < _legionChallengeList.get(i).size(); j++) {
                    _legionChallengeList.get(i).removeIf(LegionChallengeClass::isEnd);
                }
            }

            for (int i = 0; i < _tabHostContent.size(); i++) {
                if(_adapterLegionList.get(i).getCount() > 0) _adapterLegionList.get(i).notifyDataSetChanged();
            }
            _hTimerLegion.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadLegion = new Runnable() {
        @Override
        public void run() {
            try {
                LegionClass legion = new LegionClass(getActivity(), MenuActivity.getWarframeWorldState().getLegion());
                boolean stop;
                for (int i = 0; i < legion.getChallengeLength(); i++) { // we go through the new list (probably bigger than the old one)
                    stop = false;
                    for(int j = 0; j < _legionChallengeList.get(0).size(); j++) { // we compare to the old list
                        if(legion.getChallenge(i).getId().compareTo(_legionChallengeList.get(0).get(j).getId()) == 0) {
                            stop = true; // we indicate that we have found one
                            break; // we break the loop because it is useless to continue
                        }
                    }
                    if (!stop) { // if we did not leave the loop then it's because this alert is new
                        Log.i(_currentFileName,"Adding Challenge ID : " + legion.getChallenge(i).getId());
                        _legionChallengeList.get(0).add(legion.getChallenge(i)); // add legion to category 'all'
                        _legionChallengeList.get(_tabHostContent.indexOf(legion.getChallenge(i).getTypeCode())).add(legion.getChallenge(i)); // add legion to it's good category
                    }
                }
                Collections.sort(_legionChallengeList.get(0),(o1, o2) -> o1.getTypeCode().compareTo(o2.getTypeCode()) );
                for(int j = 0; j < _tabHostContent.size(); j++)
                {
                    if (_adapterLegionList.get(j).getCount() > 0) _adapterLegionList.get(j).notifyDataSetChanged(); // we update the view
                }
            } catch (Exception ex) {
                Log.e(_currentFileName,"Cannot add new legion - " + ex.getMessage());
            }
            _hReloadLegion.postDelayed(this, 60000);
        }
    };

}