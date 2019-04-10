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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DarvoMarketFragment  extends Fragment {

    final String CurrentFileName = "DarvoMarketFragment";
    List<DarvoDealsClass> darvoDealsList = new ArrayList<>();
    List<MarketItemsClass> marketItemsList = new ArrayList<>();
    DarvoDealsListView adapterDarvoDeals;
    MarketItemsListView adapterMarketItems;
    Handler hTimerDarvoMarket = new Handler();
    Handler hTimerDarvoMarketLoad = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.darvo_market_content, container, false);
        darvoDealsList.clear();
        marketItemsList.clear();
        getActivity().setTitle(getString(R.string.darvo_market));

        TabHost tabHost = view.findViewById(R.id.tabHost_darvo_market);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.darvo_sales));
        spec.setContent(R.id.darvo_current);
        spec.setIndicator(getString(R.string.darvo_sales));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getString(R.string.market_items));
        spec.setContent(R.id.market_current);
        spec.setIndicator(getString(R.string.market_items));
        tabHost.addTab(spec);

        ListView listViewDarvoDeals = view.findViewById(R.id.list_darvo_current);
        ListView listViewMarketItems = view.findViewById(R.id.list_market_current);
        adapterDarvoDeals = new DarvoDealsListView(getActivity(), darvoDealsList);
        adapterMarketItems = new MarketItemsListView(getActivity(), marketItemsList);
        listViewDarvoDeals.setAdapter(adapterDarvoDeals);
        listViewMarketItems.setAdapter(adapterMarketItems);

        hTimerDarvoMarketLoad.post(runnableDarvoMarketLoad);
        hTimerDarvoMarket.post(runnableDarvoMarket);

        return view;
    }

    private Runnable runnableDarvoMarket = new Runnable() {
        @Override
        public void run() {
            try {
                if(adapterDarvoDeals.getCount() > 0)
                    adapterDarvoDeals.notifyDataSetChanged();
            } catch(Exception ex) {
                Log.e(CurrentFileName,"Cannot update Darvo items timer - " + ex.getMessage());
            }
            try {
                if(adapterMarketItems.getCount() > 0)
                    adapterMarketItems.notifyDataSetChanged();
            } catch(Exception ex) {
                Log.e(CurrentFileName,"Cannot update Market items timer - " + ex.getMessage());
            }

            hTimerDarvoMarket.postDelayed(this, 1000);
        }
    };
    private Runnable runnableDarvoMarketLoad = new Runnable() {
        @Override
        public void run() {
            load();
            hTimerDarvoMarket.postDelayed(this, 60 * 1000);
        }
    };

    void load() {
        darvoDealsList.clear();
        marketItemsList.clear();
        try {
            JSONArray darvoDeals = MenuActivity.warframeWorldState.getDailyDeals();
            for (int i = 0; i < darvoDeals.length(); i++) {
                darvoDealsList.add(new DarvoDealsClass(getActivity(),darvoDeals.getJSONObject(i)));
            }
        } catch (Exception ex){
            Log.e(CurrentFileName,"Cannot read darvo deals - " + ex.getMessage());
        }
        try {
            JSONArray marketItems = MenuActivity.warframeWorldState.getFlashSales();
            for (int i = 0; i < marketItems.length(); i++) {
                marketItemsList.add(new MarketItemsClass(getActivity(),marketItems.getJSONObject(i)));
            }
        } catch (Exception ex){
            Log.e(CurrentFileName,"Cannot read market items - " + ex.getMessage());
        }
    }
}
