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

import com.evilflora.warframesentinel.Modele.InvasionClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.InvasionListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class InvasionFragment extends Fragment {

    private static String _currentFileName = "InvasionFragment";
    private Handler _hTimerInvasionCurrent = new Handler();
    private List<List<InvasionClass>> _invasionList = new ArrayList<>();
    private List<InvasionListView> _adapterInvasionList;
    private TabHost tabHost;
    private int[] _tabTitle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.invasions_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.invasions));

        // Tabs
        int[] listListView = {R.id.list_invasion_current, R.id.list_invasion_completed};
        int[] tabId = {R.id.invasions_current, R.id.invasions_completed};
        _tabTitle = new int[]{R.string.current, R.string.completed};
        tabHost = view.findViewById(R.id.tabHost_invasions);
        tabHost.setup();
        TabHost.TabSpec spec;
        for (int i = 0; i < _tabTitle.length; i++) {
            spec = tabHost.newTabSpec(getString(_tabTitle[i]));
            spec.setContent(tabId[i]);
            spec.setIndicator(getString(_tabTitle[i]));
            tabHost.addTab(spec);
        }
        // end tabs

        // Adapter
        _adapterInvasionList = new ArrayList<>(_tabTitle.length); // we created a list to fit the size of the number of types of fissure plus the category all
        List<ListView> listView = new ArrayList<>(_tabTitle.length);
        for (int i = 0; i < _tabTitle.length; i++) {
            _invasionList.add(new ArrayList<>());
            _adapterInvasionList.add(i,new InvasionListView(getActivity(), _invasionList.get(i)));
            listView.add(i, view.findViewById(listListView[i]));
            listView.get(i).setAdapter(_adapterInvasionList.get(i));
        }
        // end adapters

        // Handlers
        _hTimerInvasionCurrent.post(runnableLoadInvasionCurrent);
        // end handlers

        return view;
    }

    private Runnable runnableLoadInvasionCurrent = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < _tabTitle.length; i++) {
                _invasionList.get(i).clear();
            }
            try {
                JSONArray invasions = MenuActivity.getWarframeWorldState().getInvasions();
                for (int i = 0; i < invasions.length(); i++) { // for the number of invasion
                    InvasionClass tmp = new InvasionClass(getActivity(), invasions.getJSONObject(i));
                    if (!tmp.isCompleted()) { // if it's no completed
                        _invasionList.get(0).add(tmp);
                    } else {
                        _invasionList.get(1).add(tmp);
                    }
                }
                updateTabs();
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot read invasion - " + ex.getMessage());
            }
            _hTimerInvasionCurrent.postDelayed(this, 60000);
        }
    };


    void updateTabs() {
        if (getActivity() != null){
            for(int i = 0; i < _tabTitle.length; i++) {
                TextView text = tabHost.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.title);
                text.setText(getResources().getString(getResources().getIdentifier("tab_name_quantity", "string", getActivity().getPackageName()), getResources().getString(_tabTitle[i]), _invasionList.get(i).size()));
                if(_adapterInvasionList.get(i).getCount() > 0) _adapterInvasionList.get(i).notifyDataSetChanged();
            }
        }
    }
}
