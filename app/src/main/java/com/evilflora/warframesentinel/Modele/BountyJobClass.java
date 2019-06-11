package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class BountyJobClass {
    private static String _currentFileName = "BountyJobClass"; // filename
    private Context _context;
    private String _jobType;
    //private String _rewards;
    private String _minEnnemyLevel;
    private String _maxEnnemyLevel;
    private int[] _xpAmounts;

    /**
     * Return time left before reset of bounty
     *
     * @param context           Activity context
     * @param cetusJob          The JSONArray containing data
     */
    BountyJobClass(Context context, JSONObject cetusJob) {
        try {
            this._context           = context;
            this._jobType          = cetusJob.getString("jobType");
            //this._rewards           = cetusJob.getString("rewards");
            this._minEnnemyLevel  = cetusJob.getString("minEnemyLevel");
            this._maxEnnemyLevel  = cetusJob.getString("maxEnemyLevel");
            this._xpAmounts = new int[cetusJob.getJSONArray("xpAmounts").length()];
        } catch (Exception ex) {
            Log.e(_currentFileName,"Error while reading cetus job - " + ex.getMessage());
        }
        try {
            for (int i = 0; i < _xpAmounts.length; i++) {
                this._xpAmounts[i] = cetusJob.getJSONArray("xpAmounts").getInt(i);
            }
        }catch (Exception ex) {
            Log.e(_currentFileName,"Error while reading job xp amount - " + ex.getMessage());
        }
    }

    /**
     * Returns the job type
     *
     * @return      string
     */
    public String getJobType() {
        String jobType = (_jobType != null ? _jobType.substring(_jobType.lastIndexOf('/')).replace("/","") : "" );
        try {
            return _context.getResources().getString(_context.getResources().getIdentifier(jobType, "string", _context.getPackageName()));
        } catch (Exception ex) {
            return jobType;
        }
    }

    /**
     * Returns the job's level range
     *
     * @return      string
     */
    public String getEnnemyLevel() {
        return _context.getResources().getString(_context.getResources().getIdentifier("ennemy_level", "string", _context.getPackageName()), _minEnnemyLevel, _maxEnnemyLevel);
    }

    /**
     * Returns the number of job steps
     *
     * @return      int
     */
    public int getNumberOfJobs() {
        return _xpAmounts.length;
    }

    /**
     * Returns the standing of a job step
     *
     * @return      int
     */
    public int getStandingForJob(int i) {
        return _xpAmounts[i];
    }

    /**
     * Returns the total job standing
     *
     * @return      string
     */
    public String getTotalStanding() {
        int sum = 0;
        for (int xp:_xpAmounts)
            sum += xp;
        return _context.getResources().getString(_context.getResources().getIdentifier("total_standing", "string", _context.getPackageName()), sum);
    }
}