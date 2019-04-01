package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONObject;

class BountyJobClass {
    private String _job_type;
    //private String _rewards;
    private String _min_ennemy_level;
    private String _max_ennemy_level;
    private int[] _xp_amounts;

    BountyJobClass(JSONObject cetusJob) { // constructor
        try {
            this._job_type          = cetusJob.getString("jobType");
            //this._rewards           = cetusJob.getString("rewards");
            this._min_ennemy_level  = cetusJob.getString("minEnemyLevel");
            this._max_ennemy_level  = cetusJob.getString("maxEnemyLevel");
            this._xp_amounts = new int[cetusJob.getJSONArray("xpAmounts").length()];
            try {
                for (int i = 0; i < _xp_amounts.length; i++) {
                    this._xp_amounts[i] = cetusJob.getJSONArray("xpAmounts").getInt(i);
                }
            }catch (Exception ex) {
                Log.e("CetusJobClass","Error while reading job xp amount - " + ex.getMessage());
            }
        } catch (Exception ex) {
            Log.e("CetusJobClass","Error while reading cetus job - " + ex.getMessage());
        }
    }
    String get_job_type() { return (_job_type != null ? _job_type.substring(_job_type.lastIndexOf("/")).replace("/","") : null ); }

    /*public String get_rewards() {
        String rewards;
        try {
            rewards = _context.getResources().getString(_context.getResources().getIdentifier(_rewards, "string", _context.getPackageName()));
        } catch (Exception ex) {
            rewards = _rewards;
        }
        return rewards;
    }*/

    String get_ennemy_level() {
        return _min_ennemy_level + " - " + _max_ennemy_level;
    }

    int get_xp_amounts_size() {
        return _xp_amounts.length;
    }

    int get_xp_amounts(int i) {
        return _xp_amounts[i];
    }

    int get_total_xp_amounts() {
        int sum = 0;
        for (int xp:_xp_amounts)
            sum += xp;
        return sum;
    }
}