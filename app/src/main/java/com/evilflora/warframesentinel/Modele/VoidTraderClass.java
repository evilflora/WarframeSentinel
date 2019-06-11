package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class VoidTraderClass {
    private static String _currentFileName = "VoidTraderClass";
    Context _context;
    //private String _id;
    private long _date_activation;
    private long _date_expiration;
    //private String _character;
    private String _node;
    private List<VoidTraderItemClass> _items;

    /**
     * Return time left before reset of bounty
     *
     * @param context           Activity context
     * @param deal             The JSONArray containing data
     */
    public VoidTraderClass(Context context, JSONArray deal) {
        _items = new ArrayList<>();
        try {
            this._context           = context;
            //this._id                = deal.getJSONObject(0).getJSONObject("_id").getString("$oid");
            this._date_activation   = deal.getJSONObject(0).getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = deal.getJSONObject(0).getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            //this._character         = void_trader.getString("Character");
            this._node              = deal.getJSONObject(0).getString("Node");
            for (int i =0; i < deal.getJSONObject(0).getJSONArray("Manifest").length(); i++) {
                this._items.add(new VoidTraderItemClass(context, deal.getJSONObject(0).getJSONArray("Manifest").getJSONObject(i)));
            }
        } catch (Exception e) {
            Log.e(_currentFileName,"Void Trader Data Error");
        }
    }

    /**
     * Translated time left before reset of the void trader
     *
     * @return      string
     */
    public String getTimeBeforeEnd() {
        if (System.currentTimeMillis() < _date_activation) {
            return _context.getResources().getString(_context.getResources().getIdentifier("start_in", "string", _context.getPackageName()), TimestampToTimeleft.convert(_date_activation - System.currentTimeMillis(),true));
        } else {
            return _context.getResources().getString(_context.getResources().getIdentifier("end_in", "string", _context.getPackageName()), TimestampToTimeleft.convert(getTimeLeft(),true));
        }
    }

    /**
     * Time left end of the void
     *
     * @return      long
     */
    public long getTimeLeft() { return _date_expiration - System.currentTimeMillis();}

    /**
     * Translated node location
     *
     * @return      string
     */
    public String getLocation() {
        String location;
        try {
            location = _context.getResources().getString(_context.getResources().getIdentifier(_node, "string", _context.getPackageName()));
        } catch (Exception ex) {
            location = _node;
        }
        return location;
    }

    /**
     * All items
     *
     * @return      List<VoidTraderItemClass>
     */
    public List<VoidTraderItemClass> getItems() { return _items;}
}