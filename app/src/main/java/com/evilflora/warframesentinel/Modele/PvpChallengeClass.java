package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class PvpChallengeClass {
    private final String CurrentFileName = "PvpChallengeClass"; // le nom du fichier
    Context _context;
    private String _id;
    private String _challengeTypeRefID;
    //private long _date_activation;
    private long _date_expiration;
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
            //this._date_activation   = pvpChallenge.getJSONObject("startDate").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = pvpChallenge.getJSONObject("endDate").getJSONObject("$date").getLong("$numberLong");
            this._params            = pvpChallenge.getJSONArray("params").getJSONObject(0).getInt("v");
            //this._isGenerated       = pvpChallenge.getBoolean("isGenerated");
            this._pvpMode           = pvpChallenge.getString("PVPMode");
            // this._subChallenges     = pvpChallenge.getString("subChallenges");
            this._category          = pvpChallenge.getString("Category");
        } catch (Exception e) {
            Log.e(CurrentFileName,"Connat load PVP Challenges - " + e.getMessage());
        }
    }

    /**
     * Time time left before expiry
     *
     * @return      string
     */
    public String get_time_before_expiry() { return TimestampToTimeleft.convert((_date_expiration - System.currentTimeMillis()),true); }

    /**
     * PVP Challenge ID
     *
     * @return      string
     */
    public String get_id() { return _id;}

    /**
     * If it's daily or weekly or neither
     *
     * @return      string
     */
    public String get_category_code() {
        return _category;
    }


    /**
     * If it's daily or weekly or neither
     *
     * @return      string
     */
    public String get_category() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_category, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _category;
        }
    }

    /**
     * The challenge code or the challenge string
     *
     * @return      String
     */
    public String get_challenge() {
        String challenge = (_challengeTypeRefID != null ? _challengeTypeRefID.substring(_challengeTypeRefID.lastIndexOf('/')).replace("/", "") : "");
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(challenge, "string", _context.getPackageName()), _params);
        } catch (Exception ex) {
            return challenge;
        }
    }

    /**
     * The challenge code or the challenge string
     *
     * @return      String
     */
    public String get_mode() {
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(_pvpMode, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return _pvpMode;
        }
    }

    /**
     * Retourne true if pvp_challenge is over
     *
     * @return      boolean
     */
    public boolean is_end_of_pvp_challenge() { return (_date_expiration - System.currentTimeMillis()) <= 0; }
}