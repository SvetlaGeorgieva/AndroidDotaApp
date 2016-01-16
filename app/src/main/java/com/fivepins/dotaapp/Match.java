package com.fivepins.dotaapp;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by e30 on 11/28/2015.
 */
public class Match {
    public String radiantTeamName;
    public String direTeamName;
    public int radiantTeamKills;
    public int direTeamKills;

    //JSON Node Names
    private static final String TAG_RADIANT_TEAM = "radiant_team";
    private static final String TAG_DIRE_TEAM = "dire_team";
    private static final String TAG_TEAM_NAME = "team_name";
    private static final String TAG_SCOREBOARD = "scoreboard";
    private static final String TAG_RADIANT_STATS = "radiant";
    private static final String TAG_DIRE_STATS = "dire";
    private static final String TAG_SCORE = "score";

    public Match(String radiantTeamName, String direTeamName, int radiantTeamKills, int direTeamKills) {
        this.radiantTeamName = radiantTeamName;
        this.direTeamName = direTeamName;
        this.radiantTeamKills = radiantTeamKills;
        this.direTeamKills = direTeamKills;
    }

    public Match(JSONObject jsonObjectMatch) {
        try {
            // Storing  JSON item in a Variable
            if (jsonObjectMatch.isNull(TAG_RADIANT_TEAM)){
                this.radiantTeamName = "Radiant Team";
            } else {
                JSONObject radiantTeamJSON = jsonObjectMatch.getJSONObject(TAG_RADIANT_TEAM);
                String radiantTeamName = radiantTeamJSON.getString(TAG_TEAM_NAME);
                this.radiantTeamName = radiantTeamName;
            }

            if (jsonObjectMatch.isNull(TAG_DIRE_TEAM)){
                this.direTeamName = "Dire Team";
            } else {
                JSONObject direTeamJSON = jsonObjectMatch.getJSONObject(TAG_DIRE_TEAM);
                String direTeamName = direTeamJSON.getString(TAG_TEAM_NAME);
                this.direTeamName = direTeamName;
            }

            if (jsonObjectMatch.isNull(TAG_SCOREBOARD)){
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
