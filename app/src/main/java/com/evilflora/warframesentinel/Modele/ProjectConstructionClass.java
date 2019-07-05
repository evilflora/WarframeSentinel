package com.evilflora.warframesentinel.Modele;

import android.content.Context;
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
    private static String _currentFileName = "ProjectConstructionClass";
    private List<Integer> _ProjectPct = new ArrayList<>(3);
    private Context _context;

    public ProjectConstructionClass(Context context, JSONArray project) {
        this._context = context;
        try {
            for(int i = 0; i < project.length(); i++) {
                _ProjectPct.add(project.getInt(i));
            }
        } catch (Exception e) {
            Log.e(_currentFileName, "Error while reading construction project data");
        }
    }

    /**
     * Percentage of construction progress
     *
     * @return      int
     */
    public int getProjectPct(int i) {
        return _ProjectPct.get(i);
    }

    /**
     * Translated project progress
     *
     * @return      string
     */
    public String getProjectProgress(int i) {
        return _context.getResources().getString(_context.getResources().getIdentifier("project_progress", "string", _context.getPackageName()), _ProjectPct.get(i));
    }
}
