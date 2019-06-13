package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LegionClass {
    private static String _currentFileName = "LegionClass";
    private Context _context;
    //private long _date_activation;
    private long _date_expiration;
    private String _tag;
    private int _season;
    private int _phase;
    //private String _param;
    private List<LegionChallengeClass> _challenges = new ArrayList<>();

    /**
     * The Legion and his challenges
     *
     * @param context           Activity context
     * @param challenge          The JSONObject containing data
     */
    public LegionClass(Context context, JSONObject challenge) {
        try {
            this._context           = context;
            //this._date_activation   = challenge.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = challenge.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._tag               = challenge.getString("AffiliationTag");
            this._season            = challenge.getInt("Season");
            this._phase             = challenge.getInt("Phase");
            //this._param             = challenge.getString("Params");
            for (int i = 0; i < (challenge.getJSONArray("ActiveChallenges").length()); i++) {
                this._challenges.add(new LegionChallengeClass(_context,challenge.getJSONArray("ActiveChallenges").getJSONObject(i)));
            }
        } catch (Exception e) {
            Log.e(_currentFileName,"Cannot load legion - " + e.getMessage());
        }
    }

    /**
     * Translated time before end of challenge
     *
     * @return      string
     */
    public String getTimeBeforeEnd() { return TimestampToTimeleft.convert(getTimeLeft(),true); }

    /**
     * The translated description of the challenge
     *
     * @return      string
     */
    public String getTag() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_tag, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _tag;
        }
    }

    /**
     * Time left before reset of the challenge
     *
     * @return      long
     */
    private long getTimeLeft() { return _date_expiration - System.currentTimeMillis(); }

    /**
     * True if challenge is closed
     *
     * @return      boolean
     */
    public boolean isEnd() { return (getTimeLeft() <= 0); }

    /**
     * Current season
     *
     * @return      int
     */
    public int getSeason() { return _season; }

    /**
     * Current phase
     *
     * @return      int
     */
    public int getPhase() { return _phase; }

    /**
     * Get the challenge
     *
     * @return      LegionChallengeClass
     */
    public LegionChallengeClass getChallenge(int i) { return _challenges.get(i); }

    /**
     * Get the challenge quantity
     *
     * @return      int
     */
    public int getChallengeLength() { return _challenges.size(); }

}