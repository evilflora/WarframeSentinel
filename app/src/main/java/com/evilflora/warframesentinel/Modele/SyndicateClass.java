package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import com.evilflora.warframesentinel.Utils.NumberToTimeLeft;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyndicateClass {
    private Context _context;
    private String _id;
    private long _dateActivation;
    private long _dateExpiration;
    private String _tag;
    //private int _seed;
    private List<String> _nodes;
    private List<String> _levels = Arrays.asList("? - ?","? - ?","? - ?","? - ?","20 - 25","25 - 30","30 - 35");
    //private List<String> _syndicate_rank; // todo mettre les rangs pour tous les syndicats (1 à 5, 6 fois)

    /**
     * Return time left before reset of bounty
     *
     * @param context           Activity context
     * @param syndicate         The JSONArray containing data
     */
    public SyndicateClass(Context context, JSONObject syndicate) {
        try {
            this._context           = context;
            this._id                = syndicate.getJSONObject("_id").getString("$oid");
            this._dateActivation   = syndicate.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._dateExpiration   = syndicate.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._tag               = syndicate.getString("Tag");
            //this._seed              = object.getInt("Seed");
            this._nodes             = new ArrayList<>();
            for (int i = 0; i < syndicate.getJSONArray("Nodes").length(); i++) {
                this._nodes.add(syndicate.getJSONArray("Nodes").getString(i));
            }
        } catch (Exception e) {
            Log.e("SyndicateClass","Error while reading Syndicates" + e.getMessage());
        }
    }

    /**
     * Translated time left before reset
     *
     * @return      string
     */
    public String getTimeBeforeEnd() {
        if (System.currentTimeMillis() < _dateActivation) {
            return _context.getResources().getString(_context.getResources().getIdentifier("start_in", "string", _context.getPackageName()), NumberToTimeLeft.convert(_dateActivation - System.currentTimeMillis(), true));
        } else {
            return NumberToTimeLeft.convert(getTimeLeft(),true);
        }
    }

    /**
     * Syndicate owner of missions
     *
     * @return      long
     */
    public String getTag() {
        return _tag;
    }

    /**
     * Translated syndicate name
     *
     * @return      long
     */
    public String getType() {
        String type;
        try {
            type = _context.getResources().getString(_context.getResources().getIdentifier(_tag, "string", _context.getPackageName()));
        } catch (Exception ex) {
            type = _tag;
        }
        return type;
    }

    /**
     * Translated node name
     *
     * @return      long
     */
    public String getNode(int i) {
        String location;
        try {
            location = _context.getResources().getString(_context.getResources().getIdentifier(_nodes.get(i), "string", _context.getPackageName()));
        } catch (Exception ex) {
            location = _nodes.get(i);
        }
        return location;
    }

    /**
     * Time left before reset of the mission
     *
     * @return      long
     */
    private long getTimeLeft() { return _dateExpiration - System.currentTimeMillis(); }

    /**
     * Where missions are located
     *
     * @return      int
     */
    public int getNodeSize() { return this._nodes.size(); }

    /**
     * True if the mission has ended
     *
     * @return      int
     */
    public boolean isEndOfMission() { return (_dateExpiration - System.currentTimeMillis()) <= 0; }

    /**
     * Id of the mission
     *
     * @return      int
     */
    public String getId() {
        return _id;
    }

    /**
     * Level of the current mission
     *
     * @return      int
     */
    public String getLevel(int i) {
        return _context.getResources().getString(_context.getResources().getIdentifier("syndicate_level", "string", _context.getPackageName()), _levels.get(i));
    }

}