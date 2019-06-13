package com.evilflora.warframesentinel.Modele;

/*
 * Created by guill on 09/06/2019 for WarframeSentinel
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class AlertClass {
    private static String _currentFileName = "AlertClass";
    private Context _context;

    private String _id;
    private long _date_activation;
    private long _date_expiration;
    private String _missionType;
    private String _faction;
    private String _location;
    private int _ennemy_level_min;
    private int _ennemy_level_max;
    private int _reward_credits;
    private String _reward_item_name;
    private int _reward_item_quantity;

    public AlertClass(Context context, JSONObject alert) {
        this._context = context;
        try {
            this._id                = alert.getJSONObject("_id").getString("$oid");
            this._date_activation   = alert.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = alert.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._missionType       = alert.getJSONObject("MissionInfo").getString("missionType");
            this._faction           = alert.getJSONObject("MissionInfo").getString("faction");
            this._location          = alert.getJSONObject("MissionInfo").getString("location");
            this._ennemy_level_min  = alert.getJSONObject("MissionInfo").getInt("minEnemyLevel");
            this._ennemy_level_max  = alert.getJSONObject("MissionInfo").getInt("maxEnemyLevel");
            this._reward_credits    = alert.getJSONObject("MissionInfo").getJSONObject("missionReward").getInt("credits");
            try { // on suppose qu'il n'y a qu'une récompense à la fois
                this._reward_item_name = alert.getJSONObject("MissionInfo").getJSONObject("missionReward").getJSONArray("items").getString(0);
            } catch (Exception ex) {
                //Log.i("AlertClass",_id + " has no items");
            }
            try { // on suppose qu'il n'y a qu'une récompense à la fois
                this._reward_item_name = alert.getJSONObject("MissionInfo").getJSONObject("missionReward").getJSONArray("countedItems").getJSONObject(0).getString("ItemType");
                this._reward_item_quantity = alert.getJSONObject("MissionInfo").getJSONObject("missionReward").getJSONArray("countedItems").getJSONObject(0).getInt("ItemCount");
            } catch (Exception ex) {
                //Log.i("AlertClass",_id + " has no rewards");
            }
        } catch (Exception e) {
            Log.e("AlertClass","Alert Data Error");
        }
    }

    public String getTimeBeforeExpiry() {
        if (System.currentTimeMillis() < _date_activation) {
            return _context.getResources().getString(_context.getResources().getIdentifier("start_in", "string", _context.getPackageName()), TimestampToTimeleft.convert(_date_activation - System.currentTimeMillis(),true));
        } else {
            return TimestampToTimeleft.convert(getTimeLeft(),true);
        }
    }

    public String getMissionType() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_missionType, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _missionType;
        }
    }

    public String getFaction() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_faction, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _faction;
        }
    }

    public String getLocation() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_location, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _location;
        }
    }

    public String getEnnemyLevel() { return _ennemy_level_min + " - " + _ennemy_level_max; }

    public boolean isEndOfAlert() { return getTimeLeft() <= 0; }

    public long getTimeLeft() { return _date_expiration - System.currentTimeMillis();}

    public String getId() { return _id; }

    public String getRewardItemName() {
        if (_reward_item_name != null) {
            try {
                return _context.getResources().getString(_context.getResources().getIdentifier(_reward_item_name, "string", _context.getPackageName()));
            } catch (Exception ex) {
                return _reward_item_name;
            }
        } else return null;
    }

    public int getRewardItemQuantity() { return _reward_item_quantity; }

    public int getRewardCredits() { return _reward_credits; }
}