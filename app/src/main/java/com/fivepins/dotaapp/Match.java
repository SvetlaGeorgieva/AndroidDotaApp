package com.fivepins.dotaapp;

/**
 * Created by e30 on 11/28/2015.
 */
public class Match {
    public String radiantTeamName;
    public String direTeamName;
    public int radiantTeamKills;
    public int direTeamKills;

    public Match(String radiantTeamName, String direTeamName, int radiantTeamKills, int direTeamKills) {
        this.radiantTeamName = radiantTeamName;
        this.direTeamName = direTeamName;
        this.radiantTeamKills = radiantTeamKills;
        this.direTeamKills = direTeamKills;
    }

}
