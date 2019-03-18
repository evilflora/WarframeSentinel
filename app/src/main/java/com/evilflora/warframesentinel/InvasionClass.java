package com.evilflora.warframesentinel;

import android.graphics.drawable.LayerDrawable;
import android.util.Log;

import org.json.JSONObject;

class InvasionClass {
    private String _id;
    private String _location;
    private int _count;
    private int _goal;
    //private String _locTag;
    private Boolean _completed;
    private String _attackerReward;
    private int _attackerRewardCount;
    private String _attackerFaction;
    private String _defenderReward;
    private int _defenderRewardCount;
    private String _defenderFaction;
    //private long _date_activation;
    private LayerDrawable _progressBar;

    InvasionClass(JSONObject invasion) { // constructor
        try {
            this._id                    = invasion.getJSONObject("_id").getString("$oid");
            this._location              = invasion.getString("Node");
            this._count                 = invasion.getInt("Count");
            this._goal                  = invasion.getInt("Goal");
            //this._locTag                = invasion.getString("LocTag");
            this._completed             = invasion.getBoolean("Completed");
            this._attackerFaction       = invasion.getJSONObject("DefenderMissionInfo").getString("faction");
            this._defenderFaction       = invasion.getJSONObject("AttackerMissionInfo").getString("faction");
            //this._date_activation       = invasion.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            try {
                this._attackerReward        = invasion.getJSONObject("AttackerReward").getJSONArray("countedItems").getJSONObject(0).getString("ItemType");
                this._attackerRewardCount   = invasion.getJSONObject("AttackerReward").getJSONArray("countedItems").getJSONObject(0).getInt("ItemCount");
            } catch (Exception ex) {
                Log.i("InvasionClass","Warning : No attacker reward");
            }

            try {
                this._defenderReward        = invasion.getJSONObject("DefenderReward").getJSONArray("countedItems").getJSONObject(0).getString("ItemType");
                this._defenderRewardCount   = invasion.getJSONObject("DefenderReward").getJSONArray("countedItems").getJSONObject(0).getInt("ItemCount");
            } catch (Exception ex) {
                Log.i("InvasionClass","Warning : No defender reward");
            }
        } catch (Exception e) {
            Log.e("InvasionClass","Error while reading invasion data");
        }
    }

    void set_progressBar(LayerDrawable layer) {
        _progressBar = layer;
    }

    LayerDrawable get_progressBar() {
        return _progressBar;
    }

    float getGoal() { return _goal * 2; }

    public float getCount() {
        return (_attackerFaction.compareTo("FC_INFESTATION") == 0 ? ((_goal - _count * -1) * 2) : (_count + _goal));
    }

    float get_percent_attacker() {
        return MinMaxForValue.valueOf(((float)Math.round(((getCount() / getGoal())*100) * 100) / 100),0,100);
    }

    float get_percent_defender() {
        return MinMaxForValue.valueOf(((float)Math.round((100.0 - get_percent_attacker()) * 100) / 100),0,100);
    }

    String get_reward_attacker() {
        return ( _attackerReward != null ? _attackerReward.substring(_attackerReward.lastIndexOf("/")).replace("/","") : null);
    }

    int get_attacker_reward_count() { return _attackerRewardCount; }

    String get_reward_defender() {
        return ( _defenderReward != null ? _defenderReward.substring(_defenderReward.lastIndexOf("/")).replace("/","") : null);
    }

    int get_defender_reward_count() { return _defenderRewardCount; }

    String get_attacker_faction() { return _attackerFaction; }

    String get_defender_faction() { return _defenderFaction; }

    String get_location() { return _location; }

    String get_id() { return _id;}

    Boolean get_completed() { return _completed; }
}