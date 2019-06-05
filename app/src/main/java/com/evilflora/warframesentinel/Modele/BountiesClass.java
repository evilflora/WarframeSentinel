package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class BountiesClass {
    private static String _currentFileName = "BountiesClass";
    private Context _context;
    private int _cycleTime = 50 * 60 * 1000; // in ms 50 minutes
    private List<String> _cycles;
    private String _currentCycle = "";
    //private String _id;
    //private long _dateActivation;
    private long _dateExpiration;
    //private String _tag;
    private List<BountyJobClass> _bountyJobs;

    public BountiesClass(Context context, JSONArray cetus, List<String> cycles) { // constructor
        this._context               = context;
        _bountyJobs = new ArrayList<>();
        _cycles = cycles;
        try {
            //this._id                = cetus.getJSONObject(0).getJSONObject("_id").getString("$oid");
            //this._dateActivation    = cetus.getJSONObject(0).getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._dateExpiration    = cetus.getJSONObject(0).getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            //this._tag               = cetus.getJSONObject(0).getString("Tag");
        } catch (Exception ex) {
            Log.e(_currentFileName,"Error while reading bounty - " + ex.getMessage());
        }

        try {
            for (int i = 0; i < (cetus.getJSONObject(0).getJSONArray("Jobs").length()); i++) {
                this._bountyJobs.add(new BountyJobClass(_context,cetus.getJSONObject(0).getJSONArray("Jobs").getJSONObject(i)));
            }
        } catch(Exception ex) {
            Log.e(_currentFileName,"Error while reading bounty jobs - " + ex.getMessage());
        }
        _currentCycle = getCurrentCycle();
    }

    /**
     * Return time left before reset of bounty
     *
     * @return      string
     */
    public String getTimeBeforeExpiry() { return TimestampToTimeleft.convert(getTimeLeft(),true); }

    /**
     * Return time left before reset of bounty
     *
     * @return      long
     */
    public long getTimeLeft() { return _dateExpiration - System.currentTimeMillis(); }

    /**
     * Return time left before the end of the next cycle
     *
     * @return      string
     */
    public String getCycleTimeLeft() {
        String timer = "";
        if (_currentCycle.compareTo(_cycles.get(0)) == 0) {
            timer = TimestampToTimeleft.convert(_dateExpiration - System.currentTimeMillis() - (_cycleTime),true);
        } else if (_currentCycle.compareTo(_cycles.get(1)) == 0) {
            timer = TimestampToTimeleft.convert(_dateExpiration - System.currentTimeMillis(),true);
        }
        return timer;
    }

    /**
     * Return HH:mm:ss of the end of the cycle
     *
     * @return      string
     */
    public String getCycleEndDate() {
        String date = "";
        if (_currentCycle.compareTo(_cycles.get(0)) == 0) {
            date = (android.text.format.DateFormat.format("HH:mm:ss", System.currentTimeMillis() + (_dateExpiration - System.currentTimeMillis()) - (_cycleTime)).toString());
        } else if (_currentCycle.compareTo(_cycles.get(1)) == 0) {
            date = (android.text.format.DateFormat.format("HH:mm:ss", System.currentTimeMillis() + (_dateExpiration - System.currentTimeMillis())).toString());
        }
        return date;
    }

    /**
     * Returns true if we are still in transition between the last cycle and the new cycle
     *
     * @return      boolean
     */
    public boolean getCycleStatus() {
        return (_currentCycle.compareTo(_cycles.get(2)) == 0);
    }

    /**
     * Returns the state the active cycle
     *
     * @return      string
     */
    public String getCurrentCycle() {
        if ((_dateExpiration - System.currentTimeMillis() <= _cycleTime) && (_dateExpiration - System.currentTimeMillis() >= 0)) {
            _currentCycle = _cycles.get(1);
        } else {
            _currentCycle = _cycles.get(0);
        }
        return _currentCycle;
    }

    /**
     * Return the next cycle
     *
     * @return      string
     */
    public String getNextCycle() {
        if (_currentCycle.compareTo(_cycles.get(0)) == 0) return _cycles.get(1);
        else return _cycles.get(0);
    }

    /**
     * Return the steps of the bounty
     *
     * @return      List<BountyJobClass>
     */
    public List<BountyJobClass> getBountyJobs() { return _bountyJobs; }

}