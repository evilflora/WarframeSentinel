package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import com.evilflora.warframesentinel.Utils.NumberToTimeLeft;

import org.json.JSONObject;

public class MarketItemsClass {
    private Context _context;
    private String _typeName;
    //private long _dateActivation;
    private long _dateExpiration;
    //private boolean _featured;
    //private boolean _popular;
    //private int _bannerIndex;
    private int _discount;
    private int _regularOverride;
    private int _premiumOverride;
    //private int _bogoBuy;
    //private int _bogoGet;

    /**
     * Returns item name
     *
     * @param context       Activity context
     * @param marketItem    Data
     */
    public MarketItemsClass(Context context, JSONObject marketItem) {
        try {
            this._context           = context;
            this._typeName         = marketItem.getString("TypeName");
            //this._dateActivation   = marketItem.getJSONObject("StartDate").getJSONObject("$date").getLong("$numberLong");
            this._dateExpiration   = marketItem.getJSONObject("EndDate").getJSONObject("$date").getLong("$numberLong");
            //this._featured          = marketItem.getBoolean("Featured");
            //this._popular           = marketItem.getBoolean("Popular");
            //this._banner_index      = marketItem.getInt("BannerIndex");
            this._discount          = marketItem.getInt("Discount");
            this._regularOverride  = marketItem.getInt("RegularOverride"); // prix en crédit
            this._premiumOverride  = marketItem.getInt("PremiumOverride"); // prix en plat
            //this._bogo_buy          = marketItem.getInt("BogoBuy");
            //this._bogo_get          = marketItem.getInt("BogoGet");
        } catch (Exception ex) {
            Log.e("CetusClass","Error while reading cetus bounties - " + ex.getMessage());
        }
    }

    /**
     * Item name
     *
     * @return      string
     */
    public String getItemName() {
        String typeName = _typeName.substring(_typeName.lastIndexOf('/')).replace("/","");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(typeName, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return typeName;
        }
    }

    /**
     * True if price is in credits
     *
     * @return      boolean
     */
    private Boolean isRegularOverride() { return _regularOverride >= 1; }

    /**
     * Translated time left before the end if the discount
     *
     * @return      string
     */
    public String getTimeBeforeEnd() { return NumberToTimeLeft.convert(_dateExpiration - System.currentTimeMillis(),true); }


    /**
     * True if darvo deal is ended
     *
     * @return      boolean
     */
    public boolean isEnd() { return (_dateExpiration - System.currentTimeMillis()) <= 0; }

    /**
     * Translated discount
     *
     * @return      string
     */
    public String getReduction() // todo need to search string and wtf is that RegularOverride
    {
        if (isRegularOverride()) {
            return String.format("%s %s",_regularOverride, _context.getResources().getString(_context.getResources().getIdentifier("credits", "string", _context.getPackageName())));
        } else if (_discount > 0) {
            return String.format("%s%% %s %s",_discount, _premiumOverride , _context.getResources().getString(_context.getResources().getIdentifier("plats", "string", _context.getPackageName())));
        } else {
            return String.format("%s %s",_premiumOverride , _context.getResources().getString(_context.getResources().getIdentifier("plats", "string", _context.getPackageName())));
        }
    }
}