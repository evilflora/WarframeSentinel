package com.evilflora.warframesentinel.Modele;

import android.util.Log;

import org.json.JSONObject;

public class VoidTraderItemClass {
    private String _item_name;
    private int _ducat_price;
    private int _credit_price;

    public VoidTraderItemClass(JSONObject void_trader_items) { // constructor
        try {
            this._item_name     = void_trader_items.getString("ItemType");
            this._ducat_price   = void_trader_items.getInt("PrimePrice");
            this._credit_price  = void_trader_items.getInt("RegularPrice");
        } catch (Exception e) {
            Log.e("VoidTraderClass","Void Trader Data Error");
        }
    }

    public String get_item_name() { return _item_name.substring(_item_name.lastIndexOf("/")).replace("/","");}

    public int get_ducat_price() { return _ducat_price;}

    public int get_credit_price() { return _credit_price;}
}