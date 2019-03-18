package com.evilflora.warframesentinel;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class VoidTraderClass {
    Context _context;
    private String _id;
    private long _date_activation;
    private long _date_expiration;
    private String _character;
    private String _node;
    private List<VoidTraderItemClass> _items;


    VoidTraderClass(Context context, JSONObject void_trader) { // constructor
        _items = new ArrayList<>();
        try {
            this._context           = context;
            this._id                = void_trader.getJSONObject("_id").getString("$oid");
            this._date_activation   = void_trader.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = void_trader.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._character         = void_trader.getString("Character");
            this._node              = void_trader.getString("Node");
            for (int i =0; i < void_trader.getJSONArray("Manifest").length(); i++) {
                this._items.add(new VoidTraderItemClass(void_trader.getJSONArray("Manifest").getJSONObject(i)));
            }
        } catch (Exception e) {
            Log.e("VoidTraderClass","Void Trader Data Error");
        }
    }

    public String get_time_before_expiry() {
        return ( System.currentTimeMillis() < _date_activation ? "Start in " + TimestampToDate.convert(_date_activation - System.currentTimeMillis(),true): TimestampToDate.convert((_date_expiration - System.currentTimeMillis()),true));
    }

    boolean end_of_alert() { return (_date_expiration - System.currentTimeMillis()) <= 0; }

    long get_time_before_end() { return _date_expiration - System.currentTimeMillis();}

    String get_location() {
        String location;
        try {
            location = _context.getResources().getString(_context.getResources().getIdentifier(_node, "string", _context.getPackageName()));
        } catch (Exception ex) {
            location = _node;
        }
        return location;
    }

    List<VoidTraderItemClass> get_items() { return _items;}
}