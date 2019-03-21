package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guill on 20/03/2019 for WarframeSentinel
 */

class ProjectConstructionClass {
    /**
     * Normalement un tableau de 2 mais parfois 3
     * 1) Fomorian
     * 2) Razorback
     * 3) Relay
     */
    final String CurrentFileName = "ProjectConstructionClas";
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

    int getProjectPct(int i) {
        return _ProjectPct.get(i);
    }
}
