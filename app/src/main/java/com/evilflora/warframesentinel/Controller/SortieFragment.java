package com.evilflora.warframesentinel.Controller;

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

import com.evilflora.warframesentinel.Modele.SortieClass;
import com.evilflora.warframesentinel.Modele.SortieStepClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.SortieRewardListView;
import com.evilflora.warframesentinel.Vue.SortieStepListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SortieFragment extends Fragment {

    final String CurrentFileName = "SortieFragment";
    List<SortieStepClass> sortieStepList = new ArrayList<>(); // Liste des sortie
    SortieClass sortieClass;
    SortieStepListView adapterSortie; // La liste customisé basé sur le layout alerte_element
    SortieRewardListView adapterRewards; // La liste customisé basé sur le layout alerte_element
    TextView sortie_reset_timer;
    TextView sortie_type;
    Handler hTimerSortie = new Handler();
    Handler hReloadSortie = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sortie_content, container, false);
        sortieStepList.clear();
        getActivity().setTitle(getString(R.string.sorties));

        TabHost tabHost = view.findViewById(R.id.tabHost_sortie);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.current));
        spec.setContent(R.id.sortie_steps);
        spec.setIndicator(getString(R.string.missions));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getString(R.string.completed));
        spec.setContent(R.id.sortie_rewards);
        spec.setIndicator(getString(R.string.rewards));
        tabHost.addTab(spec);

        sortie_reset_timer = view.findViewById(R.id.sortie_reset_timer);
        sortie_type = view.findViewById(R.id.sortie_type);

        load();

        ListView listViewSortie = view.findViewById(R.id.list_sortie_steps);
        ListView listViewRewards = view.findViewById(R.id.list_sortie_rewards);
        adapterSortie = new SortieStepListView(getActivity(), sortieStepList);
        adapterRewards = new SortieRewardListView(getActivity(), sortieClass.get_rewards(),sortieClass.get_drop_chance());
        listViewSortie.setAdapter(adapterSortie);
        listViewRewards.setAdapter(adapterRewards);

        hTimerSortie.post(runnableSortie); // On rafraichis toutes les secondes les timers
        hReloadSortie.post(runnableReloadSortie); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableSortie = new Runnable() {
        @Override
        public void run() {
            try {
                sortie_reset_timer.setText(sortieClass.get_time_before_expiry());
                sortie_type.setText(sortieClass.get_sortie_type());
            } catch (Exception ex){
                Log.e(CurrentFileName,"Cannot update sortie timer | " + ex.getMessage());
            }
            hTimerSortie.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadSortie = new Runnable() {
        @Override
        public void run() {
            load();
            hReloadSortie.postDelayed(this, 60 * 1000);
        }
    };

    public void load() {
        try {
            JSONArray sortie = MenuActivity.warframeWorldState.getSorties();
            sortieClass = new SortieClass(getActivity(), sortie.getJSONObject(0)); // on l'instancie
            sortieStepList.clear();
            for (int i = 0; i < sortieClass.get_sortie_lenght(); i++) {
                sortieStepList.add(sortieClass.get_step(i)); // on ajoute les étapes à l'interface
            }
        } catch (Exception ex){
            Log.e(CurrentFileName,"Cannot read sortie | " + ex.getMessage());
        }
    }
}
