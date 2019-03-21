package com.evilflora.warframesentinel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;

public class EventFragment extends Fragment {

    final String CurrentFileName = "AlertFragment";
    ProjectConstructionClass _projectPct;
    ProjectConstructionView adapterEvents; // La liste customisé basé sur le layout alerte_element
    ListView listViewEvents;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_content, container, false);
        try {
            JSONArray _ProjectPct = MenuActivity.warframeWorldState.getProjectPct(); // on récupère la liste des alertes

            _projectPct = new ProjectConstructionClass(_ProjectPct);
            getActivity().setTitle(getString(R.string.alerts));

            listViewEvents = view.findViewById(R.id.listView_events); // on récupère la liste
            adapterEvents = new ProjectConstructionView(getContext(), _projectPct); // todo : je ne sais plus
            listViewEvents.setAdapter(adapterEvents); // todo : la meme

            adapterEvents.notifyDataSetChanged(); // met à jour la liste
        } catch (Exception ex) {
            Log.e(CurrentFileName,"Cannot add events | " + ex.getMessage());
        }


        return view;
    }

}
