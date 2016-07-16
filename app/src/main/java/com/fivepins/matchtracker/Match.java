package com.fivepins.matchtracker;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by e30 on 11/28/2015.
 * Object that contains information about a DoTA Match object.
 * score info, team names info, league info
 */
public class Match implements Comparable <Match> {
    public String radiantTeamName;
    public String direTeamName;
    public String leagueName;
    public String radiantTeamId;
    public String direTeamId;
    public ArrayList<Player> radiantTeamPlayers;
    public ArrayList<Player> direTeamPlayers;

    private String matchId;
    private TeamScore radiantTeamScore;
    private TeamScore direTeamScore;
    private String duration;
    private int series;

    private String matchStatus;
    private Date matchStartDateTime;

    private String winnerTeam;
    //JSON Node Names
    private static final String TAG_RADIANT = "radiant";
    private static final String TAG_DIRE = "dire";
    private static final String TAG_NAME = "name";
    private static final String TAG_LEAGUE = "league";
    private static final String TAG_SCORE = "score";
    private static final String TAG_ID = "id";
    private static final String TAG_PLAYERS = "players";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_START_DATE = "start_date";
    /**
     * TAG_SERIES is related to the "best of" number.
     * 0 -> best of 1. Only one match will be played
     * 1 -> best of 3. The team that takes 2 games wins.
     * 2 -> best of 5. The team that takes 3 games wins.
     */
    private static final String TAG_SERIES = "series";
    private static final String TAG_IS_LIVE = "is_live";
    private static final String TAG_RADIANT_WIN = "radiant_win";


    public Match(String matchId, String radiantTeamName, String direTeamName, String leagueName, String radiantTeamId, String direTeamId,
                 ArrayList<Player> radiantTeamPlayers, ArrayList<Player> direTeamPlayers, TeamScore radiantTeamScore,
                 TeamScore direTeamScore, String matchDuration, int series, String matchStatus, String winnerTeam) {
        this.matchId = matchId;
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
        this.series = series;
        this.matchStatus = matchStatus;
        this.winnerTeam = winnerTeam;
    }

    public Match(JSONObject jsonObjectMatch) {
        try {
            setMatchProperties(jsonObjectMatch);
            setRadiantTeam(jsonObjectMatch);
            setDireTeam(jsonObjectMatch);
            setScore(jsonObjectMatch);
            setWinner(jsonObjectMatch);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setMatchProperties(JSONObject jsonObjectMatch) throws JSONException {
        setMatchId(jsonObjectMatch);
        setMatchStatus(jsonObjectMatch);
        setSeriesType(jsonObjectMatch);
        setLeagueName(jsonObjectMatch);
        setMatchStartDateTime(jsonObjectMatch);
    }

    private void setMatchId(JSONObject jsonObjectMatch) throws JSONException {
        this.matchId = jsonObjectMatch.getString(TAG_ID);
    }

    private void setLeagueName(JSONObject jsonObjectMatch) throws JSONException {
        JSONObject league = jsonObjectMatch.getJSONObject(TAG_LEAGUE);
        this.leagueName = league.getString(TAG_NAME);
    }

    public void setSeriesType(JSONObject jsonObjectMatch) {
        try {
            this.series = jsonObjectMatch.getInt(TAG_SERIES);
        } catch (JSONException e) {
            System.out.println("Error getting match series type from JSON");
            e.printStackTrace();
        }
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
            this.duration = convertTime(duration);
        } catch (JSONException e) {
            System.out.println("ERROR: Error getting match duration from JSON");
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
        for (int i = 0; i < jsonObjectPlayers.length(); i++) {
            JSONObject playerJSON = jsonObjectPlayers.getJSONObject(i);
            Player player = new Player(playerJSON);
            playersList.add(player);
        }
        return playersList;
    }

    // TODO use enum for match status -> FINISHED, LIVE, INVALID
    private void setMatchStatus(JSONObject jsonObjectMatch) {
        try {
            int statusCode = jsonObjectMatch.getInt(TAG_IS_LIVE);
            switch (statusCode) {
                case 0:
                    this.matchStatus = "FINISHED";
                    break;
                case 1:
                    this.matchStatus = "LIVE";
                    break;
                default:
                    System.out.println("ERROR: Invalid status for matchID " + this.getMatchId());
                    this.matchStatus = "INVALID";
                    break;
            }
        } catch (JSONException e) {
            System.out.println("ERROR: can't set match status from JSON for matchID " + this.getMatchId());
            e.printStackTrace();
        }
    }

    public void setMatchStartDateTime(JSONObject jsonObjectMatch){
        try {
            // The date-time arrives in format like "2016-07-09 05:48:02"
            String matchStartDateTime = jsonObjectMatch.getString(TAG_START_DATE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date date = simpleDateFormat.parse(matchStartDateTime);
//            System.out.println("date : " + simpleDateFormat.format(date));
            this.matchStartDateTime = date;
        } catch (ParseException ex) {
            System.out.println("ERROR: can't parse match start date String to Date for matchID " + this.getMatchId());
        } catch (JSONException e) {
            System.out.println("ERROR: can't set match start date and time from JSON for matchID " + this.getMatchId());
            e.printStackTrace();
        }
    }

    //TODO use enum for winner -> RADIANT, DIRE, NONE
    private void setWinner(JSONObject jsonObjectMatch) {
        try {
            String matchStatus = this.getMatchStatus();
            if ("FINISHED".equals(matchStatus)) {
                int radiantWinCode = jsonObjectMatch.getInt(TAG_RADIANT_WIN);
                switch (radiantWinCode) {
                    case 0:
                        this.winnerTeam = "DIRE";
                        break;
                    case 1:
                        this.winnerTeam = "RADIANT";
                        break;
                    default:
                        this.winnerTeam = "NONE";
                        break;
                }
            } else {
                this.winnerTeam = "NONE";
            }
        } catch (JSONException e) {
            System.out.println("ERROR: can't set match winner from JSON for matchID " + this.getMatchId());
            e.printStackTrace();
        }
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

    public int getSeriesType() {
        return this.series;
    }

    public String getMatchStatus() {
        return this.matchStatus;
    }

    public String getWinner() {
        return this.winnerTeam;
    }

    public String getMatchId() {
        return this.matchId;
    }

    public Date getMatchStartDateTime() {
        return this.matchStartDateTime;
    }

    // The matches will compare by start date-time
    @Override
    public int compareTo(@NonNull Match compareMatch) {
        Date currentMatchDate = this.getMatchStartDateTime();
        Date compareMatchDate = compareMatch.getMatchStartDateTime();
        return compareMatchDate.compareTo(currentMatchDate);
    }
}
