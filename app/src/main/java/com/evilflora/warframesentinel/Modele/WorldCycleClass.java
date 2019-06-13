package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

public class WorldCycleClass {
    private static String _currentFileName = "WorldCycleClass";
    private Context _context;
    private String[] _cycles;
    private String _currentCycle = "";
    //private String _id;
    //private long _dateActivation;
    private long _dateExpiration;
    //private String _tag;

    private int _cycleTime; // in ms

    /**
     * Return time left before reset of bounty
     *
     * @param context           Activity context
     * @param cetus             The JSONArray containing data
     * @param cycleTime         Duration in minutes of the shortest cycle (last cycle, 2 cycle per open world)
     */
    public WorldCycleClass(Context context, JSONArray cetus, int cycleTime, String cycles) {
        this._context               = context;
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
        this._currentCycle = getCurrentCycle();
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
}