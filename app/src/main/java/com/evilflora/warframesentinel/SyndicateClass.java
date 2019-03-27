package com.evilflora.warframesentinel;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SyndicateClass {
    private Context _context;
    private String _id;
    private long _date_activation;
    private long _date_expiration;
    private String _tag;
    //private int _seed;
    private List<String> _nodes;
    private List<String> _levels = Arrays.asList("7 - 9","? - ?","? - ?","? - ?","20 - 25","25 - 30","30 - 35");

    SyndicateClass(Context context, JSONObject object) { // constructor
        try {
            this._context           = context;
            this._id                = object.getJSONObject("_id").getString("$oid");
            this._date_activation   = object.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = object.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._tag               = object.getString("Tag");
            //this._seed              = object.getInt("Seed");
            this._nodes             = new ArrayList<>();
            try {
                for (int i = 0; i < object.getJSONArray("Nodes").length(); i++) {
                    this._nodes.add(object.getJSONArray("Nodes").getString(i));
                }
            } catch(Exception ex) {
                Log.e("SyndicateClass","Error while syndicate nodes " + ex.getMessage());
            }
        } catch (Exception e) {
            Log.e("SyndicateClass","Error while reading Syndicates" + e.getMessage());
        }
    }

    public String get_time_before_expiry() {
        return ( System.currentTimeMillis() < _date_activation ? "Start in " + TimestampToDate.convert(_date_activation - System.currentTimeMillis(),true): TimestampToDate.convert((_date_expiration - System.currentTimeMillis()),true));
    }

    String get_tag() {
        return _tag;
    }

    String get_type() {
        String type;
        try {
            type = _context.getResources().getString(_context.getResources().getIdentifier(_tag, "string", _context.getPackageName()));
        } catch (Exception ex) {
            type = _tag;
        }
        return type;
    }

    String get_node(int i) {
        String location;
        try {
            location = _context.getResources().getString(_context.getResources().getIdentifier(_nodes.get(i), "string", _context.getPackageName()));
        } catch (Exception ex) {
            location = _nodes.get(i);
        }
        return location;
    }

    int get_nodes_size() { return this._nodes.size(); }

    boolean end_of_syndicate() { return (_date_expiration - System.currentTimeMillis()) <= 0; }

    String get_id() {
        return _id;
    }

    String get_level(int i) { return "Level: " + _levels.get(i); }

}