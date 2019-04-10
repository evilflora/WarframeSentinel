package com.evilflora.warframesentinel;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

class MarketItemsClass {
    private Context _context;
    private String _type_name;
    //private long _date_activation;
    private long _date_expiration;
    //private boolean _featured;
    //private boolean _popular;
    //private int _banner_index;
    private int _discount;
    private int _regular_override;
    private int _premium_override;
    //private int _bogo_buy;
    //private int _bogo_get;

    MarketItemsClass(Context context, JSONObject market_item) { // constructor
        try {
            this._context           = context;
            this._type_name         = market_item.getString("TypeName");
            //this._date_activation   = market_item.getJSONObject("StartDate").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = market_item.getJSONObject("EndDate").getJSONObject("$date").getLong("$numberLong");
            //this._featured          = market_item.getBoolean("Featured");
            //this._popular           = market_item.getBoolean("Popular");
            //this._banner_index      = market_item.getInt("BannerIndex");
            this._discount          = market_item.getInt("Discount");
            this._regular_override  = market_item.getInt("RegularOverride"); // prix en crédit
            this._premium_override  = market_item.getInt("PremiumOverride"); // prix en plat
            //this._bogo_buy          = market_item.getInt("BogoBuy");
            //this._bogo_get          = market_item.getInt("BogoGet");
        } catch (Exception ex) {
            Log.e("CetusClass","Error while reading cetus bounties - " + ex.getMessage());
        }
    }

    /**
     * Item name
     *
     * @return      string
     */
    String get_item_name() {
        String type_name = _type_name.substring(_type_name.lastIndexOf("/")).replace("/","");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(type_name, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return type_name;
        }
    }

    /**
     * True if price is in credits // todo i'm not sure
     *
     * @return      boolean
     */
    Boolean is_regular_override() { return _regular_override == 1; }

    /**
     * Percent discount of item
     *
     * @return      int
     */
    int get_discount() { return _discount;}

    /**
     * Price is in plats // todo
     *
     * @return      int
     */
    int get_premium_override() {return _premium_override; }

    /**
     * Price in credits // todo
     *
     * @return      int
     */
    int get_regular_override() { return _regular_override; }

    /**
     * Time left before the expiry of discounted item
     *
     * @return      string
     */
    public String get_time_before_expiry() { return TimestampToDate.convert(_date_expiration - System.currentTimeMillis(),true); }

}