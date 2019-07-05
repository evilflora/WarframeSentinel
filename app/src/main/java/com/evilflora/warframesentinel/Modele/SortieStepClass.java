package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class SortieStepClass {
    private String _missionType;
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
            this._missionType       = sortie.getString("missionType");
            this._condition          = sortie.getString("modifierType");
            this._location           = sortie.getString("node");
            //this._tileset            = sortie.getString("tileset");
            this._credits            = String.valueOf(credits);
            this._level              = level;
        } catch (Exception e) {
            Log.e("SortieStepClass","Error while reading sortie steps");
        }
    }

    /**
     * Translated mission type
     *
     * @return      string
     */
    public String getMissionType() {
        String missionType;
        try {
            missionType = _context.getResources().getString(_context.getResources().getIdentifier(_missionType, "string", _context.getPackageName()));
        } catch (Exception ex) {
            missionType = _missionType;
        }
        return missionType;
    }

    /**
     * Translated condition of the mission
     *
     * @return      string
     */
    public String getCondition() {
        String condition;
        try {
            condition = _context.getResources().getString(_context.getResources().getIdentifier(_condition, "string", _context.getPackageName()));
        } catch (Exception ex) {
            condition = _condition;
        }
        return condition;
    }

    /**
     * Translated location
     *
     * @return      string
     */
    public String getLocation() {
        String location;
        try {
            location = _context.getResources().getString(_context.getResources().getIdentifier(_location, "string", _context.getPackageName()));
        } catch (Exception ex) {
            location = _location;
        }
        return location;
    }

    /**
     * Translated level range
     *
     * @return      string
     */
    public String getLevel() {
        return _context.getResources().getString(_context.getResources().getIdentifier("sortie_level", "string", _context.getPackageName()), _level);
    }

    /**
     * Translated credits reward
     *
     * @return      string
     */
    public String getCredits() {
        return _context.getResources().getString(_context.getResources().getIdentifier("credits", "string", _context.getPackageName()), _credits);
    }
}