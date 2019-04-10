package com.evilflora.warframesentinel;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

import org.json.JSONObject;

class InvasionClass {
    private final String CurrentFileName = "InvasionClass"; // le nom du fichier
    private Context _context;
    private String _id;
    //private String _faction;
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

    InvasionClass(Context context,JSONObject invasion) { // constructor
        try {
            this._context               = context;
            this._id                    = invasion.getJSONObject("_id").getString("$oid");
            //this._faction               = invasion.getString("Faction");
            this._location              = invasion.getString("Node");
            this._count                 = invasion.getInt("Count");
            this._goal                  = invasion.getInt("Goal");
            //this._locTag                = invasion.getString("LocTag"); // in-game kinematic tooltip
            this._completed             = invasion.getBoolean("Completed");
            this._attackerFaction       = invasion.getJSONObject("DefenderMissionInfo").getString("faction");
            this._defenderFaction       = invasion.getJSONObject("AttackerMissionInfo").getString("faction");
            //this._date_activation       = invasion.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            try {
                this._attackerReward        = invasion.getJSONObject("AttackerReward").getJSONArray("countedItems").getJSONObject(0).getString("ItemType");
                this._attackerRewardCount   = invasion.getJSONObject("AttackerReward").getJSONArray("countedItems").getJSONObject(0).getInt("ItemCount");
            } catch (Exception ex) {
                Log.i(CurrentFileName,"Warning : No attacker reward");
            }

            try {
                this._defenderReward        = invasion.getJSONObject("DefenderReward").getJSONArray("countedItems").getJSONObject(0).getString("ItemType");
                this._defenderRewardCount   = invasion.getJSONObject("DefenderReward").getJSONArray("countedItems").getJSONObject(0).getInt("ItemCount");
            } catch (Exception ex) {
                Log.i(CurrentFileName,"Warning : No defender reward");
            }
        } catch (Exception e) {
            Log.e(CurrentFileName,"Error while reading invasion data");
        }
    }

    /**
     * Associates the progress bar with the class
     */
    void set_progressBar(LayerDrawable layer) {
        _progressBar = layer;
    }

    /**
     * Return progress bar
     *
     * @return      layer drawable
     */
    LayerDrawable get_progressBar() {
        return _progressBar;
    }

    /**
     * Return the goal of the attacker
     *
     * @return      float
     */
    float get_goal() { return _goal * 2; }

    /**
     * Returns the attacker's progress
     *
     * @return      float
     */
    float get_count() {
        return (_attackerFaction.compareTo("FC_INFESTATION") == 0 ? ((_goal - _count * -1) * 2) : (_count + _goal));
    }

    /**
     * Returns the progress percentage of the attacker
     *
     * @return      float
     */
    float get_percent_attacker() {
        return MinMaxForValue.valueOf(((float)Math.round(((get_count() / get_goal())*100) * 100) / 100),0,100);
    }

    /**
     * Returns the percentage of progression of the defender
     *
     * @return      float
     */
    float get_percent_defender() {
        return MinMaxForValue.valueOf(((float)Math.round((100.0 - get_percent_attacker()) * 100) / 100),0,100);
    }

    /**
     * Returns the code name of the reward item if the attacker wins
     *
     * @return      string
     */
    String get_reward_attacker() {

        String reward = ( _attackerReward != null ? _attackerReward.substring(_attackerReward.lastIndexOf("/")).replace("/","") : "");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(reward, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return reward;
        }
    }

    /**
     * Returns the quantity of the item as a reward if the attacker wins
     *
     * @return      int
     */
    int get_attacker_reward_count() { return _attackerRewardCount; }

    /**
     * Returns the code name of the reward item if the defender wins
     *
     * @return      string
     */
    String get_reward_defender() {
        String reward = ( _defenderReward != null ? _defenderReward.substring(_defenderReward.lastIndexOf("/")).replace("/","") : "");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(reward, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return reward;
        }
    }

    /**
     * Returns the quantity of the item as a reward if the defender wins
     *
     * @return      int
     */
    int get_defender_reward_count() { return _defenderRewardCount; }

    /**
     * Returns the code name of the attacker
     *
     * @return      string
     */
    String get_attacker_faction() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_attackerFaction, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return _attackerFaction;
        }
    }

    /**
     * Returns the code name of the defender
     *
     * @return      string
     */
    String get_defender_faction() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_defenderFaction, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return _defenderFaction;
        }
    }

    /**
     * Returns the code name of the node
     *
     * @return      string
     */
    String get_location() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_location, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return _location;
        }
    }

    /**
     * Return the Invasion ID
     *
     * @return      string
     */
    String get_id() { return _id;}

    /**
     * Indicates if the invasion has been completed
     *
     * @return      boolean
     */
    Boolean is_completed() { return _completed; }

}