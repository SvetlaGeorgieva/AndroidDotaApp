package com.fivepins.matchtracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    private Score radiantScore;
    private Score direScore;


    //JSON Node Names
    private static final String TAG_RADIANT = "radiant";
    private static final String TAG_DIRE = "dire";
    private static final String TAG_NAME = "name";
    private static final String TAG_LEAGUE = "league";
    private static final String TAG_SCORE = "score";
    private static final String TAG_ID = "id";
    private static final String TAG_PLAYERS = "players";

    // TODO add score
    public Match(String radiantTeamName, String direTeamName, String leagueName, String radiantTeamId, String direTeamId,
                 ArrayList<Player> radiantTeamPlayers, ArrayList<Player> direTeamPlayers, Score radiantScore, Score direScore) {
        this.radiantTeamName = radiantTeamName;
        this.direTeamName = direTeamName;
        this.leagueName = leagueName;
        this.radiantTeamId = radiantTeamId;
        this.direTeamId = direTeamId;
        this.radiantTeamPlayers = radiantTeamPlayers;
        this.direTeamPlayers = direTeamPlayers;
        this.radiantScore = radiantScore;
        this.direScore = direScore;
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

        JSONObject radiantScoreJSON = score.getJSONObject(TAG_RADIANT);
        this.radiantScore = new Score(radiantScoreJSON);

        JSONObject direScoreJSON = score.getJSONObject(TAG_DIRE);
        this.direScore = new Score(direScoreJSON);
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

    public Score getRadiantScore() {
        return this.radiantScore;
    }

    public Score getDireScore() {
        return this.direScore;
    }

}
