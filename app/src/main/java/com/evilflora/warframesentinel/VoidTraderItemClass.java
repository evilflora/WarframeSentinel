package com.evilflora.warframesentinel;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

class VoidTraderItemClass {
    private String _item_name;
    private int _ducat_price;
    private int _credit_price;

    VoidTraderItemClass(JSONObject void_trader_items) { // constructor
        try {
            this._item_name     = void_trader_items.getString("ItemType");
            this._ducat_price   = void_trader_items.getInt("PrimePrice");
            this._credit_price  = void_trader_items.getInt("RegularPrice");
        } catch (Exception e) {
            Log.e("VoidTraderClass","Void Trader Data Error");
        }
    }

    String get_item_name() { return _item_name.substring(_item_name.lastIndexOf("/")).replace("/","");}

    int get_ducat_price() { return _ducat_price;}

    int get_credit_price() { return _credit_price;}
}