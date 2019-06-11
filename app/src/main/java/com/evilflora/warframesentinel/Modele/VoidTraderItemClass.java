package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class VoidTraderItemClass {
    private Context _context;
    private String _item_name;
    private int _ducat_price;
    private int _credit_price;

    /**
     * Return time left before reset of bounty
     *
     * @param context           Activity context
     * @param item              The JSONArray containing data
     */
    public VoidTraderItemClass(Context context,JSONObject item) {
        try {
            this._context       = context;
            this._item_name     = item.getString("ItemType");
            this._ducat_price   = item.getInt("PrimePrice");
            this._credit_price  = item.getInt("RegularPrice");
        } catch (Exception e) {
            Log.e("VoidTraderClass","Void Trader Data Error");
        }
    }

    /**
     * Translated item name
     *
     * @return      string
     */
    public String getItemName() {
        String item = _item_name.substring(_item_name.lastIndexOf('/')).replace("/","");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(item, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return item;
        }
    }

    /**
     * Translated ducat price
     *
     * @return      string
     */
    public String getDucatPrice() {
        return _context.getResources().getString(_context.getResources().getIdentifier("void_trader_ducat_price", "string", _context.getPackageName()), _ducat_price);
    }

    /**
     * Translated credit price
     *
     * @return      string
     */
    public String getCreditPrice() {
        return _context.getResources().getString(_context.getResources().getIdentifier("void_trader_credit_price", "string", _context.getPackageName()), _credit_price);

    }
}