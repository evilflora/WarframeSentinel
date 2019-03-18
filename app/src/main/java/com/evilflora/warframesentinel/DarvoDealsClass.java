package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONObject;

class DarvoDealsClass {
    private String _store_item;
    //private long _date_activation;
    private long _date_expiration;
    private int _discount;
    private int _original_price;
    private int _sale_price;
    private int _amount_total;
    private int _amount_sold;

    DarvoDealsClass(JSONObject darvo_deal) { // constructor
        try {
            this._store_item        = darvo_deal.getString("StoreItem");
            //this._date_activation   = darvo_deal.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = darvo_deal.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._discount          = darvo_deal.getInt("Discount");
            this._original_price    = darvo_deal.getInt("OriginalPrice");
            this._sale_price        = darvo_deal.getInt("SalePrice");
            this._amount_total      = darvo_deal.getInt("AmountTotal");
            this._amount_sold       = darvo_deal.getInt("AmountSold");
        } catch (Exception ex) {
            Log.e("CetusClass","Error while reading cetus bounties - " + ex.getMessage());
        }
    }

    String get_item_name() {
        return _store_item.substring(_store_item.lastIndexOf("/")).replace("/","");
    }

    String get_reduction() {
        return _discount + "%" + " " + _original_price + " -> " + _sale_price;
    }

    String get_items_left() {
        return String.valueOf(_amount_total - _amount_sold) + "/" + _amount_total;
    }

    public String get_time_before_expiry() {
        return TimestampToDate.convert(_date_expiration - System.currentTimeMillis(),true);
    }

}