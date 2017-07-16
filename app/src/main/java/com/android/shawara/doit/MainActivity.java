package com.android.shawara.doit;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private ImageView mDownloadInstaImageView;
    private EditText mUrlInstaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDownloadInstaImageView = (ImageView) findViewById(R.id.download_insta_imageView);
        mUrlInstaEditText = (EditText) findViewById(R.id.url_insta_editText);
        isStoragePermissionGranted();
        mDownloadInstaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateImageAnimation(mDownloadInstaImageView);
                mDownloadInstaImageView.setEnabled(false);
                new InstaDownloader().execute(mUrlInstaEditText.getText().toString());
            }
        });

    }


    private class InstaDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String dUrl = "";
            try {
                dUrl = ImageScrapper.getDownloadUrl(params[0]);
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

                DownloadUrl(downloadUrl);
            }
        }
    }

    private void DownloadUrl(String downloadUrl) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setDescription("Social Downloader").setTitle("Instagram image")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        "IMG_" + System.currentTimeMillis() / 1000 + ".jpg");

        downloadManager.enqueue(request);
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

    private void rotateImageAnimation(ImageView imageView) {

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        //Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(1000); //Put desired duration per anim cycle here, in milliseconds

        //Start animation
        imageView.startAnimation(anim);
    }
}
