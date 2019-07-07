package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import com.evilflora.warframesentinel.Utils.NumberToTimeLeft;

import org.json.JSONObject;

public class LegionChallengeClass {
    private static String _currentFileName = "LegionChallengeClass";
    private Context _context;
    private String _id;
    //private long _dateActivation;
    private long _dateExpiration;
    private String _challenge;
    private int[] _standingReward = {1000, 4500, 7000}; // reversed see below
    private String[] _difficulty = {"Daily", "Weekly", "WeeklyHard"}; // reversed, because 'SeasonWeekly' will be always true with 'startWith' in 'SeasonWeeklyHard'

    /**
     * The Legion and his challenges
     *
     * @param context           Activity context
     * @param challenge          The JSONObject containing data
     */
    public LegionChallengeClass(Context context, JSONObject challenge) {
        try {
            this._context           = context;
            this._id                = challenge.getJSONObject("_id").getString("$oid");
            //this._dateActivation   = challenge.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._dateExpiration   = challenge.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._challenge         = challenge.getString("Challenge");
        } catch (Exception e) {
            Log.e(_currentFileName,"Cannot load challenge - " + e.getMessage());
        }
    }

    /**
     * Translated time before end of challenge
     *
     * @return      string
     */
    public String getTimeBeforeEnd() { return NumberToTimeLeft.convert(getTimeLeft(),true); }

    /**
     * The translated description of the challenge
     *
     * @return      string
     */
    public String getName() {
        String name = _challenge.substring(_challenge.lastIndexOf('/')).replace("/","");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(name, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return name;
        }
    }

    /**
     * The challenge code
     *
     * @return      string
     */
    private String getNameCode() {
        String word = "Seasons/";
        String value = _challenge.substring(_challenge.indexOf(word) + word.length());
        return value.substring(0, value.indexOf('/'));
    }

    /**
     * Time left before reset of the challenge
     *
     * @return      long
     */
    private long getTimeLeft() { return _dateExpiration - System.currentTimeMillis(); }

    /**
     * True if challenge is closed
     *
     * @return      boolean
     */
    public boolean isEnd() { return (getTimeLeft() <= 0); }

    /**
     * Challenge ID
     *
     * @return      string
     */
    public String getId() { return _id; }

    /**
     * Standing reward
     *
     * @return      String
     */
    public String getStandingReward() {
        return String.valueOf(_standingReward[getIndex()]);
    }

    /**
     * Translated type of challenge
     *
     * @return      string
     */
    public String getType() {
        return _context.getResources().getString(_context.getResources().getIdentifier(getTypeCode(), "string", _context.getPackageName()));
    }

    /**
     * Type of challenge
     *
     * @return      string
     */
    public String getTypeCode() {
        return String.valueOf(_difficulty[getIndex()]);
    }

    /**
     * Index
     *
     * @return      int
     */
    private int getIndex() {
        for(int i = 0; i < _difficulty.length; i++)
        {
            if(getNameCode().equals(_difficulty[i]))
            {
                return i;
            }
        }
        return 0;
    }
}