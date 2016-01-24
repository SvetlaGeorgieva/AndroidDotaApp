package com.fivepins.dotaapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by e30 on 1/24/2016.
 * Object for a Player from a Match.
 */
public class Player {

    public String playerName;
    public String heroName;
    public String heroIconName;

    private static final String TAG_NAME = "name";
    private static final String TAG_HERO = "hero";

    public Player (String playerName, String heroName) {
        this.playerName = playerName;
        this.heroName = heroName;
        this.heroIconName = "hero_" + heroName;
    }

    public Player(JSONObject jsonObjectPlayer) {
        try {
            setPlayerName(jsonObjectPlayer);
            setHeroName(jsonObjectPlayer);
            this.heroIconName = "hero_" + heroName;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerName(JSONObject jsonObjectPlayer) throws JSONException {
        this.playerName = jsonObjectPlayer.getString(TAG_NAME);
    }

    public void setHeroName(JSONObject jsonObjectPlayer) throws JSONException {
        this.heroName = jsonObjectPlayer.getString(TAG_HERO);
    }
}
