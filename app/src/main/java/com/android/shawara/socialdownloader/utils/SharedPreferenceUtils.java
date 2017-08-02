package com.android.shawara.socialdownloader.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by shawara on 8/2/2017.
 */

public final class SharedPreferenceUtils {
    private final static String PREF_YOUTUBE_URL = "last_youtube_url";

    public static void saveYoutubeUrl(Context context, String url) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_YOUTUBE_URL, url)
                .apply();
    }

    public static String getYoutubeUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_YOUTUBE_URL, null);
    }


}
