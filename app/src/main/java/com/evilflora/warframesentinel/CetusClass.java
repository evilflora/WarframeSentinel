package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class CetusClass {
    private final int NightTime = 50 * 60 * 1000; // en ms 50 minutes
    private final String sDayNight[] = {"Day","Night","Indeterminate"};
    private String _day_or_night = "";
    //private String _id;
    //private long _date_activation;
    private long _date_expiration;
    //private String _tag;
    private List<CetusJobClass> _cetus_jobs;

    CetusClass(JSONObject cetus) { // constructor
        _cetus_jobs = new ArrayList<>();
        try {
            //this._id                  = cetus.getJSONObject("_id").getString("$oid");
            //this._date_activation     = cetus.getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._date_expiration       = cetus.getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            //this._tag                 = cetus.getString("Tag");
            try {
                for (int i = 0; i < (cetus.getJSONArray("Jobs").length()); i++) {
                    this._cetus_jobs.add(new CetusJobClass(cetus.getJSONArray("Jobs").getJSONObject(i)));
                }
            } catch(Exception ex) {
                Log.e("CetusClass","Error while reading cetus jobs" + ex.getMessage());
            }
        } catch (Exception ex) {
            Log.e("CetusClass","Error while reading cetus bounties - " + ex.getMessage());
        }
    }

    /**
     * Retourne un string avec le temps restant avant le reset des bounties
     *
     * @return      le temps restant avant le reset des bounties
     */
    public String get_time_before_expiry() {
        return TimestampToDate.convert(_date_expiration - System.currentTimeMillis(),true);
    }

    /**
     * Retourne le temps restant avantle reset desounties
     *
     * @return      le temps restant avant le reset des bounties
     */
    long get_time_left() {
        return _date_expiration - System.currentTimeMillis();
    }

    /**
     * Retourne un string avec le temps restant pour la nuit comme pour le jour
     *
     * @return      le temps restant avant la fin ddu jour ou la fin de la nuit
     */
    String cetus_day_night_time() {
        String timer = "";
        if (_date_expiration - System.currentTimeMillis() > NightTime) {
            _day_or_night = sDayNight[0];
            timer = TimestampToDate.convert(_date_expiration - System.currentTimeMillis() - (NightTime),true);
        } else if ((_date_expiration - System.currentTimeMillis() <= NightTime) && (_date_expiration - System.currentTimeMillis() >= 0)) {
            _day_or_night = sDayNight[1];
            timer = TimestampToDate.convert(_date_expiration - System.currentTimeMillis(),true);
        } else {
            _day_or_night = sDayNight[2];
        }

        return timer;
    }

    /**
     * Retourne un string avec le datetime de la fin du jour de ou la nnuit
     *
     * @return      le datetime entre la fin du jour ou de la nuit
     */
    String cetus_next_cycle_date() {
        String date = "";
        if (_day_or_night.compareTo(sDayNight[0]) == 0) {
            date = (android.text.format.DateFormat.format("HH:mm:ss", System.currentTimeMillis() + (_date_expiration - System.currentTimeMillis()) - (NightTime)).toString());
        } else if (_day_or_night.compareTo(sDayNight[1]) == 0) {
            date = (android.text.format.DateFormat.format("HH:mm:ss", System.currentTimeMillis() + (_date_expiration - System.currentTimeMillis())).toString());
        }
        return date;
    }

    /**
     * Retourne un boolean pour savoir si nous somme encore en transition entre le jour et la nuit
     *
     * @return      true si indéterminté, false si déterminé
     */
    boolean get_status() {
        return (_day_or_night.compareTo(sDayNight[2]) == 0);
    }

    /**
     * Retourne un string sur l'état du cycle jour/nuit ou indéterminé
     *
     * @return      l'état du cycle
     */
    String cetus_day_or_night() {
        return _day_or_night;
    }

    /**
     * Retourne les étapes de la bounty
     *
     * @return      les étapes de la bounty
     */
    List<CetusJobClass> get_cetus_jobs() { return _cetus_jobs; }

}