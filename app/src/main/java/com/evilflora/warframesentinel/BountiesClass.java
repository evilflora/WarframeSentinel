package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

class BountiesClass {
    private final String CurrentFileName = "BountiesClass"; // filename
    private final int NightTime = 50 * 60 * 1000; // in ms 50 minutes
    private final String sDayNight[] = {"Day","Night","Indeterminate"};
    private String _day_or_night = "";
    //private String _id;
    //private long _date_activation;
    private long _date_expiration;
    //private String _tag;
    private List<BountyJobClass> _cetus_jobs;

    BountiesClass(JSONArray cetus) { // constructor
        _cetus_jobs = new ArrayList<>();
        try {
            //this._id                  = cetus.getJSONObject("_id").getString("$oid");
            //this._date_activation     = cetus.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration       = cetus.getJSONObject(0).getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            //this._tag                 = cetus.getString("Tag");
            try {
                for (int i = 0; i < (cetus.getJSONObject(0).getJSONArray("Jobs").length()); i++) {
                    this._cetus_jobs.add(new BountyJobClass(cetus.getJSONObject(0).getJSONArray("Jobs").getJSONObject(i)));
                }
            } catch(Exception ex) {
                Log.e(CurrentFileName,"Error while reading cetus jobs - " + ex.getMessage());
            }
        } catch (Exception ex) {
            Log.e(CurrentFileName,"Error while reading cetus bounties -" + ex.getMessage());
        }
        cetus_day_or_night(); // todo : allow us to know if it's day or night
    }

    /**
     * Return time left before reset of bounty
     *
     * @return      string
     */
    public String get_time_before_expiry() { return TimestampToDate.convert(get_time_left(),true); }

    /**
     * Return time left before reset of bounty
     *
     * @return      long
     */
    long get_time_left() { return _date_expiration - System.currentTimeMillis(); }

    /**
     * Return time left before the end of the next cycle
     *
     * @return      string
     */
    String cetus_day_night_time() {
        String timer = "";
        if (_day_or_night.compareTo(sDayNight[0]) == 0) {
            timer = TimestampToDate.convert(_date_expiration - System.currentTimeMillis() - (NightTime),true);
        } else if (_day_or_night.compareTo(sDayNight[1]) == 0) {
            timer = TimestampToDate.convert(_date_expiration - System.currentTimeMillis(),true);
        }
        return timer;
    }

    /**
     * Return HH:mm:ss of the end of the cycle
     *
     * @return      string
     */
    String cetus_next_cycle_date() {
        String date = "";
        if (_day_or_night.compareTo(sDayNight[0]) == 0) {
            date = (android.text.format.DateFormat.format("HH:mm:ss", System.currentTimeMillis() + (_date_expiration - System.currentTimeMillis()) - (NightTime)).toString());
        } else if (_day_or_night.compareTo(sDayNight[1]) == 0) {
            date = (android.text.format.DateFormat.format("HH:mm:ss", System.currentTimeMillis() + (_date_expiration - System.currentTimeMillis())).toString());
        }
        return date;
    }

    /**
     * Returns true if we are still in transition between the last cycle and the new cycle
     *
     * @return      boolean
     */
    boolean get_status() {
        return (_day_or_night.compareTo(sDayNight[2]) == 0);
    }

    /**
     * Returns the state the active cycle
     *
     * @return      string
     */
    String cetus_day_or_night() {
        if ((_date_expiration - System.currentTimeMillis() <= NightTime) && (_date_expiration - System.currentTimeMillis() >= 0)) {
            _day_or_night = sDayNight[1];
        } else {
            _day_or_night = sDayNight[0];
        }
        return _day_or_night;
    }

    /**
     * Return the next cycle
     *
     * @return      string
     */
    String cetus_next_world_cycle_state() {
        if (_day_or_night.compareTo(sDayNight[0]) == 0) return sDayNight[1];
        else  return sDayNight[0];
    }

    /**
     * Return the steps of the bounty
     *
     * @return      List<BountyJobClass>
     */
    List<BountyJobClass> get_cetus_jobs() { return _cetus_jobs; }

}