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

public class CetusEarthFragment extends Fragment {

    final String CurrentFileName = "CetusEarthFragment";
    CetusClass cetusClass;
    TextView bountyTimer;
    TextView cetus_day_night_time_left;
    TextView cetus_day_night_status;
    TextView cetus_day_night_info;
    //private TextView earth_day_night_time_left;
    //private TextView earth_day_night_status;
    Handler hTimerBounties = new Handler();
    Handler hTimerResetBounties = new Handler();
    CetusBountiesListView adapterCetusBounties;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cetus_earth_content, container, false);
        getActivity().setTitle(getString(R.string.cetus_earth));

        TabHost tabHost = view.findViewById(R.id.tabHost_cetus);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.current));
        spec.setContent(R.id.cetus_bounties);
        spec.setIndicator(getString(R.string.bounties));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getString(R.string.completed));
        spec.setContent(R.id.cetus_timers);
        spec.setIndicator(getString(R.string.day_night));
        tabHost.addTab(spec);

        load();

        ListView listViewCetusBounties = view.findViewById(R.id.list_cetus_bounties);
        adapterCetusBounties = new CetusBountiesListView(getContext(), cetusClass.get_cetus_jobs());
        listViewCetusBounties.setAdapter(adapterCetusBounties);

        bountyTimer = view.findViewById(R.id.cetus_bounties_timer_reset);
        cetus_day_night_time_left = view.findViewById(R.id.cetus_day_night_time_left);
        cetus_day_night_status = view.findViewById(R.id.cetus_day_night_status);
        cetus_day_night_info = view.findViewById(R.id.cetus_day_night_info);

        hTimerResetBounties.post(runnableLoadBounties); // On rafraichis toutes les secondes les timers
        hTimerBounties.post(runnableBounties); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableBounties = new Runnable() {
        @Override
        public void run() {
            try {
                bountyTimer.setText(String.format("%s %s", getString(R.string.time_before_reset), cetusClass.get_time_before_expiry()));
                cetus_day_night_status.setText(!cetusClass.get_status() ? cetusClass.cetus_day_or_night() : getString(R.string.cetus_waiting_reset));
                cetus_day_night_time_left.setText(String.format("%s %s", getString(R.string.time_left), cetusClass.cetus_day_night_time()));
                cetus_day_night_info.setText(!cetusClass.get_status() ?  String.format("%s %s", getString(R.string.date), cetusClass.cetus_next_cycle_date()) : "");
            } catch (Exception ex){
                Log.e(CurrentFileName,"Cannot update bounties timer | " + ex.getMessage());
            }
            hTimerBounties.postDelayed(this, 1000);
        }
    };

    private Runnable runnableLoadBounties = new Runnable() {
        @Override
        public void run() {
            load();
            // relance le refresh des bounties au moment du reset puis tant que le statut est indéterminé on rééssaie toutes les minutes
            //Log.i(CurrentFileName, getString(R.string.status) + ": " + cetusClass.get_status() + " - " + TimestampToDate.convert(cetusClass.get_status() ? 60 * 1000 : cetusClass.get_time_left(),true));
            hTimerResetBounties.postDelayed(this, (cetusClass.get_status() ? 60 * 1000 : cetusClass.get_time_left()));
        }
    };

    void load() {
        try {
            JSONArray bounties = MenuActivity.warframeWorldState.getCetusMissions();
            cetusClass = new CetusClass(bounties); // on l'instancie

            Log.e(CurrentFileName,"Reading news bounties"); // todo pourquoi cette fonction est-elle lancée 2 fois lors de l'affichage de la vue ?
            if (cetusClass.get_status()) {
                cetusClass.get_cetus_jobs().clear();
                adapterCetusBounties.notifyDataSetChanged();
            }
        } catch (Exception ex){
            Log.e(CurrentFileName,"Cannot read bounties - " + ex.getMessage());
        }
    }
}
