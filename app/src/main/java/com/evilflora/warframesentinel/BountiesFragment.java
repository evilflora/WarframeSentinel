package com.evilflora.warframesentinel;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;

public class BountiesFragment extends Fragment {

    final String CurrentFileName = "CetusEarthFragment"; // filename
    BountiesClass cetusClass;
    BountiesClass orbVallisClass;
    TextView cetus_day_night_time_left;
    TextView orb_vallis_bounties_timer_reset;
    Handler hTimerBounties = new Handler();
    Handler hTimerResetBounties = new Handler();
    BountiesListView adapterCetusBounties;
    BountiesListView adapterOrbVallisBounties;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bounties_content, container, false);
        getActivity().setTitle(getString(R.string.bounties));

        TabHost tabHost = view.findViewById(R.id.tabHost_bounties);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.current)); // todo why this ?
        spec.setContent(R.id.cetus_bounties);
        spec.setIndicator(getString(R.string.cetus));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getString(R.string.completed)); // todo why this ?
        spec.setContent(R.id.orb_vallis_bounties);
        spec.setIndicator(getString(R.string.orb_vallis));
        tabHost.addTab(spec);

        load();

        // chargement des bounties pour cetus
        ListView listViewCetusBounties = view.findViewById(R.id.list_cetus_bounties);
        adapterCetusBounties = new BountiesListView(getContext(), cetusClass.get_cetus_jobs());
        listViewCetusBounties.setAdapter(adapterCetusBounties);

        // chargement des bounties pour orb vallis
        ListView listViewOrbVallisBounties = view.findViewById(R.id.list_orb_vallis_bounties);
        adapterOrbVallisBounties = new BountiesListView(getContext(), orbVallisClass.get_cetus_jobs());
        listViewOrbVallisBounties.setAdapter(adapterOrbVallisBounties);

        cetus_day_night_time_left = view.findViewById(R.id.cetus_bounties_timer_reset);
        orb_vallis_bounties_timer_reset = view.findViewById(R.id.orb_vallis_bounties_timer_reset);

        hTimerResetBounties.post(runnableLoadBounties); // we are refreshing every seconds
        hTimerBounties.post(runnableBounties); // we are refreshing every seconds

        return view;
    }

    private Runnable runnableBounties = new Runnable() {
        @Override
        public void run() {
            try {
                cetus_day_night_time_left.setText(String.format("%s: %s", getString(R.string.time_before_reset), cetusClass.get_time_before_expiry()));
                orb_vallis_bounties_timer_reset.setText(String.format("%s: %s", getString(R.string.time_before_reset), orbVallisClass.get_time_before_expiry()));
            } catch (Exception ex){
                Log.e(CurrentFileName,"Cannot update bounties timer - " + ex.getMessage());
            }
            hTimerBounties.postDelayed(this, 1000);
        }
    };

    private Runnable runnableLoadBounties = new Runnable() {
        @Override
        public void run() {
            load();
            // restart the refresh of the bounties at the time of the reset and as long as the status is undetermined then we retry every minute
            hTimerResetBounties.postDelayed(this, (cetusClass.get_status() ? 60 * 1000 : cetusClass.get_time_left()));
        }
    };

    void load() {
        try {
            JSONArray cetus = MenuActivity.warframeWorldState.getCetusMissions();
            JSONArray orb_vallis = MenuActivity.warframeWorldState.getOrbVallisMissions();

            cetusClass = new BountiesClass(cetus);
            orbVallisClass = new BountiesClass(orb_vallis);

            Log.e(CurrentFileName,"Reading news bounties"); // todo why is this function started 2 times when viewing the view?
            if (cetusClass.get_status()) {
                cetusClass.get_cetus_jobs().clear();
                adapterCetusBounties.notifyDataSetChanged();
            }

            Log.e(CurrentFileName,"Reading news bounties"); // todo why is this function started 2 times when viewing the view?
            if (orbVallisClass.get_status()) {
                orbVallisClass.get_cetus_jobs().clear();
                adapterOrbVallisBounties.notifyDataSetChanged();
            }
        } catch (Exception ex){
            Log.e(CurrentFileName,"Cannot read bounties - " + ex.getMessage());
        }
    }
}
