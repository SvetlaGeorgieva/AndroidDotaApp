package com.fivepins.matchtracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by e30 on 11/28/2015.
 * Object that contains information about a Dota Match object.
 * score info, team names info, league info
 */
public class Match {
    public String radiantTeamName;
    public String direTeamName;
    public String leagueName;
    public String radiantTeamId;
    public String direTeamId;
    public ArrayList<Player> radiantTeamPlayers;
    public ArrayList<Player> direTeamPlayers;

    private TeamScore radiantTeamScore;
    private TeamScore direTeamScore;
    private String duration;


    //JSON Node Names
    private static final String TAG_RADIANT = "radiant";
    private static final String TAG_DIRE = "dire";
    private static final String TAG_NAME = "name";
    private static final String TAG_LEAGUE = "league";
    private static final String TAG_SCORE = "score";
    private static final String TAG_ID = "id";
    private static final String TAG_PLAYERS = "players";
    private final String TAG_DURATION = "duration";

    public Match(String radiantTeamName, String direTeamName, String leagueName, String radiantTeamId, String direTeamId,
                 ArrayList<Player> radiantTeamPlayers, ArrayList<Player> direTeamPlayers, TeamScore radiantTeamScore,
                 TeamScore direTeamScore, String matchDuration) {
        this.radiantTeamName = radiantTeamName;
        this.direTeamName = direTeamName;
        this.leagueName = leagueName;
        this.radiantTeamId = radiantTeamId;
        this.direTeamId = direTeamId;
        this.radiantTeamPlayers = radiantTeamPlayers;
        this.direTeamPlayers = direTeamPlayers;
        this.radiantTeamScore = radiantTeamScore;
        this.direTeamScore = direTeamScore;
        this.duration = matchDuration;
    }

    public Match(JSONObject jsonObjectMatch) {
        try {
            setRadiantTeam(jsonObjectMatch);
            setDireTeam(jsonObjectMatch);
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
        setMatchDuration(score);

        JSONObject radiantScoreJSON = score.getJSONObject(TAG_RADIANT);
        this.radiantTeamScore = new TeamScore(radiantScoreJSON);

        JSONObject direScoreJSON = score.getJSONObject(TAG_DIRE);
        this.direTeamScore = new TeamScore(direScoreJSON);
    }

    public void setMatchDuration(JSONObject jsonObjectScore) {
        try {
            final String duration = jsonObjectScore.getString(TAG_DURATION);
            String timeElapsed = convertTime(duration);
            this.duration = timeElapsed;
        } catch (JSONException e) {
            System.out.println("Error getting match duration from JSON");
            e.printStackTrace();
        }
    }

    // The matchDuration appears as time elapsed in seconds.
    // Example: duration: 1594.289551
    // This method converts it in format MM:SS
    private String convertTime(String matchDuration) {
        final String[] durationParts = matchDuration.split("\\.");
        final int durationInSeconds = Integer.parseInt(durationParts[0]);
        int minutes = durationInSeconds / 60;
        int seconds = durationInSeconds % 60;

        String ss;
        if (seconds < 10) {
            ss = "0" + seconds;
        } else {
            ss = "" + seconds;
        }

        String mm;
        if (minutes < 10) {
            mm = "0" + minutes;
        } else {
            mm = "" + minutes;
        }

        return mm + ":" + ss;
    }

    private void setRadiantTeam(JSONObject jsonObjectMatch) throws JSONException {
        JSONObject radiantTeamJSON = jsonObjectMatch.getJSONObject(TAG_RADIANT);
        this.radiantTeamName = radiantTeamJSON.getString(TAG_NAME);
        this.radiantTeamId = radiantTeamJSON.getString(TAG_ID);

        JSONArray radiantTeamPlayers = radiantTeamJSON.getJSONArray(TAG_PLAYERS);
        this.radiantTeamPlayers = setTeamPlayers(radiantTeamPlayers);
    }

    private void setDireTeam(JSONObject jsonObjectMatch) throws JSONException {
        JSONObject direTeamJSON = jsonObjectMatch.getJSONObject(TAG_DIRE);
        this.direTeamName = direTeamJSON.getString(TAG_NAME);
        this.direTeamId = direTeamJSON.getString(TAG_ID);

        JSONArray direTeamPlayers = direTeamJSON.getJSONArray(TAG_PLAYERS);
        this.direTeamPlayers = setTeamPlayers(direTeamPlayers);
    }

    private ArrayList<Player> setTeamPlayers(JSONArray jsonObjectPlayers) throws JSONException {
        ArrayList<Player> playersList = new ArrayList<>();
        for(int i = 0; i < jsonObjectPlayers.length(); i++){
            JSONObject playerJSON = jsonObjectPlayers.getJSONObject(i);
            Player player = new Player(playerJSON);
            playersList.add(player);
        }
        return playersList;
    }

    public TeamScore getRadiantTeamScore() {
        return this.radiantTeamScore;
    }

    public TeamScore getDireTeamScore() {
        return this.direTeamScore;
    }

    public String getDuration() {
        return this.duration;
    }
}
