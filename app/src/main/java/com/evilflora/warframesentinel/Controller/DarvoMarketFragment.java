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

import com.evilflora.warframesentinel.Modele.DarvoDealsClass;
import com.evilflora.warframesentinel.Modele.MarketItemsClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.DarvoDealsListView;
import com.evilflora.warframesentinel.Vue.MarketItemsListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DarvoMarketFragment  extends Fragment {

    private static String _currentFileName = "DarvoMarketFragment";
    private List<DarvoDealsClass> _darvoDealsList = new ArrayList<>();
    private List<MarketItemsClass> _marketItemsList = new ArrayList<>();
    private DarvoDealsListView _adapterDarvoDeals;
    private MarketItemsListView _adapterMarketItems;
    private Handler _hTimerDarvoMarket = new Handler();
    private Handler _hTimerDarvoMarketLoad = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.darvo_market_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.darvo_market));

        // Tabs
        int[] tab = {R.id.darvo_current, R.id.market_current};
        int[] tabContent = {R.id.list_darvo_current, R.id.list_market_current};
        int[] tabHostContent = {R.string.darvo_sales, R.string.market_items};
        TabHost tabHost = view.findViewById(R.id.tabHost_darvo_market);
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
        _adapterDarvoDeals = new DarvoDealsListView(getActivity(), _darvoDealsList);
        ListView listViewDarvoDeals = view.findViewById(tabContent[0]);
        listViewDarvoDeals.setAdapter(_adapterDarvoDeals);

        _adapterMarketItems = new MarketItemsListView(getActivity(), _marketItemsList);
        ListView listViewMarketItems = view.findViewById(tabContent[1]);
        listViewMarketItems.setAdapter(_adapterMarketItems);
        // end adapters

        // Handlers
        _hTimerDarvoMarketLoad.post(runnableDarvoMarketLoad);
        _hTimerDarvoMarket.post(runnableDarvoMarket);
        // end handlers

        return view;
    }

    private Runnable runnableDarvoMarket = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < _darvoDealsList.size();i++) {
                if (_darvoDealsList.get(i).isEndOfSale()) {
                    _darvoDealsList.remove(i);
                }
            }
            for (int i = 0; i < _marketItemsList.size();i++) {
                if (_marketItemsList.get(i).isEndOfSale()) {
                    _marketItemsList.remove(i);
                }
            }
            if(_adapterDarvoDeals.getCount() > 0)_adapterDarvoDeals.notifyDataSetChanged();
            if(_adapterMarketItems.getCount() > 0)_adapterMarketItems.notifyDataSetChanged();
            _hTimerDarvoMarket.postDelayed(this, 1000);
        }
    };
    private Runnable runnableDarvoMarketLoad = new Runnable() {
        @Override
        public void run() {
            _darvoDealsList.clear();
            _marketItemsList.clear();

            JSONArray darvoDeals = MenuActivity.getWarframeWorldState().getDailyDeals();
            JSONArray marketItems = MenuActivity.getWarframeWorldState().getFlashSales();

            try {
                for (int i = 0; i < darvoDeals.length(); i++) {
                    _darvoDealsList.add(new DarvoDealsClass(getActivity(), darvoDeals.getJSONObject(i)));
                }
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot read darvo deals - " + ex.getMessage());
            }

            try {
                for (int i = 0; i < marketItems.length(); i++) {
                    _marketItemsList.add(new MarketItemsClass(getActivity(), marketItems.getJSONObject(i)));
                }
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot read market items - " + ex.getMessage());
            }

            _hTimerDarvoMarket.postDelayed(this, 60000);
        }
    };

}
