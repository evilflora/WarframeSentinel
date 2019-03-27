package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    JSONArray getAlerts() { // Les alertes, depuis la version 24.3 elles n'existent plus
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

    JSONArray getNews() { // Les news
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

    JSONArray getGoals() { // Event en cours
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

    JSONArray getSorties() { // Les sorties
        try {
            return data.getJSONArray("Sorties");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Sorties or no sortie avaialble");
            return null;
        }
    }
    short getSortiesLenght() {
        return (short)getSorties().length();
    }

    JSONArray getSyndicateMissions( ArrayList<String> list) {
        JSONArray allSyndicateMissions, syndicateMissions = new JSONArray();
        try {
            allSyndicateMissions = data.getJSONArray("SyndicateMissions");
            if (allSyndicateMissions != null) {
                for(int i = 0; i < allSyndicateMissions.length(); i++) { // on parcours la liste de tous les syndicats
                    for(int j = 0; j < list.size(); j++) { // on parcours la liste des syndicats souhaités
                        if (allSyndicateMissions.getJSONObject(i).getString("Tag").compareTo(list.get(j)) == 0) { // si on trouve le syndicat
                            syndicateMissions.put(allSyndicateMissions.getJSONObject(i)); // on l'ajoute à notre return
                            break; // Comme on l'a trouvé, on passe au syndicat suivant de la liste des syndicats
                        }
                    }
                }
            } else {
                Log.e("WarframeWorldState","Cannot retreive SyndicateMissions or no syndicate missions avaialble");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return syndicateMissions;

    }

    JSONArray getShipSyndicateMissions() {
        // Ne pas changer l'ordre de la liste, sinon la vue en sera impactée
        return  getSyndicateMissions(new ArrayList<>(Arrays.asList("SteelMeridianSyndicate","ArbitersSyndicate","CephalonSudaSyndicate","PerrinSyndicate","RedVeilSyndicate","NewLokaSyndicate")));
    }

    JSONArray getCetusMissions() {
        return  getSyndicateMissions(new ArrayList<>(Collections.singletonList("CetusSyndicate")));
    }
    short getCetusMissionsLenght() {
        return (short)getCetusMissions().length();
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

    JSONArray getInvasions() { // Invasions
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

    JSONArray getProjectPct() { // Construction Formorian, Razoback et Relay
        try {
            return data.getJSONArray("ProjectPct");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive construction project data or no construction project are avaialble");
            return null;
        }
    }

    JSONArray getNodeOverrides() { // todo : Nodes affectées par la Kuva Fortress
        try {
            return data.getJSONArray("NodesOverrides");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive nodes overrides or no nodes overrides are avaialble");
            return null;
        }
    }

    JSONArray getBadlandNodes() { // todo : Dark Sectors
        try {
            return data.getJSONArray("BadelandNodes");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive dark sectors or no dark sectors are avaialble");
            return null;
        }
    }

    JSONArray getPVPChallengeInstances() { // todo : PVP
        try {
            return data.getJSONArray("PVPChallengeInstances");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive pvp challenges or no pvp challenges are avaialble");
            return null;
        }
    }

    JSONArray getSeasonInfo() { // todo : PVP
        try {
            return data.getJSONArray("SeasonInfo");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive nightwaves or no nightwaves are avaialble");
            return null;
        }
    }
}