package com.evilflora.warframesentinel.Modele;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ProjectConstructionClass {
    /**
     * 1) Fomorian
     * 2) Razorback
     * 3) ???, can be relays but there is a Construction Projects
     */
    private static String _currentFileName = "ProjectConstructionClas";
    private List<Integer> _ProjectPct = new ArrayList<>(3);

    public ProjectConstructionClass(JSONArray project) {
        try {
            for(int i = 0; i < project.length(); i++) {
                _ProjectPct.add(project.getInt(i));
            }
        } catch (Exception e) {
            Log.e(_currentFileName, "Error while reading construction project data");
        }
    }
    /**
     * percentage of construction progress
     *
     * @return      int
     */
    public int getProjectPct(int i) {
        return _ProjectPct.get(i);
    }
}
