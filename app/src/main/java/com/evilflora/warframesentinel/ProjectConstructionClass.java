package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

class ProjectConstructionClass {
    /**
     * 1) Fomorian
     * 2) Razorback
     * 3) ???, can be relays but there is a Construction Projects
     */
    private final String CurrentFileName = "ProjectConstructionClas";
    private List<Integer> _ProjectPct = new ArrayList<>(3);

    ProjectConstructionClass(JSONArray project) {
        try {
            for(int i = 0; i < project.length(); i++) {
                _ProjectPct.add(project.getInt(i));
            }
        } catch (Exception e) {
            Log.e(CurrentFileName, "Error while reading construction project data");
        }
    }
    /**
     * percentage of construction progress
     *
     * @return      int
     */
    int getProjectPct(int i) {
        return _ProjectPct.get(i);
    }
}
