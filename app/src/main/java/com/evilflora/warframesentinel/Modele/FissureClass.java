package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class FissureClass {
    private static String _currentFileName = "FissureClass";
    private Context _context;
    private String _id;
    private long _date_activation;
    private long _date_expiration;
    //private int _region;
    //private int _seed;
    private String _node;
    private String _modifier;

    public FissureClass(Context context, JSONObject fissure) {
        try {
            this._context           = context;
            this._id                = fissure.getJSONObject("_id").getString("$oid");
            this._date_activation   = fissure.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = fissure.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            //this._region            = fissure.getInt("Region");
            //this._seed              = fissure.getInt("Seed");
            this._node              = fissure.getString("Node");
            this._modifier          = fissure.getString("Modifier");
        } catch (Exception e) {
            Log.e(_currentFileName,"Cannot load fissure - " + e.getMessage());
        }
    }

    /**
     * Translated time before start or end
     *
     * @return      string
     */
    public String getTimeBeforeEnd() {
        if (System.currentTimeMillis() < _date_activation) {
            return _context.getResources().getString(_context.getResources().getIdentifier("start_in", "string", _context.getPackageName()), TimestampToTimeleft.convert(_date_activation - System.currentTimeMillis(),true));
        } else {
            return TimestampToTimeleft.convert(getTimeLeft(),true);
        }
    }

    /**
     * The type of the fissure
     *
     * @return      string
     */
    public String getModifier() {
        return _modifier;
    }

    /**
     * The translated type of the fissure
     *
     * @return      string
     */
    public String getType() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_modifier, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _modifier;
        }
    }

    /**
     * The translated nodes location
     *
     * @return      string
     */
    public String getLocation() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_node, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _node;
        }
    }

    /**
     * Time left before reset of the fissure
     *
     * @return      long
     */
    private long getTimeLeft() { return _date_expiration - System.currentTimeMillis(); }

    /**
     * True if fissure is closed
     *
     * @return      boolean
     */
    public boolean isEndOfFissure() { return (getTimeLeft() <= 0); }

    /**
     * Fissure ID
     *
     * @return      string
     */
    public String getId() {
        return _id;
    }
}