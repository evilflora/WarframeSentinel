package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class FissureClass {
    private final String CurrentFileName = "FissureClass";
    private Context _context;
    private String _id;
    private long _date_activation;
    private long _date_expiration;
    //private int _region;
    //private int _seed;
    private String _node;
    private String _modifier;

    public FissureClass(Context context, JSONObject fissure) { // constructor
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
            Log.e(CurrentFileName,"Cannot load fissure - " + e.getMessage());
        }
    }

    /**
     * Return fissure timeleft before start or end
     *
     * @return      string
     */
    public String get_time_before_expiry() {
        // todo le Start in, ne semble pas fonctionner, sauf si pas de délai ?
        if (System.currentTimeMillis() < _date_activation) Log.i(CurrentFileName,"" + System.currentTimeMillis() + "<" + _date_activation);

        return ( System.currentTimeMillis() < _date_activation ? "Start in " + TimestampToTimeleft.convert(_date_activation - System.currentTimeMillis(),true): TimestampToTimeleft.convert((_date_expiration - System.currentTimeMillis()),true));
    }

    /**
     * Returns the type of the fissure
     *
     * @return      string
     */
    public String get_modifier() {
        return _modifier;
    }

    /**
     * Returns the translated type of the fissure
     *
     * @return      string
     */
    public String get_type() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_modifier, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _modifier;
        }
    }

    /**
     * Returns the nodes location
     *
     * @return      string
     */
    public String get_location() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_node, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _node;
        }
    }

    /**
     * Retourne true if fissure is closed
     *
     * @return      boolean
     */
    public boolean is_end_of_fissure() { return (_date_expiration - System.currentTimeMillis()) <= 0; }

    /**
     * Retourne fissure ID
     *
     * @return      string
     */
    public String get_id() {
        return _id;
    }
}