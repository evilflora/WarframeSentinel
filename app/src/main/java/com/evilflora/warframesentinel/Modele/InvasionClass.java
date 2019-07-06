package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

import com.evilflora.warframesentinel.Utils.MinMaxForValue;

import org.json.JSONObject;

public class InvasionClass {
    private static String _currentFileName = "InvasionClass";
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

    /**
     * Return time left before reset of bounty
     *
     * @param context           Activity context
     * @param invasion          The JSONObject containing data
     */
    public InvasionClass(Context context,JSONObject invasion) {
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
            // todo is it really possible to avoid nested try catch ? isNull, has, toString, everything throw an exception on empty JSONArray or JSONObject
            try {
                this._attackerReward        = invasion.getJSONObject("AttackerReward").getJSONArray("countedItems").getJSONObject(0).getString("ItemType");
                this._attackerRewardCount   = invasion.getJSONObject("AttackerReward").getJSONArray("countedItems").getJSONObject(0).getInt("ItemCount");
            } catch (Exception E) {

            }
            try {
                this._defenderReward        = invasion.getJSONObject("DefenderReward").getJSONArray("countedItems").getJSONObject(0).getString("ItemType");
                this._defenderRewardCount   = invasion.getJSONObject("DefenderReward").getJSONArray("countedItems").getJSONObject(0).getInt("ItemCount");
            } catch (Exception E) {

            }
        } catch (Exception e) {
            Log.e(_currentFileName,"Error while reading invasion data");
        }
    }

    /**
     * Associates the progress bar with the class
     */
    public void setProgressBar(LayerDrawable layer) {
        _progressBar = layer;
    }

    /**
     * Return progress bar
     *
     * @return      layer drawable
     */
    public LayerDrawable getProgressBar() {
        return _progressBar;
    }

    /**
     * Return the goal of the attacker
     *
     * @return      float
     */
    public float getGoal() { return _goal * 2F; }

    /**
     * Returns the attacker's progress
     *
     * @return      float
     */
    public float getCount() {
        return (_attackerFaction.compareTo("FC_INFESTATION") == 0 ? ((_goal - _count * -1) * 2) : (_count + _goal));
    }

    /**
     * Returns the progress percentage of the attacker
     *
     * @return      float
     */
    private float getPercentAttacker() {
        return MinMaxForValue.valueOf(((float)Math.round(((getCount() / getGoal())*100) * 100) / 100));
    }

    /**
     * Returns the percentage of progression of the defender
     *
     * @return      float
     */
    private float getPercentDefender() {
        return MinMaxForValue.valueOf(((float)Math.round((100.0 - getPercentAttacker()) * 100) / 100));
    }

    /**
     * Translated attacker item reward
     *
     * @return      string
     */
    private String getRewardAttacker() {

        String reward = ( _attackerReward != null ? _attackerReward.substring(_attackerReward.lastIndexOf('/')).replace("/","") : "");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(reward, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return reward;
        }
    }

    /**
     * Quantity of the item as a reward if the attacker wins
     *
     * @return      int
     */
    private int getAttackerRewardCount() { return _attackerRewardCount; }

    /**
     * Translated defender item reward
     *
     * @return      string
     */
    private String getRewardDefender() {
        String reward = ( _defenderReward != null ? _defenderReward.substring(_defenderReward.lastIndexOf('/')).replace("/","") : "");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(reward, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return reward;
        }
    }

    /**
     * Quantity of the item as a reward if the defender wins
     *
     * @return      int
     */
    private int getDefenderRewardCount() { return _defenderRewardCount; }

    /**
     * Translated name of the attacker
     *
     * @return      string
     */
    private String getAttackerFaction() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_attackerFaction, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return _attackerFaction;
        }
    }

    /**
     * Code name of the attacker
     *
     * @return      string
     */
    public String getAttackerFactionCode() {
        return _attackerFaction;
    }

    /**
     * Translated name of the defender
     *
     * @return      string
     */
    private String getDefenderFaction() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_defenderFaction, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return _defenderFaction;
        }
    }

    /**
     * Code name of the defender
     *
     * @return      string
     */
    public String getDefenderFactionCode() {
        return _defenderFaction;
    }

    /**
     * Translate node location
     *
     * @return      string
     */
    public String getLocation() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_location, "string", _context.getPackageName()));
        }catch (Exception ex) {
            return _location;
        }
    }

    /**
     * Invasion ID
     *
     * @return      string
     */
    public String getId() { return _id;}

    /**
     * Indicates if the invasion has been completed
     *
     * @return      boolean
     */
    public Boolean isCompleted() { return _completed; }

    /**
     * Translated attacker versus defender
     *
     * @return      String
     */
    public String getVersus()
    {
        return _context.getResources().getString(_context.getResources().getIdentifier("invasion_versus", "string", _context.getPackageName()), getAttackerFaction(), getDefenderFaction());
    }

    /**
     * Translated attacker reward and quantity
     *
     * @return      String
     */
    public String getAttackerReward()
    {
        if (getAttackerRewardCount() > 1)
            return _context.getResources().getString(_context.getResources().getIdentifier("invasion_rewards", "string", _context.getPackageName()), getAttackerRewardCount(), getRewardAttacker());
        else
            return _context.getResources().getString(_context.getResources().getIdentifier("invasion_reward", "string", _context.getPackageName()), getRewardAttacker());
    }

    /**
     * Translated defender reward and quantity
     *
     * @return      String
     */
    public String getDefenderReward()
    {
        if (getAttackerRewardCount() > 1)
            return _context.getResources().getString(_context.getResources().getIdentifier("invasion_rewards", "string", _context.getPackageName()), getDefenderRewardCount(), getRewardDefender());
        else
            return _context.getResources().getString(_context.getResources().getIdentifier("invasion_reward", "string", _context.getPackageName()), getRewardDefender());
    }

    /**
     * Attacker progress
     *
     * @return      String
     */
    public String getAttackerProgress()
    {
        return _context.getResources().getString(_context.getResources().getIdentifier("invasion_progress", "string", _context.getPackageName()), getPercentAttacker());
    }

    /**
     * Defender progress
     *
     * @return      String
     */
    public String getDefenderProgress()
    {
        return _context.getResources().getString(_context.getResources().getIdentifier("invasion_progress", "string", _context.getPackageName()), getPercentDefender());
    }
}