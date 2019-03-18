package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONObject;

class AlertClass {
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

    AlertClass(JSONObject alert) { // constructor
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

    public String get_time_before_expiry() {
        return ( System.currentTimeMillis() < _date_activation ? "Start in " + TimestampToDate.convert(_date_activation - System.currentTimeMillis(),true): TimestampToDate.convert((_date_expiration - System.currentTimeMillis()),true));
    }

    String get_mission_type() { return _missionType; }

    String get_faction() { return _faction; }

    String get_location() { return _location; }

    String get_ennemy_level() { return _ennemy_level_min + " - " + _ennemy_level_max; }

    boolean end_of_alert() { return (_date_expiration - System.currentTimeMillis()) <= 0; }

    long get_time_before_end_of_alert() { return _date_expiration - System.currentTimeMillis();}

    String get_id() { return _id; }

    String get_reward_item_name() { return (_reward_item_name != null ? _reward_item_name.substring(_reward_item_name.lastIndexOf("/")).replace("/","") : null);}

    int get_reward_item_quantity() { return _reward_item_quantity; }

    int get_reward_credits() { return _reward_credits; }
}