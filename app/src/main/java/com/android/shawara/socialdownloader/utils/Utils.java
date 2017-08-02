package com.android.shawara.socialdownloader.utils;

import com.android.shawara.socialdownloader.API;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shawara on 7/24/2017.
 */

public final class Utils {
    private static final String ReservedChars = "[|\\\\?*<\":>+\\[\\]/']";

    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        T service = retrofit.create(clazz);

        return service;
    }

    public static String getValidFileName(String orginal) {
        return orginal.replaceAll(ReservedChars, "");
    }

    public static String getTypeOfQ(int Q) {
        if (Q == 720) {
            return ".720.mp4";
        } else if (Q == 360) {
            return ".360.mp4";
        } else if (Q == 240) {
            return ".240.3gp";
        } else if (Q == 128) {
            return ".128.m4a";
        } else if (Q == 64) {
            return ".64.m4a";
        } else return "";
    }
}
