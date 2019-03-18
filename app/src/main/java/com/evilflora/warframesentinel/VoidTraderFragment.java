package com.evilflora.warframesentinel;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

public class VoidTraderFragment extends Fragment {

    final String CurrentFileName = "VoidTraderFragment";
    VoidTraderClass voidTrader; // Baro ki teer
    VoidTraderListView adapterVoidTrader; // La liste customisé basé sur le layout alerte_element
    Handler hTimerVoidTrader = new Handler();
    Handler hReloadVoidTrader = new Handler();
    ListView listViewVoidTrader;
    TextView void_trader_reset_timer;
    TextView void_trader_location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.void_trader_content, container, false);
        getActivity().setTitle(getString(R.string.baro_kiteer));

        load();

        listViewVoidTrader = view.findViewById(R.id.listView_void_trader);
        adapterVoidTrader = new VoidTraderListView(getContext(), voidTrader.get_items());
        listViewVoidTrader.setAdapter(adapterVoidTrader);

        void_trader_reset_timer = view.findViewById(R.id.void_trader_reset_timer);
        void_trader_location = view.findViewById(R.id.void_trader_item_name);

        hTimerVoidTrader.post(runnableVoidTrader); // On rafraichis toutes les secondes les timers
        hReloadVoidTrader.post(runnableReloadVoidTrader); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableVoidTrader = new Runnable() {
        @Override
        public void run() {

            try {
                void_trader_reset_timer.setText(voidTrader.get_time_before_expiry());
                void_trader_location.setText(voidTrader.get_location());
            } catch (Exception ex){
                Log.e(CurrentFileName,"Cannot update void trader timer | " + ex.getMessage());
            }

            hTimerVoidTrader.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadVoidTrader = new Runnable() {
        @Override
        public void run() {
            //load();
            listViewVoidTrader.postDelayed(this, 60 * 1000);
        }
    };

    void load() {
        try {
            JSONArray void_trader_update = MenuActivity.warframeWorldState.getVoidTraders(); // on récupère la liste des alertes
            voidTrader = new VoidTraderClass(getActivity(),void_trader_update.getJSONObject(0));


        } catch (Exception ex) {
            Log.e(CurrentFileName,"Cannot add new alert | " + ex.getMessage());
        }
    }
}
