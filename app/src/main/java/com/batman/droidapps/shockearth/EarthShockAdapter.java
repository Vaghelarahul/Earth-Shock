package com.batman.droidapps.shockearth;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class EarthShockAdapter extends ArrayAdapter<ShockDataModel> {

    private static final String LOC_SEPERATOR = "of";
    private String locationOffset;
    private String primaryLocation;

    public EarthShockAdapter(Context context, List<ShockDataModel> earthquake) {
        super(context, 0, earthquake);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.shock_list_item, parent, false);
        }


        ShockDataModel currentWord = getItem(position);

        TextView magText = (TextView) listItemView.findViewById(R.id.magnitude);
        Double magnitude = currentWord.getMagnitude();
        String magFormatted = magFormatter(magnitude);

        magText.setText(magFormatted);

        GradientDrawable magnitudeCircle = (GradientDrawable) magText.getBackground();
        int magnitudeColor = getMagnitudeColor(magnitude);
        magnitudeCircle.setColor(magnitudeColor);

        String originalLocation = currentWord.getLocoation();

        if (originalLocation.contains(LOC_SEPERATOR)) {
            int index = originalLocation.indexOf(LOC_SEPERATOR);
            locationOffset = originalLocation.substring(0, index + 2);
            primaryLocation = originalLocation.substring(index + 3, originalLocation.length());
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }


        TextView locOffsetText = (TextView) listItemView.findViewById(R.id.offsetLocation);
        locOffsetText.setText(locationOffset);

        TextView primaryLocText = (TextView) listItemView.findViewById(R.id.primaryLocation);
        primaryLocText.setText(primaryLocation);


        TextView dateText = (TextView) listItemView.findViewById(R.id.date);
        Date dateObject = new Date(currentWord.getQuakeDate());
        String dateFormatted = dateFormatter(dateObject);
        dateText.setText(dateFormatted);

        TextView timeText = (TextView) listItemView.findViewById(R.id.time);
        Date timeObject = new Date(currentWord.getQuakeDate());
        String timeFormatted = timeFormatter(timeObject);
        timeText.setText(timeFormatted);

        return listItemView;
    }


    private String dateFormatter(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String date = dateFormat.format(dateObject);
        return date;

    }

    private String timeFormatter(Date timeObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        String time = timeFormat.format(timeObject);
        return time;

    }

    private String magFormatter(double magnitude) {
        DecimalFormat magFormat = new DecimalFormat("0.0");
        return magFormat.format(magnitude);
    }

    private int getMagnitudeColor(Double magnitude) {
        int magnitudeColorResourceId;
        int floorValue = (int) Math.floor(magnitude);

        switch (floorValue) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;

        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
