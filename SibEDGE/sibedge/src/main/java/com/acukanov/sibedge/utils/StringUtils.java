package com.acukanov.sibedge.utils;


import android.content.Context;
import android.support.annotation.StringRes;

public class StringUtils {
    public static String formatToDecimal(String text, int decimal) {
        return String.format(text, decimal);
    }

    public static String formatToDecimal(Context context, @StringRes int text, int decimal) {
        return formatToDecimal(context.getResources().getString(text), decimal);
    }

    public static String formatToString(String text, String string) {
        return String.format(text, string);
    }

    public static String formatToString(Context context, @StringRes int text, String string) {
        return formatToString(context.getResources().getString(text), string);
    }
}
