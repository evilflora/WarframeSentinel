package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class VoidTraderItemClass {
    private static String _currentFileName = "VoidTraderClass";
    private Context _context;
    private String _itemName;
    private int _ducatPrice;
    private int _creditPrice;

    /**
     * Return time left before reset of bounty
     *
     * @param context           Activity context
     * @param item              The JSONArray containing data
     */
    public VoidTraderItemClass(Context context,JSONObject item) {
        try {
            this._context       = context;
            this._itemName     = item.getString("ItemType");
            this._ducatPrice   = item.getInt("PrimePrice");
            this._creditPrice  = item.getInt("RegularPrice");
        } catch (Exception e) {
            Log.e(_currentFileName,"Void Trader Data Error");
        }
    }

    /**
     * Translated item name
     *
     * @return      string
     */
    public String getItemName() {
        String item = _itemName.substring(_itemName.lastIndexOf('/')).replace("/","");
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
        return _context.getResources().getString(_context.getResources().getIdentifier("void_trader_ducat_price", "string", _context.getPackageName()), _ducatPrice);
    }

    /**
     * Translated credit price
     *
     * @return      string
     */
    public String getCreditPrice() {
        return _context.getResources().getString(_context.getResources().getIdentifier("credits", "string", _context.getPackageName()), _creditPrice);

    }
}