package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import com.evilflora.warframesentinel.Utils.NumberToTimeLeft;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SortieClass {
    private static String _currentFileName = "SortieClass";
    private Context _context;
    //private String _id;
    private long _dateActivation;
    private long _dateExpiration;
    private String _boss;
    private List<SortieStepClass> _sorties = new ArrayList<>();
    private String[] _rewardsItems = {"AYATAN ANASA SCULPTURE", "RIVEN MOD", "6000 KUVA", "4000 ENDO", "3 DAY BOOSTER", "EXILUS ADAPTER", "FORMA", "OROKIN CATALYST BLEPRINT", "OROKIN REACTOR BLUEPRINT", "LEGENDARY CORE"};
    private double[] _rewardsDropChance = {28.00, 25.9, 14.00, 12.10, 9.81, 2.50, 2.50, 2.50, 2.50, 0.18 };

    /**
     * Return time left before reset of bounty
     *
     * @param context           Activity context
     * @param sortie             The JSONArray containing data
     */
    public SortieClass(Context context, JSONArray sortie) {
        try {
            this._context            = context;
            int[] credits = {20000, 30000, 50000};
            String[] level = {"50 - 60", "65 - 80", "80 - 100"};
            //this._id                = sortie.getJSONObject(0).getJSONObject("_id").getString("$oid");
            this._dateActivation   = sortie.getJSONObject(0).getJSONObject("Activation").getJSONObject("$date").getLong("$numberLong");
            this._dateExpiration   = sortie.getJSONObject(0).getJSONObject("Expiry").getJSONObject("$date").getLong("$numberLong");
            this._boss              = sortie.getJSONObject(0).getString("Boss");
            for (int i = 0; i < (sortie.getJSONObject(0).getJSONArray("Variants").length()); i++) {
                this._sorties.add(new SortieStepClass(_context,sortie.getJSONObject(0).getJSONArray("Variants").getJSONObject(i), credits[i], level[i]));
            }

        } catch (Exception e) {
            Log.e(_currentFileName,"Error while reading sortie");
        }
    }

    /**
     * Quantity of sortie steps
     *
     * @return      int
     */
    public int getSortieStepLenght() {
        return _sorties.size();
    }

    /**
     * Translated time left before end
     *
     * @return      string
     */
    public String getTimeBeforeEnd() {
        return _context.getResources().getString(_context.getResources().getIdentifier("time_before_reset", "string", _context.getPackageName()), NumberToTimeLeft.convert(_dateExpiration - System.currentTimeMillis(),true));
    }

    /**
     * Translated name of the current boss
     *
     * @return      string
     */
    public String getSortieType() {
        String sortieType;
        try {
            sortieType = _context.getResources().getString(_context.getResources().getIdentifier(_boss, "string", _context.getPackageName()));
        } catch (Exception ex) {
            sortieType = _boss;
        }
        return sortieType;
    }

    /**
     * Time left before end of the sortie
     *
     * @return      int
     */
    public long getTimeLeft() {
        return (_dateExpiration - _dateActivation);
    }

    /**
     * Get a step from the step list
     *
     * @return      SortieStepClass
     */
    public SortieStepClass getStep(int i) {
        return _sorties.get(i);
    }

    /**
     * The reward list
     *
     * @return      string[]
     */
    public String[] getRewards() {
        return _rewardsItems;
    }

    /**
     * Drop chance for each reward
     *
     * @return      double[]
     */
    public double[] getDropChance() {
        return _rewardsDropChance;
    }

}