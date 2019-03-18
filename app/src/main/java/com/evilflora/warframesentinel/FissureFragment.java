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
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FissureFragment extends Fragment {

    final String CurrentFileName = "FissureFragment";
    List<List<FissureClass>> fissureList = new ArrayList<>(); // Liste des invasions
    List<FissureListView> adapterFissureList; // La liste customisé basé sur le layout alerte_element
    JSONArray fissures;
    List<String> _tabHostContent;
    Handler hTimerFissure = new Handler();
    Handler hReloadFissure = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fissure_content, container, false);
        fissureList.clear();
        getActivity().setTitle(getString(R.string.fissures));

        // Tabs
        _tabHostContent = Arrays.asList("all", "VoidT1", "VoidT2" , "VoidT3", "VoidT4");
        int[] _idLinear = {R.id.fissures_all, R.id.fissures_lith,R.id.fissures_meso,R.id.fissures_neo,R.id.fissures_axi};
        int[] _idListView = {R.id.listView_fissure_all,R.id.listView_fissure_lith,R.id.listView_fissure_meso,R.id.listView_fissure_neo,R.id.listView_fissure_axi};
        TabHost tabHost = view.findViewById(R.id.tabHost_fissures);
        tabHost.setup();
        TabHost.TabSpec spec;
        for (int i = 0; i < _tabHostContent.size(); i++) {
            spec = tabHost.newTabSpec(_tabHostContent.get(i));
            spec.setContent(_idLinear[i]);
            spec.setIndicator(getResources().getString(getResources().getIdentifier(_tabHostContent.get(i), "string", getActivity().getPackageName())));
            tabHost.addTab(spec);
        }
        // End tabs

        // Adapter
        adapterFissureList = new ArrayList<>(_tabHostContent.size()); // on, créé une liste d'adapter de la taille du nombre de types de fissures plus la catégorie all
        List<ListView> listView = new ArrayList<>(_tabHostContent.size());
        fissures = MenuActivity.warframeWorldState.getFissures();
        for (int i = 0; i < _tabHostContent.size(); i++) {
                fissureList.add(new ArrayList<>());
                adapterFissureList.add(i,new FissureListView(getActivity(), fissureList.get(i)));
                listView.add(i, view.findViewById(_idListView[i]));
                listView.get(i).setAdapter(adapterFissureList.get(i));
        }
        // end adapter

        // Data load
        for(int i = 0; i < fissures.length(); i++){
            try {
                FissureClass tmp = new FissureClass(getActivity(),fissures.getJSONObject(i));
                fissureList.get(0).add(tmp); // Instancie l'alerte et l'ajoute dans la liste
                fissureList.get(_tabHostContent.indexOf(tmp.get_modifier())).add(tmp); // Instancie l'alerte et l'ajoute dans la liste
            } catch (JSONException e) {
                Log.e(CurrentFileName,e.getMessage());
            }
        }
        // End data load

        hTimerFissure.post(runnableFissure); // On rafraichis toutes les secondes les timers
        hReloadFissure.post(runnableReloadFissure); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableFissure = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < fissureList.size();i++) {
                for(int j = 0; j < fissureList.get(i).size(); j++) {
                    if (fissureList.get(i).get(j).end_of_fissure()) {
                        fissureList.get(i).remove(j);
                    }
                }
            }

            for (int i = 0; i < _tabHostContent.size(); i++) {
                adapterFissureList.get(i).notifyDataSetChanged();
            }
            hTimerFissure.postDelayed(this, 1000);
        }
    };

    private Runnable runnableReloadFissure = new Runnable() {
        @Override
        public void run() {
            try {
                fissures = MenuActivity.warframeWorldState.getFissures(); // on récupère la liste des alertes

                boolean stop;
                for (int i = 0; i < fissures.length(); i++) { // on parcours la nouvelle liste (surement plus grande que l'ancienne)
                    stop = false; // on remet à false
                    for(int j = 0; j < fissureList.get(0).size(); j++) { // on compare à l'ancienne liste
                        if(fissures.getJSONObject(i).getJSONObject("_id").getString("$oid").compareTo(fissureList.get(0).get(j).get_id()) == 0) {
                            stop = true; // on indique que l'on en a trouvé une
                            break; // on casse la boucle car inutile de continuer
                        }
                    }
                    if (!stop) { // si on n'a pas quitté la boucle alors c'est que cette alerte est nouvelle
                        FissureClass tmp = new FissureClass(getActivity(),fissures.getJSONObject(i));
                        fissureList.get(0).add(tmp); // Instancie l'alerte et l'ajoute dans la liste
                        fissureList.get(_tabHostContent.indexOf(tmp.get_modifier())).add(tmp); // Instancie l'alerte et l'ajoute dans la liste
                    }
                    Collections.sort(fissureList.get(0),(o1, o2) -> o1.get_modifier().compareTo(o2.get_modifier()) );
                }
            } catch (Exception ex) {
                Log.e(CurrentFileName,"Cannot add new fissure | " + ex.getMessage());
                ex.printStackTrace();
            }
            hReloadFissure.postDelayed(this, 60 * 1000);
        }
    };
}
