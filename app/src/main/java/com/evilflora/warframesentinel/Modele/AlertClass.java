package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import com.evilflora.warframesentinel.Utils.NumberToTimeLeft;

import org.json.JSONObject;

public class AlertClass {
    private static String _currentFileName = "AlertClass";
    private Context _context;
    private String _id;
    private long _dateActivation;
    private long _dateExpiration;
    private String _missionType;
    private String _faction;
    private String _location;
    private int _enemyLevelMin;
    private int _enemyLevelMax;
    private int _rewardCredits;
    private String _rewardItemName;
    private int _rewardItemQuantity;

    /**
     * The Legion and his challenges
     *
     * @param context           Activity context
     * @param alert             The JSONObject containing data
     */
    public AlertClass(Context context, JSONObject alert) {
        this._context = context;
        try {
            this._id                = alert.getJSONObject("_id").getString("$oid");
            this._dateActivation   = alert.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._dateExpiration   = alert.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._missionType       = alert.getJSONObject("MissionInfo").getString("missionType");
            this._faction           = alert.getJSONObject("MissionInfo").getString("faction");
            this._location          = alert.getJSONObject("MissionInfo").getString("location");
            this._enemyLevelMin  = alert.getJSONObject("MissionInfo").getInt("minEnemyLevel");
            this._enemyLevelMax  = alert.getJSONObject("MissionInfo").getInt("maxEnemyLevel");
            this._rewardCredits    = alert.getJSONObject("MissionInfo").getJSONObject("missionReward").getInt("credits");
            // todo remove try catch, why reward_item_name is two time ?
            try {
                this._rewardItemName = alert.getJSONObject("MissionInfo").getJSONObject("missionReward").getJSONArray("items").getString(0);
            } catch (Exception ex) {
            }
            try {
                this._rewardItemName = alert.getJSONObject("MissionInfo").getJSONObject("missionReward").getJSONArray("countedItems").getJSONObject(0).getString("ItemType");
                this._rewardItemQuantity = alert.getJSONObject("MissionInfo").getJSONObject("missionReward").getJSONArray("countedItems").getJSONObject(0).getInt("ItemCount");
            } catch (Exception ex) {
            }
        } catch (Exception e) {
            Log.e(_currentFileName,"Alert Data Error");
        }
    }

    /**
     * Translated time before end of alert
     *
     * @return      string
     */
    public String getTimeBeforeExpiry() {
        if (System.currentTimeMillis() < _dateActivation) {
            return _context.getResources().getString(_context.getResources().getIdentifier("start_in", "string", _context.getPackageName()), NumberToTimeLeft.convert(_dateActivation - System.currentTimeMillis(),true));
        } else {
            return NumberToTimeLeft.convert(getTimeLeft(),true);
        }
    }

    /**
     * Translated mission type
     *
     * @return      string
     */
    private String getMissionType() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_missionType, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _missionType;
        }
    }

    /**
     * Translated mission faction
     *
     * @return      string
     */
    private String getFaction() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_faction, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _faction;
        }
    }

    /**
     * Translated mission location
     *
     * @return      string
     */
    public String getLocation() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_location, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _location;
        }
    }

    /**
     * Return mission type and faction
     *
     * @return      string
     */
    public String getType() {
        return _context.getResources().getString(_context.getResources().getIdentifier("mission_type", "string", _context.getPackageName()), getMissionType(), getFaction());
    }

    /**
     * Translated enemies level range
     *
     * @return      string
     */
    public String getEnemyLevel() {
        return _context.getResources().getString(_context.getResources().getIdentifier("enemy_level", "string", _context.getPackageName()), _enemyLevelMin, _enemyLevelMax);
    }

    /**
     * True if it's end of alert
     *
     * @return      boolean
     */
    public boolean isEnd() { return getTimeLeft() <= 0; }

    /**
     * Time left before end of alert
     *
     * @return      long
     */
    public long getTimeLeft() { return _dateExpiration - System.currentTimeMillis();}

    /**
     * Alert ID
     *
     * @return      string
     */
    public String getId() { return _id; }

    /**
     * Translated reward name
     *
     * @return      string
     */
    private String getRewardItemName() {
        if (_rewardItemName != null) {
            try {
                return _context.getResources().getString(_context.getResources().getIdentifier(_rewardItemName, "string", _context.getPackageName()));
            } catch (Exception ex) {
                return _rewardItemName;
            }
        } else return null;
    }

    /**
     * Reward quantity
     *
     * @return      string
     */
    private int getRewardItemQuantity() { return _rewardItemQuantity; }

    /**
     * Credits reward
     *
     * @return      string
     */
    public String getRewardCredits() {
        if (_rewardCredits > 0) {
            return _context.getResources().getString(_context.getResources().getIdentifier("credits", "string", _context.getPackageName()), _rewardCredits);
        } else {
            return null;
        }
    }

    /**
     * Translated quantity and reward name
     *
     * @return      string
     */
    public String getRewards() {
        if (getRewardItemQuantity() > 1)
            return _context.getResources().getString(_context.getResources().getIdentifier("alert_rewards", "string", _context.getPackageName()), getRewardItemQuantity(), getRewardItemName());
        else
            return _context.getResources().getString(_context.getResources().getIdentifier("alert_reward", "string", _context.getPackageName()), getRewardItemName());
    }
}