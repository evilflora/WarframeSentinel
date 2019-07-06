package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import com.evilflora.warframesentinel.Utils.NumberToTimeLeft;

import org.json.JSONObject;

public class DarvoDealsClass {
    private static String _currentFileName = "DarvoDealsClass";
    private Context _context;
    private String _storedItem;
    //private long _date_activation;
    private long _dateExpiration;
    private int _discount;
    private int _originalPrice;
    private int _salePrice;
    private int _amountTotal;
    private int _amountSold;

    /**
     * Returns item name
     *
     * @param context       Activity context
     * @param darvoDeal     Data
     */
    public DarvoDealsClass(Context context, JSONObject darvoDeal) {
        try {
            this._context           = context;
            this._storedItem        = darvoDeal.getString("StoreItem");
            //this._date_activation   = darvoDeal.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._dateExpiration    = darvoDeal.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._discount          = darvoDeal.getInt("Discount");
            this._originalPrice     = darvoDeal.getInt("OriginalPrice");
            this._salePrice         = darvoDeal.getInt("SalePrice");
            this._amountTotal       = darvoDeal.getInt("AmountTotal");
            this._amountSold        = darvoDeal.getInt("AmountSold");
        } catch (Exception ex) {
            Log.e(_currentFileName,"Error while reading cetus bounties - " + ex.getMessage());
        }
    }

    /**
     * Translated item name
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
     * The discount
     *
     * @return      int
     */
    private int getDiscount() { return _discount; }

    /**
     * The original price
     *
     * @return      int
     */
    private int getOriginalPrice() { return _originalPrice; }

    /**
     * The sale price
     *
     * @return      int
     */
    private int getSalePrice() { return _salePrice; }

    /**
     * Items left
     *
     * @return      int
     */
    private int getItemsLeft() {
        return (_amountTotal - _amountSold);
    }

    /**
     * Total items
     *
     * @return      int
     */
    private int getTotalItems() {
        return _amountTotal;
    }

    /**
     * Translated time before end
     *
     * @return      string
     */
    public String getTimeLeft() {
        return NumberToTimeLeft.convert(_dateExpiration - System.currentTimeMillis(),true);
    }

    /**
     * Translated discount with original and new price
     *
     * @return      string
     */
    public String getPriceAndDiscount() {
        return _context.getResources().getString(_context.getResources().getIdentifier("price_and_discount", "string", _context.getPackageName()), getDiscount(), getOriginalPrice(), getSalePrice());

    }

    /**
     * Translated current item left and total items put in sold
     *
     * @return      string
     */
    public String getItemAmountLeft() {
        return _context.getResources().getString(_context.getResources().getIdentifier("item_amount_left", "string", _context.getPackageName()), getItemsLeft(), getTotalItems());

    }

    /**
     * True if darvo deal is ended
     *
     * @return      boolean
     */
    public boolean isEnd() { return (_dateExpiration - System.currentTimeMillis()) <= 0; }
}