package com.devpaul.datalogger.utils;

import android.content.res.Resources;

import com.devpaul.datalogger.R;

/**
 * Created by Pauly D on 3/19/2015.
 *
 * Simple class to handle formatted strings.
 */
public class StringFormatter {

    public static String getFormatedHeightFromInches(Resources res, int inches) {
        int inch = inches%12;
        int feet = (inches - inch)/12;

        return String.format(res.getString(R.string.height_format_string), feet, inch);
    }

    public static String getFormattedWeight(Resources res, int weight) {
        return String.format(res.getString(R.string.weight_format_string), weight);
    }
}
