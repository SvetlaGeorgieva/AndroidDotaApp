package com.fivepins.matchtracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by e30 on 11/28/2015.
 * Custom Adapter to assign MatchList Data to MatchList View.
 */
public class MatchAdapter extends ArrayAdapter<Match> {
    private ArrayList<Match> matches;

    public MatchAdapter(Context context, ArrayList<Match> matches) {
        super(context, 0, matches);
        this.matches = matches;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Match match = getItem(position);
        Context context = getContext();

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.match_item, parent, false);
        }

        // Populate the data into the template view using the data objects
        populateTeamNames(convertView, match);
        populateTeamKills(convertView, match);
        populateLeagueName(convertView, match);
        populateMatchDuration(convertView, match);
        populateTeamLogos(convertView, match, context);
        populateHeroIcons(convertView, match, context);
        populateMap(convertView, match, context);

        populateGoldStats(convertView, match);
        populateXPStats(convertView, match);
        populateMatchStatus(convertView, match);

        // Return the completed view to render on screen
        return convertView;
    }

    private void populateLeagueName(View convertView, Match match) {
        TextView leagueName = (TextView) convertView.findViewById(R.id.leagueName);
        leagueName.setText(String.valueOf(match.leagueName));
    }

    private void populateMatchDuration(View convertView, Match match) {
        TextView matchDuration = (TextView) convertView.findViewById(R.id.matchDuration);
        matchDuration.setText(match.getDuration());
    }

    private void populateMatchStatus(View convertView, Match match) {
        TextView matchStatusText = (TextView) convertView.findViewById(R.id.leagueIsLive);
        String matchStatus = match.getMatchStatus();
        if ("LIVE".equals(matchStatus)) {
            String isLive = getContext().getResources().getString(R.string.is_live);
            matchStatusText.setText(isLive);
        } else {
            matchStatusText.setText("");
        }
    }

    //TODO gold stats
    private void populateGoldStats(View convertView, Match match) {
        TextView direGoldStat = (TextView) convertView.findViewById(R.id.dire_gold_stat);
        TextView radiantGoldStat = (TextView) convertView.findViewById(R.id.radiant_gold_stat);

        int direGold = match.getDireTeamScore().getNetWorth();
        int radiantGold = match.getRadiantTeamScore().getNetWorth();

        // show the difference between the two teams stats in a [{+ 30k for dire} to {+ 30k for radiant}] bar
        int gapGold = direGold - radiantGold;
        // the coefficient is: dire = (30k + gapGold)/60k; radiant = (30k - gapGold)/60k = 1 - dire;
        float direWeightGold = (30000 + gapGold) / (float) 60000;
        float radiantWeightGold = 1 - direWeightGold;

        if (direWeightGold < 0) {
            direWeightGold = 0;
        }
        if (radiantWeightGold < 0) {
            radiantWeightGold = 0;
        }

        direGoldStat.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, direWeightGold));
        radiantGoldStat.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, radiantWeightGold));

        if (Math.abs(gapGold) > 25000) {
            System.out.println("Gold: dire: " + direGold + "; radiant: " + radiantGold + "; GAP: " + gapGold);
        }
    }

    //TODO XP stats
    private void populateXPStats(View convertView, Match match) {
        TextView direXPStat = (TextView) convertView.findViewById(R.id.dire_xp_stat);
        TextView radiantXPStat = (TextView) convertView.findViewById(R.id.radiant_xp_stat);

        int direXP = match.getDireTeamScore().getExperience();
        int radiantXP = match.getRadiantTeamScore().getExperience();

        // show the difference between the two teams stats in a [{+ 30k for dire} to {+ 30k for radiant}] bar
        int gapXP = direXP - radiantXP;
        // the coefficient is: dire = (30k + gapXP)/60k; radiant = (30k - gapXP)/60k = 1 - dire;
        float direWeightXP = (30000 + gapXP) / (float) 60000;
        float radiantWeightXP = 1 - direWeightXP;

        if (direWeightXP < 0) {
            direWeightXP = 0;
        }
        if (radiantWeightXP < 0) {
            radiantWeightXP = 0;
        }

        direXPStat.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, direWeightXP));
        radiantXPStat.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, radiantWeightXP));

        if (Math.abs(gapXP) > 25000) {
            System.out.println("xp: dire: " + direXP + "; radiant: " + radiantXP + "; GAP: " + gapXP);
        }
    }

    private void populateTeamKills(View convertView, Match match) {
        TextView radiantTeamKills = (TextView) convertView.findViewById(R.id.radiantTeamKills);
        TextView direTeamKills = (TextView) convertView.findViewById(R.id.direTeamKills);
        radiantTeamKills.setText(String.valueOf(match.getRadiantTeamScore().getKills()));
        direTeamKills.setText(String.valueOf(match.getDireTeamScore().getKills()));
    }

    private void populateTeamNames(View convertView, Match match) {
        TextView radianTeamName = (TextView) convertView.findViewById(R.id.radiantTeamName);
        TextView direTeamName = (TextView) convertView.findViewById(R.id.direTeamName);
        radianTeamName.setText(match.radiantTeamName);
        direTeamName.setText(match.direTeamName);

        // color the winning team name in Gold
        String winner = match.getWinner();
        if ("RADIANT".equals(winner)) {
            radianTeamName.setTextColor(Color.YELLOW);
            direTeamName.setTextColor(Color.parseColor("#3B3B3B"));
        } else if ("DIRE".equals(winner)) {
            direTeamName.setTextColor(Color.YELLOW);
            radianTeamName.setTextColor(Color.parseColor("#3B3B3B"));
        }
    }

    private void populateTeamLogos(View convertView, Match match, Context context) {
        final String teamLogosURL = context.getString(R.string.team_logos_url);

        ImageView radiantTeamLogo = (ImageView) convertView.findViewById(R.id.radiantTeamLogo);
        ImageView direTeamLogo = (ImageView) convertView.findViewById(R.id.direTeamLogo);

        String radiantTeamId = match.radiantTeamId;
        String direTeamId = match.direTeamId;
        String radiantTeamLogoURL = teamLogosURL + radiantTeamId + ".png";
        String direTeamLogoURL = teamLogosURL + direTeamId + ".png";

        // Set Team logo image rounded borders in px.
        // logo_placeholder_round_30 corresponds to transform radius 40.
        final int radius = 40;
        final int margin = 0;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);

        Picasso.with(context)
                .load(radiantTeamLogoURL)
                .placeholder(R.drawable.logo_placeholder_round_30)
                .transform(transformation)
                .error(R.drawable.logo_placeholder_round_30)
                .into(radiantTeamLogo);
        Picasso.with(context)
                .load(direTeamLogoURL)
                .placeholder(R.drawable.logo_placeholder_round_30)
                .transform(transformation)
                .error(R.drawable.logo_placeholder_round_30)
                .into(direTeamLogo);
    }

    private void populateHeroIcons(View convertView, Match match, Context context) {
        ImageView radiantHero1 = (ImageView) convertView.findViewById(R.id.radiant_hero1);
        ImageView radiantHero2 = (ImageView) convertView.findViewById(R.id.radiant_hero2);
        ImageView radiantHero3 = (ImageView) convertView.findViewById(R.id.radiant_hero3);
        ImageView radiantHero4 = (ImageView) convertView.findViewById(R.id.radiant_hero4);
        ImageView radiantHero5 = (ImageView) convertView.findViewById(R.id.radiant_hero5);

        ImageView direHero1 = (ImageView) convertView.findViewById(R.id.dire_hero1);
        ImageView direHero2 = (ImageView) convertView.findViewById(R.id.dire_hero2);
        ImageView direHero3 = (ImageView) convertView.findViewById(R.id.dire_hero3);
        ImageView direHero4 = (ImageView) convertView.findViewById(R.id.dire_hero4);
        ImageView direHero5 = (ImageView) convertView.findViewById(R.id.dire_hero5);

        ArrayList<ImageView> radiantHeroesViews = new ArrayList<>();
        radiantHeroesViews.add(radiantHero1);
        radiantHeroesViews.add(radiantHero2);
        radiantHeroesViews.add(radiantHero3);
        radiantHeroesViews.add(radiantHero4);
        radiantHeroesViews.add(radiantHero5);

        ArrayList<ImageView> direHeroesViews = new ArrayList<>();
        direHeroesViews.add(direHero1);
        direHeroesViews.add(direHero2);
        direHeroesViews.add(direHero3);
        direHeroesViews.add(direHero4);
        direHeroesViews.add(direHero5);

        populateHeroesViews(context, radiantHeroesViews, match.radiantTeamPlayers);
        populateHeroesViews(context, direHeroesViews, match.direTeamPlayers);
    }

    private void populateHeroesViews(Context context, ArrayList<ImageView> heroesViews, ArrayList<Player> teamPlayers) {
        int playerCount = 0;

        for (ImageView heroView : heroesViews) {
            // Returns the filename from drawable/ of the heroIcon that the player has chosen.
            Player currentPlayer = teamPlayers.get(playerCount);
            String playerHeroIcon = currentPlayer.heroIconName;
            int id = getResourceByID("drawable", playerHeroIcon, context);
            heroView.setImageResource(id);

            playerCount++;
        }
    }

    private void populateMap(View convertView, Match match, Context context) {
        // Get the Image Views for the map parts
        ImageView radiantTowersTop = (ImageView) convertView.findViewById(R.id.radiant_towers_top);
        ImageView radiantTowersMid = (ImageView) convertView.findViewById(R.id.radiant_towers_mid);
        ImageView radiantTowersBot = (ImageView) convertView.findViewById(R.id.radiant_towers_bot);
        ImageView direTowersTop = (ImageView) convertView.findViewById(R.id.dire_towers_top);
        ImageView direTowersMid = (ImageView) convertView.findViewById(R.id.dire_towers_mid);
        ImageView direTowersBot = (ImageView) convertView.findViewById(R.id.dire_towers_bot);

        ImageView radiantBarracksTop = (ImageView) convertView.findViewById(R.id.radiant_barracks_top);
        ImageView radiantBarracksMid = (ImageView) convertView.findViewById(R.id.radiant_barracks_mid);
        ImageView radiantBarracksBot = (ImageView) convertView.findViewById(R.id.radiant_barracks_bot);
        ImageView direBarracksTop = (ImageView) convertView.findViewById(R.id.dire_barracks_top);
        ImageView direBarracksMid = (ImageView) convertView.findViewById(R.id.dire_barracks_mid);
        ImageView direBarracksBot = (ImageView) convertView.findViewById(R.id.dire_barracks_bot);

        ImageView radiantThrone = (ImageView) convertView.findViewById(R.id.radiant_throne);
        ImageView direThrone = (ImageView) convertView.findViewById(R.id.dire_throne);


        // Form the names of the local image resources to populate the Map Image Views
        TeamScore radiantTeamScore = match.getRadiantTeamScore();
        TeamScore direTeamScore = match.getDireTeamScore();

        String radiantTowersTopImageName = "map_radiant_towers_top_" + radiantTeamScore.getTowersTop();
        String radiantTowersMidImageName = "map_radiant_towers_mid_" + radiantTeamScore.getTowersMid();
        String radiantTowersBotImageName = "map_radiant_towers_bot_" + radiantTeamScore.getTowersBot();
        String direTowersTopImageName = "map_dire_towers_top_" + direTeamScore.getTowersTop();
        String direTowersMidImageName = "map_dire_towers_mid_" + direTeamScore.getTowersMid();
        String direTowersBotImageName = "map_dire_towers_bot_" + direTeamScore.getTowersBot();

        String radiantBarracksTopImageName = "map_radiant_barracks_top_" + radiantTeamScore.getBarracksTop();
        String radiantBarracksMidImageName = "map_radiant_barracks_mid_" + radiantTeamScore.getBarracksMid();
        String radiantBarracksBotImageName = "map_radiant_barracks_bot_" + radiantTeamScore.getBarracksBot();
        String direBarracksTopImageName = "map_dire_barracks_top_" + direTeamScore.getBarracksTop();
        String direBarracksMidImageName = "map_dire_barracks_mid_" + direTeamScore.getBarracksMid();
        String direBarracksBotImageName = "map_dire_barracks_bot_" + direTeamScore.getBarracksBot();

        String radiantThroneImageName = "map_radiant_throne_" + direTeamScore.getThrone();
        String direThroneImageName = "map_dire_throne_" + direTeamScore.getThrone();


        // Get the ID-s of the resources to set to the Views
        int radiantTowersTopImage = getResourceByID("drawable", radiantTowersTopImageName, context);
        int radiantTowersMidImage = getResourceByID("drawable", radiantTowersMidImageName, context);
        int radiantTowersBotImage = getResourceByID("drawable", radiantTowersBotImageName, context);
        int direTowersTopImage = getResourceByID("drawable", direTowersTopImageName, context);
        int direTowersMidImage = getResourceByID("drawable", direTowersMidImageName, context);
        int direTowersBotImage = getResourceByID("drawable", direTowersBotImageName, context);

        int radiantBarracksTopImage = getResourceByID("drawable", radiantBarracksTopImageName, context);
        int radiantBarracksMidImage = getResourceByID("drawable", radiantBarracksMidImageName, context);
        int radiantBarracksBotImage = getResourceByID("drawable", radiantBarracksBotImageName, context);
        int direBarracksTopImage = getResourceByID("drawable", direBarracksTopImageName, context);
        int direBarracksMidImage = getResourceByID("drawable", direBarracksMidImageName, context);
        int direBarracksBotImage = getResourceByID("drawable", direBarracksBotImageName, context);

        int radiantThroneImage = getResourceByID("drawable", radiantThroneImageName, context);
        int direThroneImage = getResourceByID("drawable", direThroneImageName, context);


        // Set resources to Views
        radiantTowersTop.setImageResource(radiantTowersTopImage);
        radiantTowersMid.setImageResource(radiantTowersMidImage);
        radiantTowersBot.setImageResource(radiantTowersBotImage);
        direTowersTop.setImageResource(direTowersTopImage);
        direTowersMid.setImageResource(direTowersMidImage);
        direTowersBot.setImageResource(direTowersBotImage);

        radiantBarracksTop.setImageResource(radiantBarracksTopImage);
        radiantBarracksMid.setImageResource(radiantBarracksMidImage);
        radiantBarracksBot.setImageResource(radiantBarracksBotImage);
        direBarracksTop.setImageResource(direBarracksTopImage);
        direBarracksMid.setImageResource(direBarracksMidImage);
        direBarracksBot.setImageResource(direBarracksBotImage);

        radiantThrone.setImageResource(radiantThroneImage);
        direThrone.setImageResource(direThroneImage);
    }

    private int getResourceByID(String resourceType, String resourceName, Context context) {
        return context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());
    }

    public void upDateEntries(ArrayList<Match> arrayOfMatches) {
        this.clear();
        this.matches.clear();
        this.matches.addAll(arrayOfMatches);
        this.notifyDataSetChanged();
        System.out.println("Update Matches ListView initiated.");
    }
}
