package com.evilflora.warframesentinel;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

class FissureClass {
    private Context _context;
    private String _id;
    private long _date_activation;
    private long _date_expiration;
    //private int _region;
    //private int _seed;
    private String _node;
    private String _modifier;

    FissureClass(Context context, JSONObject fissure) { // constructor
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
            Log.e("App","Alert Data Error");
        }
    }

    public String get_time_before_expiry() {
        return ( System.currentTimeMillis() < _date_activation ? "Start in " + TimestampToDate.convert(_date_activation - System.currentTimeMillis(),true): TimestampToDate.convert((_date_expiration - System.currentTimeMillis()),true));
    }

    String get_modifier() {
        return _modifier;
    }

    String get_type() {
        String type;
        try {
            type = _context.getResources().getString(_context.getResources().getIdentifier(_modifier, "string", _context.getPackageName()));
        } catch (Exception ex) {
            type = _modifier;
        }
        return type;
    }

    String get_location() {
        String location;
        try {
            location = _context.getResources().getString(_context.getResources().getIdentifier(_node, "string", _context.getPackageName()));
        } catch (Exception ex) {
            location = _node;
        }
        return location;    }

    boolean end_of_fissure() { return (_date_expiration - System.currentTimeMillis()) <= 0; }

    String get_id() {
        return _id;
    }
}