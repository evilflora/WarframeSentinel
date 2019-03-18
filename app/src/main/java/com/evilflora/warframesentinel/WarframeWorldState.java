package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

class WarframeWorldState {

    private JSONObject data;
    private String _platform = "";

    WarframeWorldState(String platform) {
        this._platform = platform;
        this.data = this.readWarframeFeed();
    }

    void ReloadWarframeWorldSate(String platform) {
        this._platform = platform;
        this.data = this.readWarframeFeed();
    }

    public JSONObject getData() {
        return this.data;
    }

    private JSONObject readWarframeFeed() { // http://content.warframe.com/dynamic/worldState.php
        try {
            return new LoadWarframeWorldState().execute(_platform).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    JSONArray getAlerts() {
        try {
            return data.getJSONArray("Alerts");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive alerts or no alert avaialble");
            return null;
        }
    }

    short getAlertsLenght() {
        return (short)getAlerts().length();
    }

    JSONArray getNews() {
        try {
            return data.getJSONArray("Events");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Events or no event avaialble");
            return null;
        }
    }


    short getNewsLenght() {
        return (short)getNews().length();
    }

    JSONArray getGoals() {
        try {
            return data.getJSONArray("Goals");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Goals or no goal avaialble");
            return null;
        }
    }

    short getGoalsLenght() {
        return (short)getGoals().length();
    }

    JSONArray getSorties() {
        try {
            return data.getJSONArray("Sorties");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Sorties or no sortie avaialble");
            return null;
        }
    }

    private JSONArray getSyndicateMissions() {
        try {
            return data.getJSONArray("SyndicateMissions");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive SyndicateMissions or no syndicate missions avaialble");
            return null;
        }
    }

    JSONObject getCetusMissions() {
        JSONObject cetusMissions = null;
        JSONArray syndicateMissions = getSyndicateMissions();
        if (syndicateMissions != null) {
            for(int i = 0; i < syndicateMissions.length(); i++) {
                try {

                    if (syndicateMissions.getJSONObject(i).getString("Tag").compareTo("CetusSyndicate") == 0) {
                        cetusMissions = syndicateMissions.getJSONObject(i);
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("WarframeWorldState","Cannot retreive CetusJobs or no cetus jobs avaialble");
        }
        return cetusMissions;
    }

    JSONArray getFissures() {
        try {
            return data.getJSONArray("ActiveMissions");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Fissures or no fissure avaialble");
            return null;
        }
    }

    short getFissuresLenght() {
        return (short)getFissures().length();
    }

    JSONArray getInvasions() {
        try {
            return data.getJSONArray("Invasions");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Invasions or no invasion avaialble");
            return null;
        }
    }

    short getInvasionsCurrentLenght() {
        short _count = 0;
        for (short i = 0; i < getInvasions().length(); i++) {
            try {
                if (!getInvasions().getJSONObject(i).getBoolean("Completed"))
                    _count++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return (_count);
    }

    JSONArray getFlashSales() { // Market
        try {
            return data.getJSONArray("FlashSales");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive FlashSales or no flash sales avaialble");
            return null;
        }
    }

    JSONArray getDailyDeals() { // Darvo
        try {
            return data.getJSONArray("DailyDeals");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive DailyDeals or no flash sales avaialble");
            return null;
        }
    }

    JSONArray getVoidTraders() { // Baro ki'teer
        try {
            return data.getJSONArray("VoidTraders");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive VoidTraders or no void traders avaialble");
            return null;
        }
    }
}