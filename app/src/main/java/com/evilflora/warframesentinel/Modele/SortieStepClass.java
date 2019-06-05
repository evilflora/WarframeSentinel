package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class SortieStepClass {
    private String _mission_type;
    private String _condition;
    private String _location;
    //private String _tileset;
    private String _credits;
    private String _level;
    private Context _context;

    public SortieStepClass(Context context, JSONObject sortie, int credits, String level) { // constructor
        try {
            this._context            = context;
            this._mission_type       = sortie.getString("missionType");
            this._condition          = sortie.getString("modifierType");
            this._location           = sortie.getString("node");
            //this._tileset            = sortie.getString("tileset");
            this._credits            = String.valueOf(credits);
            this._level              = level;
        } catch (Exception e) {
            Log.e("SortieStepClass","Error while reading sortie steps");
        }
    }
    public String get_mission_type() {
        String mission_type;
        try {
            mission_type = _context.getResources().getString(_context.getResources().getIdentifier(_mission_type, "string", _context.getPackageName()));
        } catch (Exception ex) {
            mission_type = _mission_type;
        }
        return mission_type;
    }

    public String get_condition() {
        String condition;
        try {
            condition = _context.getResources().getString(_context.getResources().getIdentifier(_condition, "string", _context.getPackageName()));
        } catch (Exception ex) {
            condition = _condition;
        }
        return condition;
    }

    public String get_location() {
        String location;
        try {
            location = _context.getResources().getString(_context.getResources().getIdentifier(_location, "string", _context.getPackageName()));
        } catch (Exception ex) {
            location = _location;
        }
        return location;
    }

    /*public String get_tileset() {
        String tileset;
        try {
            tileset = _context.getResources().getString(_context.getResources().getIdentifier(_tileset, "string", _context.getPackageName()));
        } catch (Exception ex) {
            tileset = _tileset;
        }
        return tileset;
    }*/

    public String get_level() {
        return "Level: " + _level;
    }

    public String get_credits() {
        return _credits + " Credits";
    }
}