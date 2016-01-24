package com.fivepins.dotaapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        // Lookup view for data population
        TextView radianTeamName = (TextView) convertView.findViewById(R.id.radiantTeamName);
        TextView direTeamName = (TextView) convertView.findViewById(R.id.direTeamName);
        TextView radiantTeamKills = (TextView) convertView.findViewById(R.id.radiantTeamKills);
        TextView direTeamKills = (TextView) convertView.findViewById(R.id.direTeamKills);
        TextView leagueName = (TextView) convertView.findViewById(R.id.leagueName);

        // Populate the data into the template view using the data object
        radianTeamName.setText(match.radiantTeamName);
        direTeamName.setText(match.direTeamName);
        radiantTeamKills.setText(String.valueOf(match.radiantTeamKills));
        direTeamKills.setText(String.valueOf(match.direTeamKills));
        leagueName.setText(String.valueOf(match.leagueName));

        // Populate Team Logos
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

        // Return the completed view to render on screen
        return convertView;
    }

    public void upDateEntries(ArrayList<Match> arrayOfMatches) {
        this.clear();
        this.matches.clear();
        this.matches.addAll(arrayOfMatches);
        this.notifyDataSetChanged();
        System.out.println("Update Matches ListView initiated.");
    }
}
