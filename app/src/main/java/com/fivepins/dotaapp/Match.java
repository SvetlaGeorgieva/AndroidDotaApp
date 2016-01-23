package com.fivepins.dotaapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by e30 on 11/28/2015.
 * Object that contains information about a Dota Match object.
 */
public class Match {
    public String radiantTeamName;
    public String direTeamName;
    public int radiantTeamKills;
    public int direTeamKills;
    public String leagueName;

    //JSON Node Names
    private static final String TAG_RADIANT = "radiant";
    private static final String TAG_DIRE = "dire";
    private static final String TAG_SCORE = "score";
    private static final String TAG_LEAGUE = "league";
    private static final String TAG_NAME = "name";

    public Match(String radiantTeamName, String direTeamName, int radiantTeamKills, int direTeamKills, String leagueName) {
        this.radiantTeamName = radiantTeamName;
        this.direTeamName = direTeamName;
        this.radiantTeamKills = radiantTeamKills;
        this.direTeamKills = direTeamKills;
        this.leagueName = leagueName;
    }

    public Match(JSONObject jsonObjectMatch) {
        try {
            setRadiantTeamName(jsonObjectMatch);
            setDireTeamName(jsonObjectMatch);
            setScore(jsonObjectMatch);
            setLeagueName(jsonObjectMatch);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLeagueName(JSONObject jsonObjectMatch) throws JSONException {
        JSONObject league = jsonObjectMatch.getJSONObject(TAG_LEAGUE);
        this.leagueName = league.getString(TAG_NAME);
    }

    private void setScore(JSONObject jsonObjectMatch) throws JSONException {
        JSONObject score = jsonObjectMatch.getJSONObject(TAG_SCORE);
        String radiantTeamKills = score.getString(TAG_RADIANT);
        String direTeamKills = score.getString(TAG_DIRE);

        this.radiantTeamKills = Integer.parseInt(radiantTeamKills);
        this.direTeamKills = Integer.parseInt(direTeamKills);
    }

    private void setDireTeamName(JSONObject jsonObjectMatch) throws JSONException {
        JSONObject direTeamJSON = jsonObjectMatch.getJSONObject(TAG_DIRE);
        String direTeamName = direTeamJSON.getString(TAG_NAME);
        //TODO Will be changed how a Team with no name is received
        if (direTeamName.equals("null")) {
            this.direTeamName = "Dire Team";
        } else {
            this.direTeamName = direTeamName;
        }
    }

    private void setRadiantTeamName(JSONObject jsonObjectMatch) throws JSONException {
        JSONObject radiantTeamJSON = jsonObjectMatch.getJSONObject(TAG_RADIANT);
        String radiantTeamName = radiantTeamJSON.getString(TAG_NAME);
        //TODO Will be changed how a Team with no name is received
        if (radiantTeamName.equals("null")) {
            this.radiantTeamName = "Radiant Team";
        } else {
            this.radiantTeamName = radiantTeamName;
        }
    }

}
