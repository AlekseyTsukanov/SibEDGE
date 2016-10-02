package com.acukanov.sibedge.data.preferences;


import android.content.Context;
import android.content.SharedPreferences;

import com.acukanov.sibedge.injection.annotations.ApplicationContext;

import javax.inject.Inject;

public class LocalePreferenceManager {
    public static final String PREFERENCE_LOCALE_NAME = "preference_locale_name";
    private static final String PREFERENCE_LOCALE = "preference_locale";
    private SharedPreferences mPreference;

    @Inject
    public LocalePreferenceManager(@ApplicationContext Context context) {
        mPreference = context.getSharedPreferences(PREFERENCE_LOCALE_NAME, Context.MODE_PRIVATE);
    }

    public void setLocale(String locale) {
        mPreference.edit().putString(PREFERENCE_LOCALE, locale).apply();
    }

    public String getLocale() {
        return mPreference.getString(PREFERENCE_LOCALE, null);
    }

}
