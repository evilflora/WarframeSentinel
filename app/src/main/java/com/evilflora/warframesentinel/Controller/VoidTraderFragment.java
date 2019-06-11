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
import android.widget.TextView;

import com.evilflora.warframesentinel.Modele.VoidTraderClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.VoidTraderListView;

import org.json.JSONArray;

public class VoidTraderFragment extends Fragment {

    private static String _currentFileName = "VoidTraderFragment";
    VoidTraderClass _voidTrader;
    VoidTraderListView _adapterVoidTrader;
    Handler _hTimerVoidTrader = new Handler();
    Handler _hReloadVoidTrader = new Handler();
    ListView _listViewVoidTrader;
    TextView _void_trader_reset_timer;
    TextView _void_trader_location;
    JSONArray _baro;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.void_trader_content, container, false);
        getActivity().setTitle(getString(R.string.baro_kiteer));

        // Adapters
        _baro = MenuActivity.warframeWorldState.getVoidTraders();
        _voidTrader = new VoidTraderClass(getActivity(),_baro);

        _listViewVoidTrader = view.findViewById(R.id.listView_void_trader);
        _adapterVoidTrader = new VoidTraderListView(getContext(), _voidTrader.getItems());
        _listViewVoidTrader.setAdapter(_adapterVoidTrader);
        // end of adapters

        // Timers
        _void_trader_reset_timer = view.findViewById(R.id.void_trader_reset_timer);
        _void_trader_location = view.findViewById(R.id.void_trader_item_name);
        // end of timers

        _hTimerVoidTrader.post(runnableVoidTrader);
        _hReloadVoidTrader.post(runnableReloadVoidTrader);

        return view;
    }

    private Runnable runnableVoidTrader = new Runnable() {
        @Override
        public void run() {

            try {
                _void_trader_reset_timer.setText(_voidTrader.getTimeBeforeEnd());
                _void_trader_location.setText(_voidTrader.getLocation());
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot update void trader timer | " + ex.getMessage());
            }

            _hTimerVoidTrader.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadVoidTrader = new Runnable() {
        @Override
        public void run() {
            try {
                _baro = MenuActivity.warframeWorldState.getVoidTraders();
                _voidTrader = new VoidTraderClass(getActivity(),_baro);

            } catch (Exception ex) {
                Log.e(_currentFileName,"Cannot add void trader | " + ex.getMessage());
            }
            _listViewVoidTrader.postDelayed(this, 60000);
        }
    };
}
