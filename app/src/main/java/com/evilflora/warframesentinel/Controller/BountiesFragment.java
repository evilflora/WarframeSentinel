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

import java.util.Arrays;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bounties_content, container, false);
        //noinspection ConstantConditions
        getActivity().setTitle(getString(R.string.bounties));

        TabHost tabHost = view.findViewById(R.id.tabHost_bounties);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.cetus));
        spec.setContent(R.id.cetus_bounties);
        spec.setIndicator(getString(R.string.cetus));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getString(R.string.orb_vallis));
        spec.setContent(R.id.orb_vallis_bounties);
        spec.setIndicator(getString(R.string.orb_vallis));
        tabHost.addTab(spec);

        load(); // todo due to the structure, this is called two times, here dans with runnableLoadBounties

        // chargement des bounties pour cetus
        ListView listViewCetusBounties = view.findViewById(R.id.list_cetus_bounties);
        _adapterCetusBounties = new BountiesListView(getContext(), _cetusBounty.getBountyJobs());
        listViewCetusBounties.setAdapter(_adapterCetusBounties);

        // chargement des bounties pour orb vallis
        ListView listViewOrbVallisBounties = view.findViewById(R.id.list_orb_vallis_bounties);
        _adapterOrbVallisBounties = new BountiesListView(getContext(), _orbVallisBounty.getBountyJobs());
        listViewOrbVallisBounties.setAdapter(_adapterOrbVallisBounties);

        _cetus_day_night_time_left = view.findViewById(R.id.cetus_bounties_timer_reset);
        _orb_vallis_bounties_timer_reset = view.findViewById(R.id.orb_vallis_bounties_timer_reset);

        _hTimerResetBounties.post(runnableLoadBounties);
        _hTimerBounties.post(runnableBounties);

        return view;
    }

    private Runnable runnableBounties = new Runnable() {
        @Override
        public void run() {
            try {
                _cetus_day_night_time_left.setText(String.format("%s: %s", getString(R.string.time_before_reset), _cetusBounty.getTimeBeforeExpiry()));
                _orb_vallis_bounties_timer_reset.setText(String.format("%s: %s", getString(R.string.time_before_reset), _orbVallisBounty.getTimeBeforeExpiry()));
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot update bounties timer - " + ex.getMessage());
            }
            _hTimerBounties.postDelayed(this, 1000);
        }
    };

    private Runnable runnableLoadBounties = new Runnable() {
        @Override
        public void run() {
            load();
            // restart the refresh of the bounties at the time of the reset and as long as the status is undetermined then we retry every minute
            _hTimerResetBounties.postDelayed(this, (_cetusBounty.getCycleStatus() ? 60 * 1000 : _cetusBounty.getTimeLeft()));
        }
    };

    void load() {
        try {
            JSONArray cetus = MenuActivity.warframeWorldState.getCetusMissions();
            JSONArray orbVallis = MenuActivity.warframeWorldState.getOrbVallisMissions();

            _cetusBounty = new BountiesClass(getActivity(),cetus, Arrays.asList("Day", "Night", "Indeterminate"));
            _orbVallisBounty = new BountiesClass(getActivity(), orbVallis,Arrays.asList("Warm", "Cold", "Indeterminate"));

            if (_cetusBounty.getCycleStatus()) {
                _cetusBounty.getBountyJobs().clear();
                _adapterCetusBounties.notifyDataSetChanged();
            }

            if (_orbVallisBounty.getCycleStatus()) {
                _orbVallisBounty.getBountyJobs().clear();
                _adapterOrbVallisBounties.notifyDataSetChanged();
            }
        } catch (Exception ex){
            Log.e(_currentFileName,"Cannot read bounties - " + ex.getMessage());
        }
    }
}
