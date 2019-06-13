package com.evilflora.warframesentinel.Modele;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class WarframeWorldState {

    private JSONObject _data;
    private String _platform = "";

    public WarframeWorldState(String platform) {
        this._platform = platform;
        this._data = this.readWarframeFeed();
    }

    public void ReloadWarframeWorldSate(String platform) {
        this._platform = platform;
        this._data = this.readWarframeFeed();
    }

    public JSONObject getData() {
        return this._data;
    }

    public JSONObject readWarframeFeed() { // http://content.warframe.com/dynamic/worldState.php
        try {
            return new LoadWarframeWorldState().execute(_platform).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getAlerts() { // Les alertes, depuis la version 24.3 elles n'existent plus
        try {
            return _data.getJSONArray("Alerts");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive alerts or no alert avaialble");
            return null;
        }
    }
    public short getAlertsLenght() {
        return (short)getAlerts().length();
    }

    public JSONArray getNews() { // Les news
        try {
            return _data.getJSONArray("Events");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Events or no event avaialble");
            return null;
        }
    }
    public short getNewsLenght() {
        return (short)getNews().length();
    }

    public JSONArray getGoals() { // Event en cours
        try {
            return _data.getJSONArray("Goals");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Goals or no goal avaialble");
            return null;
        }
    }

    public JSONArray getSorties() { // Les sorties
        try {
            return _data.getJSONArray("Sorties");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Sorties or no sortie avaialble");
            return null;
        }
    }

    private JSONArray getSyndicateMissions( ArrayList<String> list) {
        JSONArray allSyndicateMissions;
        JSONArray syndicateMissions = new JSONArray();
        try {
            allSyndicateMissions = _data.getJSONArray("SyndicateMissions");
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
                Log.e("WarframeWorldState","Cannot retrieve SyndicateMissions or no syndicate missions available");
            }
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Error while retrieving SyndicateMissions or no syndicate missions available");;
        }
        return syndicateMissions;

    }

    public JSONArray getShipSyndicateMissions() {
        // Ne pas changer l'ordre de la liste, sinon la vue en sera impactée
        return  getSyndicateMissions(new ArrayList<>(Arrays.asList("SteelMeridianSyndicate","ArbitersSyndicate","CephalonSudaSyndicate","PerrinSyndicate","RedVeilSyndicate","NewLokaSyndicate")));
    }

    public JSONArray getCetusMissions() {
        return  getSyndicateMissions(new ArrayList<>(Collections.singletonList("CetusSyndicate")));
    }

    public JSONObject getLegion() {
        try {
            return  _data.getJSONObject("SeasonInfo");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive SeasonInfo or no SeasonInfo avaialble");
            return null;
        }
    }

    public JSONArray getOrbVallisMissions() {
        return  getSyndicateMissions(new ArrayList<>(Collections.singletonList("SolarisSyndicate")));
    }

    public JSONArray getFissures() {
        try {
            return _data.getJSONArray("ActiveMissions");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Fissures or no fissure avaialble");
            return null;
        }
    }
    public short getFissuresLenght() {
        return (short)getFissures().length();
    }

    public JSONArray getInvasions() { // Invasions
        try {
            return _data.getJSONArray("Invasions");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive Invasions or no invasion avaialble");
            return null;
        }
    }
    public short getInvasionsCurrentLenght() {
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

    public JSONArray getFlashSales() { // Market
        try {
            return _data.getJSONArray("FlashSales");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive FlashSales or no flash sales avaialble");
            return null;
        }
    }

    public JSONArray getDailyDeals() { // Darvo
        try {
            return _data.getJSONArray("DailyDeals");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive DailyDeals or no flash sales avaialble");
            return null;
        }
    }

    public JSONArray getVoidTraders() { // Baro ki'teer
        try {
            return _data.getJSONArray("VoidTraders");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive VoidTraders or no void traders avaialble");
            return null;
        }
    }

    public JSONArray getProjectPct() { // Construction Formorian, Razoback et Relay
        try {
            return _data.getJSONArray("ProjectPct");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive construction project data or no construction project are avaialble");
            return null;
        }
    }

    public JSONArray getNodeOverrides() { // todo : Nodes affectées par la Kuva Fortress
        try {
            return _data.getJSONArray("NodesOverrides");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive nodes overrides or no nodes overrides are avaialble");
            return null;
        }
    }

    public JSONArray getBadlandNodes() { // todo : Dark Sectors
        try {
            return _data.getJSONArray("BadelandNodes");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive dark sectors or no dark sectors are avaialble");
            return null;
        }
    }

    public JSONArray getPvpChallengeInstances() { // todo : PVP
        try {
            return _data.getJSONArray("PVPChallengeInstances");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive pvp challenges or no pvp challenges are avaialble");
            return null;
        }
    }

    public JSONArray getSeasonInfo() { // todo : PVP
        try {
            return _data.getJSONArray("SeasonInfo");
        } catch (JSONException e) {
            Log.e("WarframeWorldState","Cannot retreive nightwaves or no nightwaves are avaialble");
            return null;
        }
    }
}