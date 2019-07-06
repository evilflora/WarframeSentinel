package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import com.evilflora.warframesentinel.Utils.NumberToTimeLeft;

import org.json.JSONObject;

public class PvpChallengeClass {
    private static String _currentFileName = "PvpChallengeClass";
    Context _context;
    private String _id;
    private String _challengeTypeRefID;
    //private long _dateActivation;
    private long _dateExpiration;
    private int _params;
    //private boolean _isGenerated;
    private String _pvpMode;
    // private ??? _subChallenges[];
    private String _category;

    public PvpChallengeClass(Context context, JSONObject pvpChallenge) {
        try {
            this._context           = context;
            this._id                = pvpChallenge.getJSONObject("_id").getString("$oid");
            this._challengeTypeRefID= pvpChallenge.getString("challengeTypeRefID");
            //this._dateActivation   = pvpChallenge.getJSONObject("startDate").getJSONObject("$date").getLong("$numberLong");
            this._dateExpiration   = pvpChallenge.getJSONObject("endDate").getJSONObject("$date").getLong("$numberLong");
            this._params            = pvpChallenge.getJSONArray("params").getJSONObject(0).getInt("v");
            //this._isGenerated       = pvpChallenge.getBoolean("isGenerated");
            this._pvpMode           = pvpChallenge.getString("PVPMode");
            // this._subChallenges     = pvpChallenge.getString("subChallenges");
            this._category          = pvpChallenge.getString("Category");
        } catch (Exception e) {
            Log.e(_currentFileName,"Connat load PVP Challenges - " + e.getMessage());
        }
    }

    /**
     * Translated time left before end
     *
     * @return      string
     */
    public String getTimeBeforeEnd() { return NumberToTimeLeft.convert((_dateExpiration - System.currentTimeMillis()),true); }

    /**
     * Challenge ID
     *
     * @return      string
     */
    public String getId() { return _id;}

    /**
     * Category code (daily, weekly, weekly hard)
     *
     * @return      string
     */
    public String getCategoryCode() {
        return _category;
    }


    /**
     * Translated category
     *
     * @return      string
     */
    public String getCategory() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(getCategoryCode(), "string", _context.getPackageName()));
        } catch (Exception ex) {
            return getCategoryCode();
        }
    }

    /**
     * Translated description
     *
     * @return      String
     */
    public String getChallenge() {
        String challenge = (_challengeTypeRefID != null ? _challengeTypeRefID.substring(_challengeTypeRefID.lastIndexOf('/')).replace("/", "") : "");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(challenge, "string", _context.getPackageName()), _params);
        } catch (Exception ex) {
            return challenge;
        }
    }

    /**
     * Translated mode
     *
     * @return      String
     */
    public String getMode() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_pvpMode, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _pvpMode;
        }
    }

    /**
     * Return true if challenge is over
     *
     * @return      boolean
     */
    public boolean isEnd() { return (_dateExpiration - System.currentTimeMillis()) <= 0; }
}