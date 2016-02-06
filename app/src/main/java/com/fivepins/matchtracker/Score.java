package com.fivepins.matchtracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by e30 on 1/31/2016.
 * Holds information about the score of a team.
 * <p/>
 * Bit representation of the towers and barracks state.
 * If 1 -> the tower/barrack is standing; If 0 -> the tower/barrack is down.
 * <p/>
 * String towersTop -> {Top-3}{Top-2}{Top-1}
 * String towersMid -> {Mid-3}{Mid-2}{Mid-1}
 * String towersBot -> {Bot-3}{Bot-2}{Bot-1}
 * <p/>
 * String barracksTop -> {Top-Ranged}{Top-Melle}
 * String barracksMid -> {Mid-Ranged}{Mid-Melle}
 * String barracksBot -> {Bot-Ranged}{Bot-Melle}
 * <p/>
 * String ancients -> {Throne}{Top-Ancient}{Bot-Ancient}
 */
public class Score {

    private int kills;
    private int gamesWon;
    private String towersTop;
    private String towersMid;
    private String towersBot;
    private String barracksTop;
    private String barracksMid;
    private String barracksBot;
    private String throne;

    private final String TAG_KILL_SCORE = "kills";
    private final String TAG_GAMES_WON = "games";

    private final String TAG_TOWERS = "towers";
    private final String TAG_BARRACKS = "barracks";
    private final String TAG_TOP = "top";
    private final String TAG_MID = "mid";
    private final String TAG_BOT = "bot";
    private final String TAG_THRONE = "throne";

    public Score(JSONObject teamScoreJSON) {
        setKills(teamScoreJSON);
        setGamesWon(teamScoreJSON);
        setTowersState(teamScoreJSON);
        setBarracksState(teamScoreJSON);
    }

    public Score (int kills, String towersTop, String towersMid, String towersBot, String barracksTop,
                  String barracksMid, String barracksBot, String throne) {
        this.kills = kills;
        this.towersTop = towersTop;
        this.towersMid = towersMid;
        this.towersBot = towersBot;
        this.barracksTop = barracksTop;
        this.barracksMid = barracksMid;
        this.barracksBot = barracksBot;
        this.throne = throne;
    }

    private void setKills(JSONObject teamScoreJSON) {
        try {
            String teamKills = teamScoreJSON.getString(TAG_KILL_SCORE);
            this.kills = Integer.parseInt(teamKills);
        } catch (JSONException e) {
            System.out.println("Error getting team kill score from JSON");
            e.printStackTrace();
        }
    }

    private void setGamesWon(JSONObject teamScoreJSON) {
        try {
            String gamesWon= teamScoreJSON.getString(TAG_GAMES_WON);
            this.gamesWon = Integer.parseInt(gamesWon);
        } catch (JSONException e) {
            System.out.println("Error getting games won from JSON");
            e.printStackTrace();
        }
    }

    private void setTowersState(JSONObject teamScoreJSON) {
        try {
            JSONObject towerState = teamScoreJSON.getJSONObject(TAG_TOWERS);
            String towersTop = towerState.getString(TAG_TOP);
            String towersMid = towerState.getString(TAG_MID);
            String towersBot = towerState.getString(TAG_BOT);
            String throne = towerState.getString(TAG_THRONE);

            this.towersTop = towersTop;
            this.towersMid = towersMid;
            this.towersBot = towersBot;
            this.throne = throne;
        } catch (JSONException e) {
            System.out.println("Error getting towers state from JSON");
            e.printStackTrace();
        }
    }

    private void setBarracksState(JSONObject teamScoreJSON) {
        try {
            JSONObject barracksState = teamScoreJSON.getJSONObject(TAG_BARRACKS);
            String barracksTop = barracksState.getString(TAG_TOP);
            String barracksMid = barracksState.getString(TAG_MID);
            String barracksBot = barracksState.getString(TAG_BOT);

            this.barracksTop = barracksTop;
            this.barracksMid = barracksMid;
            this.barracksBot = barracksBot;
        } catch (JSONException e) {
            System.out.println("Error getting barracks state from JSON");
            e.printStackTrace();
        }
    }

    public int getKills() {
        return this.kills;
    }

    public int getGamesWon() {
        return this.gamesWon;
    }

    public String getTowersTop() {
        return this.towersTop;
    }

    public String getTowersMid() {
        return this.towersMid;
    }

    public String getTowersBot() {
        return this.towersBot;
    }

    public String getBarracksTop() {
        return this.barracksTop;
    }

    public String getBarracksMid() {
        return this.barracksMid;
    }

    public String getBarracksBot() {
        return this.barracksBot;
    }

    public String getThrone() {
        return this.throne;
    }
}
