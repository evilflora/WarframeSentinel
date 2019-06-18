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

import com.evilflora.warframesentinel.Modele.InvasionClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.InvasionListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class InvasionFragment extends Fragment {

    private static String _currentFileName = "InvasionFragment";
    private List<InvasionClass> _invasionListCurrent = new ArrayList<>();
    private List<InvasionClass> _invasionListCompleted = new ArrayList<>();
    private Handler _hTimerInvasionCurrent = new Handler();
    private InvasionListView _adapterCurrent;
    private InvasionListView _adapterCompleted;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.invasions_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.invasions));

        // Tabs
        int[] tab = {R.id.invasions_current, R.id.invasions_completed};
        int[] tabContent = {R.id.list_invasion_current, R.id.list_invasion_completed};
        int[] tabHostContent = {R.string.current, R.string.completed};
        TabHost tabHost = view.findViewById(R.id.tabHost_invasions);
        tabHost.setup();
        TabHost.TabSpec spec;
        for (int i = 0; i < tabHostContent.length; i++) {
            spec = tabHost.newTabSpec(getString(tabHostContent[i]));
            spec.setContent(tab[i]);
            spec.setIndicator(getString(tabHostContent[i]));
            tabHost.addTab(spec);
        }
        // end tabs

        // Adapter
        _adapterCurrent = new InvasionListView(getActivity(), _invasionListCurrent);
        ListView listViewCurrent = view.findViewById(tabContent[0]);
        listViewCurrent.setAdapter(_adapterCurrent);

        _adapterCompleted = new InvasionListView(getActivity(), _invasionListCompleted);
        ListView listViewCompleted = view.findViewById(tabContent[1]);
        listViewCompleted.setAdapter(_adapterCompleted);
        // end adapters

        // Handlers
        _hTimerInvasionCurrent.post(runnableLoadInvasionCurrent);
        // end handlers

        return view;
    }

    private Runnable runnableLoadInvasionCurrent = new Runnable() {
        @Override
        public void run() {
            try {
                _invasionListCurrent.clear();
                _invasionListCompleted.clear();

                JSONArray invasions = MenuActivity.getWarframeWorldState().getInvasions();

                for (int i = 0; i < invasions.length(); i++) { // for the number of invasion
                    InvasionClass tmp = new InvasionClass(getActivity(), invasions.getJSONObject(i));
                    if (!tmp.isCompleted()) { // if it's complete
                        _invasionListCurrent.add(tmp);
                    } else {
                        _invasionListCompleted.add(tmp);
                    }
                }

                if(_adapterCurrent.getCount() > 0) _adapterCurrent.notifyDataSetChanged();
                if(_adapterCompleted.getCount() > 0)  _adapterCompleted.notifyDataSetChanged();

            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot read invasion - " + ex.getMessage());
            }

            _hTimerInvasionCurrent.postDelayed(this, 60000);
        }
    };

}
