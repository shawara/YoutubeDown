package com.android.shawara.socialdownloader.utils;

import com.android.shawara.socialdownloader.BuildConfig;

/**
 * Created by shawara on 7/24/2017.
 */


public final class Constants {
    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_LOCATION_YOUTUBE_URLS = "youtube_urls";
    public static final String FIREBASE_LOCATION_MESSAGES = "messages";


    /**
     * Constants for Firebase Database URL
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_DATABASE_ROOT_URL;

    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_URL_MESSAGES = FIREBASE_URL + "/" + FIREBASE_LOCATION_MESSAGES;
    public static final String FIREBASE_URL_YOUTUBE_URLS = FIREBASE_URL + "/" + FIREBASE_LOCATION_YOUTUBE_URLS;


    /**
     * Constants for Firebase Storage URL
     */
    public static final String FIREBASE_STORAGE_URL = BuildConfig.UNIQUE_FIREBASE_STORAGE_ROOT_URL;
    public static final String FIREBASE_STORAGE_URL_USERS = FIREBASE_STORAGE_URL + "/" + FIREBASE_LOCATION_USERS;


    /**
     * Constants related to locations in Firebase, such as the name of the node
     * where active lists are stored (ie "activeLists")
     */
    public static final String PASSWORD_PROVIDER = "password";


    /**
     * Constants for Firebase object properties
     */
    public static boolean isDestroyed = true;


    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";
    public static final String FIREBASE_PROPERTY_COUNT = "count";
    public static final String FIREBASE_PROPERTY_ID = "id";
    public static final String FIREBASE_PROPERTY_URL = "request_url";
    public static final String KEY_GOOGLE_EMAIL = "GOOGLE_EMAIL";
    public static final String GOOGLE_PROVIDER = "google";


    /**
     * Constants for bundles, extras and shared preferences keys
     */


    /**
     * API CODES
     */
    public final static String YOUTUBE_CODE = "vdfDSA23@F!@FSadsa";
}