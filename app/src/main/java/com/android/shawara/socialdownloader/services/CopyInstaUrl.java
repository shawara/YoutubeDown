package com.android.shawara.socialdownloader.services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.shawara.socialdownloader.utils.Scrapper;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by shawara on 7/17/2017.
 */

public class CopyInstaUrl extends Service {
    private final static String TAG = "CopyInstaUrl";
    private Pattern instaUrlPat = Pattern.compile("^https://www\\.instagram\\.com/p/.*$");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        final ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {

            @Override
            public void onPrimaryClipChanged() {

                ClipData clipData = clipBoard.getPrimaryClip();
                ClipData.Item item = clipData.getItemAt(0);
                final String text = item.getText().toString();
                boolean isInstaImage = instaUrlPat.matcher(text).matches();
                Log.d(TAG, "onPrimaryClipChanged: " + text + " " + isInstaImage);
                if (isInstaImage) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String url = Scrapper.getDownloadUrl(text);
                                if (isPermissionGranted() && !url.isEmpty()) {
                                    Scrapper.DownloadImage(getBaseContext(), url);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
        });


    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return START_STICKY;
    }


}
