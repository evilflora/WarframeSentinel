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

    /**
     * Return time left before reset of bounty
     *
     * @param context           Activity context
     * @param sortie            The JSONArray containing data
     * @param credits           Reward in credits for this step
     * @param level             Current ennemies level of this step
     */
    public SortieStepClass(Context context, JSONObject sortie, int credits, String level) {
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
    public String getMissionType() {
        String missionType;
        try {
            missionType = _context.getResources().getString(_context.getResources().getIdentifier(_mission_type, "string", _context.getPackageName()));
        } catch (Exception ex) {
            missionType = _mission_type;
        }
        return missionType;
    }

    public String getCondition() {
        String condition;
        try {
            condition = _context.getResources().getString(_context.getResources().getIdentifier(_condition, "string", _context.getPackageName()));
        } catch (Exception ex) {
            condition = _condition;
        }
        return condition;
    }

    public String getLocation() {
        String location;
        try {
            location = _context.getResources().getString(_context.getResources().getIdentifier(_location, "string", _context.getPackageName()));
        } catch (Exception ex) {
            location = _location;
        }
        return location;
    }

    public String getLevel() {
        return "Level: " + _level;
    }

    public String getCredits() {
        return _credits + " Credits";
    }
}