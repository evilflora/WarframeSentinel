package com.evilflora.warframesentinel;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class SortieClass {
    private Context _context;
    private String _id;
    private long _date_activation;
    private long _date_expiration;
    private String _boss;
    private List<SortieStepClass> _sorties = new ArrayList<>();
    private String[] _rewardsItems = {"AYATAN ANASA SCULPTURE", "RIVEN MOD", "6000 KUVA", "4000 ENDO", "3 DAY BOOSTER", "EXILUS ADAPTER", "FORMA", "OROKIN CATALYST BLEPRINT", "OROKIN REACTOR BLUEPRINT", "LEGENDARY CORE"};
    private double[] _rewardsDropChance = {28.00, 25.9, 14.00, 12.10, 9.81, 2.50, 2.50, 2.50, 2.50, 0.18 };

    SortieClass(Context context, JSONObject sortie) { // constructor
        try {
            this._context            = context;
            this._id                = sortie.getJSONObject("_id").getString("$oid");
            this._date_activation   = sortie.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration   = sortie.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._boss              = sortie.getString("Boss");
            try {
                for (int i = 0; i < (sortie.getJSONArray("Variants").length()); i++) {
                    String[] _level = {"50 - 60", "65 - 80", "80 - 100"};
                    int[] _credits = {20000, 30000, 50000};
                    this._sorties.add(new SortieStepClass(_context,sortie.getJSONArray("Variants").getJSONObject(i), _credits[i], _level[i]));
                }
            } catch(Exception ex) {
                Log.e("SortieClass", ex.toString());
            }

        } catch (Exception e) {
            Log.e("SortieClass","Error while reading sortie");
        }
    }

    int get_sortie_lenght() {
        return _sorties.size();
    }

    public String get_time_before_expiry() {
        return "Time before reset " + TimestampToDate.convert(_date_expiration - System.currentTimeMillis(),true);
    }

    /*public boolean is_exprired() {
        return (((_date_expiration - System.currentTimeMillis()) - 3600 * 1000) <= 0);
    }*/

    String get_sortie_type() {
        String sortie_type;
        try {
            sortie_type = _context.getResources().getString(_context.getResources().getIdentifier(_boss, "string", _context.getPackageName()));
        } catch (Exception ex) {
            sortie_type = _boss;
        }
        return sortie_type;
    }

    public int get_time_left() {
        return (int)(_date_expiration - _date_activation);
    }

    SortieStepClass get_step(int i) {
        return _sorties.get(i);
    }

    String[] get_rewards() {
        return _rewardsItems;
    }

    double[] get_drop_chance() {
        return _rewardsDropChance;
    }

}