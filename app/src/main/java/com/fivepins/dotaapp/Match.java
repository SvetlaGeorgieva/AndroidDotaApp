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
    private static final String TAG_MATCHES = "result";
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
            JSONObject radiantTeamJSON = jsonObjectMatch.getJSONObject(TAG_RADIANT_TEAM);
            this.radiantTeamName = radiantTeamJSON.getString(TAG_TEAM_NAME);

            JSONObject direTeamJSON = jsonObjectMatch.getJSONObject(TAG_DIRE_TEAM);
            this.direTeamName = direTeamJSON.getString(TAG_TEAM_NAME);

            JSONObject scoreboard = jsonObjectMatch.getJSONObject(TAG_SCOREBOARD);
            JSONObject radiantTeamStats = scoreboard.getJSONObject(TAG_RADIANT_STATS);
            JSONObject direTeamStats = scoreboard.getJSONObject(TAG_DIRE_STATS);
            this.radiantTeamKills = Integer.parseInt(radiantTeamStats.getString(TAG_SCORE));
            this.direTeamKills = Integer.parseInt(direTeamStats.getString(TAG_SCORE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
