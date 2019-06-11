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

import com.evilflora.warframesentinel.Modele.SortieClass;
import com.evilflora.warframesentinel.Modele.SortieStepClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.SortieRewardListView;
import com.evilflora.warframesentinel.Vue.SortieStepListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SortieFragment extends Fragment {

    private static String _currentFileName = "SortieFragment";
    private List<SortieStepClass> _sortieStepList = new ArrayList<>(); // Liste des sortie
    private SortieClass _sortieClass = null;
    private SortieStepListView _adapterSortie; // La liste customisé basé sur le layout alerte_element
    private SortieRewardListView _adapterRewards; // La liste customisé basé sur le layout alerte_element
    private TextView _sortie_reset_timer;
    private TextView _sortie_type;
    private Handler _hTimerSortie = new Handler();
    private Handler _hReloadSortie = new Handler();
    JSONArray _sortie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sortie_content, container, false);
        getActivity().setTitle(getString(R.string.sorties));

        // Tabs
        int[] tab = {R.id.sortie_steps, R.id.sortie_rewards};
        int[] tabContent =  {R.id.list_sortie_steps, R.id.list_sortie_rewards};
        int[] tabHostContent =  {R.string.missions, R.string.rewards};
        TabHost tabHost = view.findViewById(R.id.tabHost_sortie);
        tabHost.setup();
        TabHost.TabSpec spec;
        for (int i = 0; i < tabHostContent.length; i++) {
            spec = tabHost.newTabSpec(getString(tabHostContent[i]));
            spec.setContent(tab[i]);
            spec.setIndicator(getString(tabHostContent[i]));
            tabHost.addTab(spec);
        }
        //end tabs

        // Adapters
        _sortie = MenuActivity.warframeWorldState.getSorties();

        _sortieClass = new SortieClass(getActivity(), _sortie); // on l'instancie

        _sortie_reset_timer = view.findViewById(R.id.sortie_reset_timer);
        _sortie_type = view.findViewById(R.id.sortie_type);

        _adapterSortie = new SortieStepListView(getActivity(), _sortieStepList);
        ListView listViewSortie = view.findViewById(tabContent[0]);
        listViewSortie.setAdapter(_adapterSortie);

        _adapterRewards = new SortieRewardListView(getActivity(), _sortieClass.getRewards(),_sortieClass.getDropChance());
        ListView listViewRewards = view.findViewById(tabContent[1]);
        listViewRewards.setAdapter(_adapterRewards);
        // end adapters

        // Timers
        _hTimerSortie.post(runnableSortie);
        _hReloadSortie.post(runnableReloadSortie);
        // end timers

        return view;
    }

    private Runnable runnableSortie = new Runnable() {
        @Override
        public void run() {
            try {
                _sortie_reset_timer.setText(_sortieClass.getTimeBeforeEnd());
                _sortie_type.setText(_sortieClass.getSortieType());
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot update sortie timer | " + ex.getMessage());
            }
            _hTimerSortie.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadSortie = new Runnable() {
        @Override
        public void run() {
            try {
                _sortie = MenuActivity.warframeWorldState.getSorties();
                _sortieClass = new SortieClass(getActivity(), _sortie); // on l'instancie
                _sortieStepList.clear();
                for (int i = 0; i < _sortieClass.getSortieStepLenght(); i++) {
                    _sortieStepList.add(_sortieClass.getStep(i)); // on ajoute les étapes à l'interface
                }
                if(_adapterSortie.getCount() > 0)_adapterSortie.notifyDataSetChanged();
                if(_adapterRewards.getCount() > 0)_adapterRewards.notifyDataSetChanged();
            } catch (Exception ex){
                Log.e(_currentFileName,"Cannot read sortie | " + ex.getMessage());
            }
            _hReloadSortie.postDelayed(this, 60000);
        }
    };

}
