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
    public int leagueTier;
    public String leagueName;

    //JSON Node Names
    private static final String TAG_RADIANT_TEAM = "radiant_team";
    private static final String TAG_DIRE_TEAM = "dire_team";
    private static final String TAG_TEAM_NAME = "team_name";
    private static final String TAG_SCOREBOARD = "scoreboard";
    private static final String TAG_RADIANT_STATS = "radiant";
    private static final String TAG_DIRE_STATS = "dire";
    private static final String TAG_SCORE = "score";
    private static final String TAG_LEAGUE_TEAR = "league_tier";
    private static final String TAG_LEAGUE = "league";
    private static final String TAG_LEAGUE_NAME = "name";

    public Match(String radiantTeamName, String direTeamName, int radiantTeamKills, int direTeamKills, int leagueTier, String leagueName) {
        this.radiantTeamName = radiantTeamName;
        this.direTeamName = direTeamName;
        this.radiantTeamKills = radiantTeamKills;
        this.direTeamKills = direTeamKills;
        this.leagueTier = leagueTier;
        this.leagueName = leagueName;
    }

    public Match(JSONObject jsonObjectMatch) {
        try {
            setRadiantTeamName(jsonObjectMatch);
            setDireTeamName(jsonObjectMatch);

            setScore(jsonObjectMatch);

            setLeagueTear(jsonObjectMatch);
            setLeagueName(jsonObjectMatch);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLeagueName(JSONObject jsonObjectMatch) throws JSONException {
        JSONObject league = jsonObjectMatch.getJSONObject(TAG_LEAGUE);
        this.leagueName = league.getString(TAG_LEAGUE_NAME);
    }

    private void setLeagueTear(JSONObject jsonObjectMatch) throws JSONException {
        String leagueTier = jsonObjectMatch.getString(TAG_LEAGUE_TEAR);
        this.leagueTier = Integer.parseInt(leagueTier);
    }

    private void setScore(JSONObject jsonObjectMatch) throws JSONException {
        if (jsonObjectMatch.isNull(TAG_SCOREBOARD)) {
            this.radiantTeamKills = 0;
            this.direTeamKills = 0;
        } else {
            JSONObject scoreboard = jsonObjectMatch.getJSONObject(TAG_SCOREBOARD);
            JSONObject radiantTeamStats = scoreboard.getJSONObject(TAG_RADIANT_STATS);
            JSONObject direTeamStats = scoreboard.getJSONObject(TAG_DIRE_STATS);
            String radiantTeamKills = radiantTeamStats.getString(TAG_SCORE);
            String direTeamKills = direTeamStats.getString(TAG_SCORE);

            this.radiantTeamKills = Integer.parseInt(radiantTeamKills);
            this.direTeamKills = Integer.parseInt(direTeamKills);
        }
    }

    private void setDireTeamName(JSONObject jsonObjectMatch) throws JSONException {
        if (jsonObjectMatch.isNull(TAG_DIRE_TEAM)) {
            this.direTeamName = "Dire Team";
        } else {
            JSONObject direTeamJSON = jsonObjectMatch.getJSONObject(TAG_DIRE_TEAM);
            this.direTeamName = direTeamJSON.getString(TAG_TEAM_NAME);
        }
    }

    private void setRadiantTeamName(JSONObject jsonObjectMatch) throws JSONException {
        if (jsonObjectMatch.isNull(TAG_RADIANT_TEAM)) {
            this.radiantTeamName = "Radiant Team";
        } else {
            JSONObject radiantTeamJSON = jsonObjectMatch.getJSONObject(TAG_RADIANT_TEAM);
            this.radiantTeamName = radiantTeamJSON.getString(TAG_TEAM_NAME);
        }
    }

}
