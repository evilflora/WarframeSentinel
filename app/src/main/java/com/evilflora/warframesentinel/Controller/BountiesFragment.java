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

import com.evilflora.warframesentinel.Modele.BountiesClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.BountiesListView;

import org.json.JSONArray;

public class BountiesFragment extends Fragment {

    private static String _currentFileName = "CetusEarthFragment"; // filename
    BountiesClass _cetusBounty;
    BountiesClass _orbVallisBounty;
    TextView _cetus_day_night_time_left;
    TextView _orb_vallis_bounties_timer_reset;
    Handler _hTimerBounties = new Handler();
    Handler _hTimerResetBounties = new Handler();
    BountiesListView _adapterCetusBounties;
    BountiesListView _adapterOrbVallisBounties;
    JSONArray _cetus;
    JSONArray _orbVallis;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bounties_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.bounties));

        // Tabs
        int[] tab = {R.id.cetus_bounties, R.id.orb_vallis_bounties};
        int[] tabContent = {R.id.list_cetus_bounties, R.id.list_orb_vallis_bounties};
        int[] tabHostContent = {R.string.cetus, R.string.orb_vallis};
        TabHost tabHost = view.findViewById(R.id.tabHost_bounties);
        tabHost.setup();
        TabHost.TabSpec spec;
        for (int i = 0; i < tabHostContent.length; i++) {
            spec = tabHost.newTabSpec(getString(tabHostContent[i]));
            spec.setContent(tab[i]);
            spec.setIndicator(getString(tabHostContent[i]));
            tabHost.addTab(spec);
        }
        // End tabs

        // Adapters
        _cetus = MenuActivity.warframeWorldState.getCetusMissions();
        _orbVallis = MenuActivity.warframeWorldState.getOrbVallisMissions();

        _cetusBounty = new BountiesClass(getActivity(),_cetus,50, "cetus_cycle");
        _orbVallisBounty = new BountiesClass(getActivity(), _orbVallis, 50, "orb_vallis_cycle");

        _adapterCetusBounties = new BountiesListView(getActivity(), _cetusBounty.getBountyJobs());
        ListView listViewCetusBounties = view.findViewById(tabContent[0]);
        listViewCetusBounties.setAdapter(_adapterCetusBounties);

        _adapterOrbVallisBounties = new BountiesListView(getActivity(), _orbVallisBounty.getBountyJobs());
        ListView listViewOrbVallisBounties = view.findViewById(tabContent[1]);
        listViewOrbVallisBounties.setAdapter(_adapterOrbVallisBounties);
        // end adapters

        // Timers
        _cetus_day_night_time_left = view.findViewById(R.id.cetus_bounties_timer_reset);
        _orb_vallis_bounties_timer_reset = view.findViewById(R.id.orb_vallis_bounties_timer_reset);
        // end timers

        // Handlers
        _hTimerResetBounties.post(runnableLoadBounties);
        _hTimerBounties.post(runnableBounties);
        // end handlers

        return view;
    }

    private Runnable runnableBounties = new Runnable() {
        @Override
        public void run() {
            try {
                _cetus_day_night_time_left.setText(_cetusBounty.getTimeBeforeReset());
                _orb_vallis_bounties_timer_reset.setText(_orbVallisBounty.getTimeBeforeReset());
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot update bounties timer - " + ex.getMessage());
            }
            _hTimerBounties.postDelayed(this, 1000);
        }
    };

    private Runnable runnableLoadBounties = new Runnable() {
        @Override
        public void run() {
            try {
                if (_cetusBounty.isEndOfBounty()) {
                    _cetusBounty.getBountyJobs().clear();
                    _cetus = MenuActivity.warframeWorldState.getCetusMissions();
                    _cetusBounty = new BountiesClass(getActivity(),_cetus,50, "cetus_cycle");
                }

                if (_orbVallisBounty.isEndOfBounty()) {
                    _orbVallisBounty.getBountyJobs().clear();
                    _orbVallis = MenuActivity.warframeWorldState.getOrbVallisMissions();
                    _orbVallisBounty = new BountiesClass(getActivity(), _orbVallis, 50, "orb_vallis_cycle");
                }

                if(_adapterOrbVallisBounties.getCount() > 0)_adapterOrbVallisBounties.notifyDataSetChanged();
                if(_adapterCetusBounties.getCount() > 0)_adapterCetusBounties.notifyDataSetChanged();
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot read bounties - " + ex.getMessage());
            }

            _hTimerResetBounties.postDelayed(this, (_cetusBounty.isEndOfBounty() ? 60000 : _cetusBounty.getTimeLeft()));
        }
    };

}
