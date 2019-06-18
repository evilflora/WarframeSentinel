package com.evilflora.warframesentinel.Modele;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WarframeWorldState {

    private String _currentFileName = "WarframeWorldState";
    private JSONObject _data;
    private String _platform;

    /**
     * Warframe World State
     *
     * @param platform           Where to retrieve feed
     */
    public WarframeWorldState(String platform) {
        this._platform = platform;
        this._data = this.readWarframeFeed();
    }

    /**
     * Reload the Warframe World State
     *
     * @param platform           Where to retrieve feed
     */
    public void reloadWarframeWorldSate(String platform) {
        this._platform = platform;
        this._data = this.readWarframeFeed();
    }

    /**
     * Read the feed
     */
    private JSONObject readWarframeFeed() {
        try {
            return new LoadWarframeWorldState().execute(_platform).get();
        } catch (Exception e) {
            Log.e(_currentFileName,"Cannot retrieve feed : " + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Retrieve Alerts
     */
    public JSONArray getAlerts() {
        try {
            return _data.getJSONArray("Alerts");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve alerts or no alert available");
            return null;
        }
    }

    /**
     * Number of Alerts
     */
    public short getAlertsLenght() {
        return (short)getAlerts().length();
    }

    /**
     * Retrieve News
     */
    public JSONArray getNews() {
        try {
            return _data.getJSONArray("Events"); // Yeah, it's really the news
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve news or no news available");
            return null;
        }
    }

    /**
     * Retrieve Gols
     */
    public JSONArray getGoals() {
        try {
            return _data.getJSONArray("Goals");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve goals or no goals available");
            return null;
        }
    }

    /**
     * Retrieve Sorties
     */
    public JSONArray getSorties() { // Les sorties
        try {
            return _data.getJSONArray("Sorties");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve sorties or no sorties available");
            return null;
        }
    }

    /**
     * Retrieve a specific list of Syndicates
     *
     * @param list a code named list of syndicates
     * @return list of selected syndicates
     */
    private JSONArray getSyndicateMissions( ArrayList<String> list) {
        JSONArray allSyndicateMissions;
        JSONArray syndicateMissions = new JSONArray();
        try {
            if (_data != null) {
                allSyndicateMissions = _data.getJSONArray("SyndicateMissions");
            } else {
                allSyndicateMissions = null;
            }
            if (allSyndicateMissions != null) {
                for(int i = 0; i < allSyndicateMissions.length(); i++) {
                    for(int j = 0; j < list.size(); j++) {
                        if (allSyndicateMissions.getJSONObject(i).getString("Tag").compareTo(list.get(j)) == 0) {
                            syndicateMissions.put(allSyndicateMissions.getJSONObject(i));
                            break;
                        }
                    }
                }
            } else {
                Log.e(_currentFileName,"Cannot retrieve syndicate missions or no syndicate missions available");
            }
        } catch (JSONException e) {
            Log.e(_currentFileName,"Error while retrieving syndicate missions or no syndicate missions available");
        }
        return syndicateMissions;

    }

    /**
     * Retrieve Syndicates that can be found on ship and on relays
     */
    public JSONArray getShipSyndicateMissions() {
        return getSyndicateMissions(new ArrayList<>(Arrays.asList("SteelMeridianSyndicate","ArbitersSyndicate","CephalonSudaSyndicate","PerrinSyndicate","RedVeilSyndicate","NewLokaSyndicate")));
    }

    /**
     * Retrieve Cetus / Plains of Eidolon Syndicate
     */
    public JSONArray getCetusMissions() {
        return getSyndicateMissions(new ArrayList<>(Collections.singletonList("CetusSyndicate")));
    }

    /**
     * Retrieve Nightwave missions (it's not really a Syndicate but still present on the list, data are somewhere else)
     */
    public JSONObject getLegion() {
        try {
            return _data.getJSONObject("SeasonInfo");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve nightwave or no nightwave available");
            return null;
        }
    }

    /**
     * Retrieve Fortuna / Orb Vallis Syndicate
     */
    public JSONArray getOrbVallisMissions() {
        return getSyndicateMissions(new ArrayList<>(Collections.singletonList("SolarisSyndicate")));
    }

    /**
     * Retrieve Fissures
     */
    public JSONArray getFissures() {
        try {
            return _data.getJSONArray("ActiveMissions");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve fissures or no fissure available");
            return null;
        }
    }

    /**
     * Number of Fissures
     */
    public short getFissuresLenght() {
        return (short)getFissures().length();
    }

    /**
     * Retrieve Invasions
     */
    public JSONArray getInvasions() {
        try {
            return _data.getJSONArray("Invasions");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve Invasions or no invasion available");
            return null;
        }
    }

    /**
     * Number of Fissures
     */
    public short getInvasionsCurrentLenght() {
        return (short)getInvasions().length();
    }

    /**
     * Retrieve Market Sold
     */
    public JSONArray getFlashSales() {
        try {
            return _data.getJSONArray("FlashSales");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve FlashSales or no flash sales available");
            return null;
        }
    }

    /**
     * Retrieve Darvo Sold
     */
    public JSONArray getDailyDeals() {
        try {
            return _data.getJSONArray("DailyDeals");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve DailyDeals or no flash sales available");
            return null;
        }
    }

    /**
     * Retrieve Baro ki'teer Sold
     */
    public JSONArray getVoidTraders() {
        try {
            return _data.getJSONArray("VoidTraders");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve VoidTraders or no void traders available");
            return null;
        }
    }

    /**
     * Retrieve Construction Project for Formorian, Razoback and Relay (not sure for the last one)
     */
    public JSONArray getProjectPct() { //
        try {
            return _data.getJSONArray("ProjectPct");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve construction project data or no construction project are available");
            return null;
        }
    }

    /**
     * Retrieve PVP Challenges
     */
    public JSONArray getPvpChallengeInstances() {
        try {
            return _data.getJSONArray("PVPChallengeInstances");
        } catch (JSONException e) {
            Log.e(_currentFileName,"Cannot retrieve pvp challenges or no pvp challenges are available");
            return null;
        }
    }
}