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
import java.util.List;

public class SyndicateFragment extends Fragment {

    final String CurrentFileName = "SyndicateFragment";
    List<List<SyndicateClass>> syndicateList = new ArrayList<>(); // Liste des invasions
    List<SyndicateListView> adapterSyndicateList; // La liste customisé basé sur le layout alerte_element
    JSONArray syndicate;
    List<String> _tabHostContent;
    Handler hTimerSyndicate = new Handler();
    Handler hReloadSyndicate = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.syndicate_content, container, false);
        syndicateList.clear();
        getActivity().setTitle(getString(R.string.syndicate));

        // Tabs
        _tabHostContent = Arrays.asList("all", "SteelMeridianSyndicate", "ArbitersSyndicate" , "CephalonSudaSyndicate", "PerrinSyndicate", "RedVeilSyndicate", "NewLokaSyndicate");
        int[] _idLinear = {R.id.syndicate_all, R.id.syndicate_steel_meridian,R.id.syndicate_arbiter_of_hexis,R.id.syndicate_cephalon_suda,R.id.syndicate_perrin_sequence,R.id.syndicate_red_veil,R.id.syndicate_new_loka};
        int[] _idListView = {R.id.listView_all,R.id.listView_steel_meridian,R.id.listView_arbiter_of_hexis, R.id.listViewcephalon_suda,  R.id.listView_perrin_sequence, R.id.listView_red_veil, R.id.listView_new_loka};
        TabHost tabHost = view.findViewById(R.id.tabHost_syndicate);
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
        adapterSyndicateList = new ArrayList<>(_tabHostContent.size()); // on, créé une liste d'adapter de la taille du nombre de types de syndicate plus la catégorie all
        List<ListView> listView = new ArrayList<>(_tabHostContent.size());
        syndicate = MenuActivity.warframeWorldState.getShipSyndicateMissions();
        for (int i = 0; i < _tabHostContent.size(); i++) {
                syndicateList.add(new ArrayList<>());
                adapterSyndicateList.add(i,new SyndicateListView(getActivity(), syndicateList.get(i)));
                listView.add(i, view.findViewById(_idListView[i]));
                listView.get(i).setAdapter(adapterSyndicateList.get(i));
        }
        // end adapter

        // Data load
        for(int i = 0; i < syndicate.length(); i++){ // pour le nombre de syndicats
            try {
                SyndicateClass tmp = new SyndicateClass(getActivity(),syndicate.getJSONObject(i));
                for(int j = 0; j < tmp.get_nodes_size(); j++) { // pour le nombre de missions par syndicat
                    syndicateList.get(0).add(tmp); // Ajout dans l'onglet all
                    syndicateList.get(_tabHostContent.indexOf(tmp.get_tag())).add(tmp); // Ajout dans son onglet spécifique
                }
            } catch (JSONException e) {
                Log.e(CurrentFileName,e.getMessage());
            }
        }
        // End data load

        hTimerSyndicate.post(runnableSyndicate); // On rafraichis toutes les secondes les timers
        hReloadSyndicate.post(runnableReloadSyndicate); // On rafraichis toutes les secondes les timers

        return view;
    }

    private Runnable runnableSyndicate = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < syndicateList.size();i++) {
                for(int j = 0; j < syndicateList.get(i).size(); j++) {
                    if (syndicateList.get(i).get(j).end_of_syndicate()) {
                        syndicateList.get(i).remove(j);
                    }
                }
            }

            for (int i = 0; i < _tabHostContent.size(); i++) {
                adapterSyndicateList.get(i).notifyDataSetChanged();
            }
            hTimerSyndicate.postDelayed(this, 1000); // todo trouver une meilleur solution avec le end_of_syndicate()
        }
    };

    private Runnable runnableReloadSyndicate = new Runnable() {
        @Override
        public void run() {
            try {
                syndicate = MenuActivity.warframeWorldState.getShipSyndicateMissions(); // on récupère la liste des alertes

                boolean stop;
                for (int i = 0; i < syndicate.length(); i++) { // on parcours la nouvelle liste (surement plus grande que l'ancienne)
                    stop = false; // on remet à false
                    for(int j = 0; j < syndicateList.get(0).size(); j++) { // on compare à l'ancienne liste
                        if(syndicate.getJSONObject(i).getJSONObject("_id").getString("$oid").compareTo(syndicateList.get(0).get(j).get_id()) == 0) {
                            stop = true; // on indique que l'on en a trouvé une
                            break; // on casse la boucle car inutile de continuer
                        }
                    }
                    if (!stop) { // si on n'a pas quitté la boucle alors c'est que cette alerte est nouvelle
                        SyndicateClass tmp = new SyndicateClass(getActivity(),syndicate.getJSONObject(i));
                        syndicateList.get(0).add(tmp); // Instancie l'alerte et l'ajoute dans la liste
                        syndicateList.get(_tabHostContent.indexOf(tmp.get_tag())).add(tmp); // Instancie l'alerte et l'ajoute dans la liste
                    }
                }
            } catch (Exception ex) {
                Log.e(CurrentFileName,"Cannot add new syndicate | " + ex.getMessage());
                ex.printStackTrace();
            }
            hReloadSyndicate.postDelayed(this, 60 * 1000);
        }
    };
}
