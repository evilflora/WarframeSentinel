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

    final String CurrentFileName = "CetusEarthFragment";
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
        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.current)); // todo a quoi ça sert ?
        spec.setContent(R.id.cetus_bounties);
        spec.setIndicator(getString(R.string.cetus));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getString(R.string.completed)); // todo a quoi ça sert ?
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

        hTimerResetBounties.post(runnableLoadBounties); // On rafraichis toutes les secondes les timers
        hTimerBounties.post(runnableBounties); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableBounties = new Runnable() {
        @Override
        public void run() {
            try {
                cetus_day_night_time_left.setText(String.format("%s: %s", getString(R.string.time_before_reset), cetusClass.get_time_before_expiry()));
                orb_vallis_bounties_timer_reset.setText(String.format("%s: %s", getString(R.string.time_before_reset), orbVallisClass.get_time_before_expiry()));
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
            JSONArray cetus = MenuActivity.warframeWorldState.getCetusMissions();
            JSONArray orb_vallis = MenuActivity.warframeWorldState.getOrbVallisMissions();

            cetusClass = new BountiesClass(cetus); // on l'instancie
            orbVallisClass = new BountiesClass(orb_vallis); // on l'instancie

            Log.e(CurrentFileName,"Reading news bounties"); // todo pourquoi cette fonction est-elle lancée 2 fois lors de l'affichage de la vue ?
            if (cetusClass.get_status()) {
                cetusClass.get_cetus_jobs().clear();
                adapterCetusBounties.notifyDataSetChanged();
            }

            Log.e(CurrentFileName,"Reading news bounties"); // todo pourquoi cette fonction est-elle lancée 2 fois lors de l'affichage de la vue ?
            if (orbVallisClass.get_status()) {
                orbVallisClass.get_cetus_jobs().clear();
                adapterOrbVallisBounties.notifyDataSetChanged();
            }
        } catch (Exception ex){
            Log.e(CurrentFileName,"Cannot read bounties - " + ex.getMessage());
        }
    }
}
