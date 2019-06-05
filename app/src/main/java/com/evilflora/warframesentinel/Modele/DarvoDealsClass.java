package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class DarvoDealsClass {
    private Context _context;
    private String _storedItem;
    //private long _date_activation;
    private long _dateExpiration;
    private int _discount;
    private int _originalPrice;
    private int _salePrice;
    private int _amountTotal;
    private int _amountSold;

    public DarvoDealsClass(Context context, JSONObject darvoDeal) { // constructor
        try {
            this._context           = context;
            this._storedItem        = darvoDeal.getString("StoreItem");
            //this._date_activation   = darvoDeal.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._dateExpiration   = darvoDeal.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._discount          = darvoDeal.getInt("Discount");
            this._originalPrice    = darvoDeal.getInt("OriginalPrice");
            this._salePrice        = darvoDeal.getInt("SalePrice");
            this._amountTotal      = darvoDeal.getInt("AmountTotal");
            this._amountSold       = darvoDeal.getInt("AmountSold");
        } catch (Exception ex) {
            Log.e("CetusClass","Error while reading cetus bounties - " + ex.getMessage());
        }
    }

    /**
     * Returns item name
     *
     * @return      string
     */
    public String getItemName() {
        String itemName = _storedItem.substring(_storedItem.lastIndexOf('/')).replace("/","");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(itemName, "string", _context.getPackageName()));
        } catch (Exception e) {
            return itemName;
        }
    }

    /**
     * Returns the discount
     *
     * @return      int
     */
    public int getDiscount() { return _discount; }

    /**
     * Returns the original price
     *
     * @return      int
     */
    public int getOriginalPrice() { return _originalPrice; }

    /**
     * Returns the sale price
     *
     * @return      int
     */
    public int getSalePrice() { return _salePrice; }

    /**
     * Returns items left
     *
     * @return      int
     */
    public int getItemsLeft() {
        return (_amountTotal - _amountSold);
    }

    /**
     * Returns total item
     *
     * @return      int
     */
    public int getTotalItems() {
        return _amountTotal;
    }

    /**
     * Returns item discount expiry timer
     *
     * @return      string
     */
    public String getTimeLeft() {
        return TimestampToTimeleft.convert(_dateExpiration - System.currentTimeMillis(),true);
    }

}