package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class BountiesClass {
    private static String _currentFileName = "BountiesClass";
    private Context _context;
    private String[] _cycles;
    private String _currentCycle;
    //private String _id;
    //private long _dateActivation;
    private long _dateExpiration;
    //private String _tag;
    private List<BountyJobClass> _bountyJobs;

    private int _cycleTime; // in ms

    /**
     * Bounty
     *
     * @param context           Activity context
     * @param cetus             The JSONArray containing data
     * @param cycleTime         Duration in minutes of the shortest cycle (last cycle, 2 cycle per open world)
     */
    public BountiesClass(Context context, JSONArray cetus, int cycleTime, String cycles) {
        this._context               = context;
        this._bountyJobs = new ArrayList<>();
        this._cycleTime = cycleTime * 60000;
        this._cycles = _context.getResources().getStringArray(_context.getResources().getIdentifier(cycles, "array", _context.getPackageName()));
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
     * Translated time left before reset of the bounty
     *
     * @return      string
     */
    public String getTimeBeforeReset() {
        return _context.getResources().getString(_context.getResources().getIdentifier("time_before_reset", "string", _context.getPackageName()), TimestampToTimeleft.convert(getTimeLeft(),true));
    }

    /**
     * Time left before reset of the bounty
     *
     * @return      long
     */
    public long getTimeLeft() { return _dateExpiration - System.currentTimeMillis(); }

    /**
     * Return time left before the end of the next cycle
     *
     * @return      string
     */
    private String getCycleTimeLeft() {
        String timer;
        if (_currentCycle.compareTo(_cycles[0]) == 0) {
            timer = TimestampToTimeleft.convert(getTimeLeft() - _cycleTime,true);
        } else {
            timer = TimestampToTimeleft.convert(getTimeLeft(),true);
        }
        return timer;
    }

    /**
     * Return HH:mm:ss of the end of the cycle
     *
     * @return      string
     */
    private String getCycleEndDate() {
        String date;
        if (_currentCycle.compareTo(_cycles[0]) == 0) {
            date = (android.text.format.DateFormat.format("HH:mm:ss", _dateExpiration - _cycleTime).toString());
        } else {
            date = (android.text.format.DateFormat.format("HH:mm:ss", _dateExpiration).toString());
        }
        return date;
    }
    /**
     * Translated time of the end of the cycle and the reset time of the cycle
     *
     * @param       name the name of the currrent bounty set
     *
     * @return      string
     */
    public String getWorldStatusCycleStatus(String name) {
        return _context.getResources().getString(_context.getResources().getIdentifier("world_cycle_timer", "string", _context.getPackageName()), name, getNextCycle(), getCycleTimeLeft(), getCycleEndDate());
    }

    /**
     * The state the active cycle
     *
     * @return      string
     */
    private String getCurrentCycle() {
        if ((getTimeLeft() <= _cycleTime) && (getTimeLeft() >= 0)) {
            _currentCycle = _cycles[1];
        } else {
            _currentCycle = _cycles[0];
        }
        return _currentCycle;
    }

    /**
     * The next cycle
     *
     * @return      string
     */
    private String getNextCycle() {
        if (_currentCycle.compareTo(_cycles[0]) == 0) return _cycles[1];
        else return _cycles[0];
    }

    /**
     * Steps of the bounty
     *
     * @return      List<BountyJobClass>
     */
    public List<BountyJobClass> getBountyJobs() { return _bountyJobs; }


    /**
     * True if fissure is closed
     *
     * @return      boolean
     */
    public boolean isEndOfBounty() { return getTimeLeft() <= 0; }

}