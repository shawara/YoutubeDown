package com.android.shawara.socialdownloader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.shawara.socialdownloader.services.CopyInstaUrl;
import com.android.shawara.socialdownloader.utils.Animations;
import com.android.shawara.socialdownloader.utils.Scrapper;
import com.android.shawara.socialdownloader.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private ImageView mDownloadInstaImageView;
    private EditText mUrlInstaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, CopyInstaUrl.class));

        mDownloadInstaImageView = (ImageView) findViewById(R.id.download_insta_imageView);
        mUrlInstaEditText = (EditText) findViewById(R.id.url_insta_editText);
        isStoragePermissionGranted();
        mDownloadInstaImageView.setOnClickListener(view -> {
            Animations.rotateImageAnimation(mDownloadInstaImageView);
            mDownloadInstaImageView.setEnabled(false);
            new InstaDownloader().execute(mUrlInstaEditText.getText().toString());
        });

    }


    private class InstaDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String dUrl = "";
            try {
                dUrl = Scrapper.getDownloadUrl(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return dUrl;
        }

        @Override
        protected void onPostExecute(String downloadUrl) {
            mDownloadInstaImageView.setAnimation(null);
            mDownloadInstaImageView.setEnabled(true);

            if (downloadUrl.isEmpty()) {
                Toast.makeText(MainActivity.this, "Not valid URL.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isStoragePermissionGranted()) {
                Scrapper.DownloadImage(getBaseContext(), downloadUrl);
            }
        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission

        }
    }

}
