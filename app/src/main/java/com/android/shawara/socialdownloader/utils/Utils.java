package com.android.shawara.socialdownloader.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.android.shawara.socialdownloader.API;
import com.android.shawara.socialdownloader.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;

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

    public static void showFile(Context mContext, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType = getMimeType(file.getAbsolutePath());

        if (android.os.Build.VERSION.SDK_INT >= 24) {

            Uri fileURI = FileProvider.getUriForFile(mContext,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    file);
            intent.setDataAndType(fileURI, mimeType);

        } else {
            intent.setDataAndType(Uri.fromFile(file), mimeType);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No Application found to open this type of file.", Toast.LENGTH_LONG).show();

        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = url.substring(url.lastIndexOf(".") + 1);
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return type;
    }
}
