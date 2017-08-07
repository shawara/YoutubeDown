package com.android.shawara.socialdownloader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.shawara.socialdownloader.API;
import com.android.shawara.socialdownloader.helpers.ProgressResponseBody;
import com.android.shawara.socialdownloader.R;
import com.android.shawara.socialdownloader.helpers.RadioGroupMerger;
import com.android.shawara.socialdownloader.helpers.SimpleItemTouchHelperCallback;
import com.android.shawara.socialdownloader.model.YoutubeItem;
import com.android.shawara.socialdownloader.adapter.DownloadAdapter;
import com.android.shawara.socialdownloader.services.UrlCopiedService;
import com.android.shawara.socialdownloader.utils.Animations;
import com.android.shawara.socialdownloader.utils.Constants;
import com.android.shawara.socialdownloader.utils.Scrapper;
import com.android.shawara.socialdownloader.utils.SharedPreferenceUtils;
import com.android.shawara.socialdownloader.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by shawara on 7/24/2017.
 */

public class YoutubeActivity extends AppCompatActivity {
    private static final String TAG = "YoutubeActivity";
    private List<YoutubeItem> mYoutubeItems = new ArrayList<>();

    private ImageView mYoutubeDownloadImageView;
    private EditText mYoutubeUrlEditText;
    private LinearLayout mQualityLinearLayout;
    private TextInputLayout mURLInputLayout;
    private RecyclerView mRecyclerView;
    private DownloadAdapter mAdapter;
    private RadioGroup mVideoRadioGroup, mAudioRadioGroup;
    private RadioButton mQ720RadioButton, mQ360RadioButton, mQ240RadioButton,
            mQ128RadioButton, mQ64RadioButton;
    private RadioGroupMerger mAudioVideoGroup;
    private API mYoutubeService;
    private Handler mHandler = new Handler();


    private OkHttpClient client;

    private Pattern youtubePat = Pattern.compile("((https?\\:\\/\\/)?(www\\.)?(youtube\\.com|youtu\\.?be)\\/[^\\s]+)");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        //startService(new Intent(this, UrlCopiedService.class));
        mYoutubeService = Utils.createRetrofitService(API.class, API.BASE_URL);

        initUI();

        isStoragePermissionGranted();
        initHttpClient();
    }

    private void initHttpClient() {
        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    YoutubeItem item = (YoutubeItem) chain.request().tag();
                    Log.d(TAG, "initHttpClient: " + item.getTitle());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(),
                                    new ProgressResponseBody.ProgressListener(YoutubeActivity.this, item)
                            ))
                            .build();
                })
                .build();


    }


    private void initUI() {
        mYoutubeDownloadImageView = (ImageView) findViewById(R.id.download_youtube_imageView);
        mYoutubeUrlEditText = (EditText) findViewById(R.id.url_youtube_editText);
        mQualityLinearLayout = (LinearLayout) findViewById(R.id.quality_radio_group);
        mURLInputLayout = (TextInputLayout) findViewById(R.id.url_input_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.download_youtube_recyclerView);

        mVideoRadioGroup = (RadioGroup) findViewById(R.id.video_radiogroup);
        mAudioRadioGroup = (RadioGroup) findViewById(R.id.audio_radiogroup);
        mQ720RadioButton = (RadioButton) findViewById(R.id.q720_radiobutton);
        mQ360RadioButton = (RadioButton) findViewById(R.id.q360_radiobutton);
        mQ240RadioButton = (RadioButton) findViewById(R.id.q240_radiobutton);
        mQ128RadioButton = (RadioButton) findViewById(R.id.q128_radiobutton);
        mQ64RadioButton = (RadioButton) findViewById(R.id.q64_radiobutton);


        mAudioVideoGroup = new RadioGroupMerger(mAudioRadioGroup, mVideoRadioGroup);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DownloadAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setList(mYoutubeItems);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        mAdapter.setItemDismissListener(item -> {
            onYoutubeItemsFitched();

        });

        mYoutubeDownloadImageView.setOnClickListener(view -> {
                    if (!isStoragePermissionGranted()) {
                        toast("you have to allow storage first");
                        return;
                    }

                    if (mQualityLinearLayout.getVisibility() == View.INVISIBLE) {
                        Animations.rotateImageAnimation(mYoutubeDownloadImageView);
                        String url = mYoutubeUrlEditText.getText().toString();
                        handlePastedUrl(url);
                    } else {
                        int id = mAudioVideoGroup.getCheckedRadioButtonId();
                        if (id == -1) {
                            toast("you have to select download quality first");
                        } else {
                            Log.d(TAG, "initUI: Current Quality" + id);

                            for (YoutubeItem item : mYoutubeItems)
                                item.setState(YoutubeItem.IN_QUEUE);
                            mAdapter.notifyDataSetChanged();

                            Observable.just(mYoutubeItems)
                                    .flatMap(youtubeItems -> Observable.fromIterable(mYoutubeItems))
                                    .flatMap(item ->
                                            {
                                                Pair<String, Integer> pair = getQUrl(item);
                                                item.setSelectedQ(pair.second);
                                                return downloadFile(pair.first, item);
                                            }
                                    )
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(file -> {
                                                Log.d(TAG, "initUI: " + file.getAbsolutePath());
                                            }, Throwable::printStackTrace,
                                            () -> Log.d(TAG, "initUI: Download Successfully"));
                        }
                    }
                }
        );

        String lastUrl = SharedPreferenceUtils.getYoutubeUrl(this);
        if (lastUrl != null) {
            mYoutubeUrlEditText.setText(lastUrl);
            mYoutubeDownloadImageView.callOnClick();
        }
    }


    private Pair<String, Integer> getQUrl(YoutubeItem item) {
        switch (mAudioVideoGroup.getCheckedRadioButtonId()) {
            case R.id.q720_radiobutton:
                return new Pair<>(item.getQ720(), 720);
            case R.id.q360_radiobutton:
                return new Pair<>(item.getQ360(), 360);
            case R.id.q240_radiobutton:
                return new Pair<>(item.getQ240(), 240);
            case R.id.q128_radiobutton:
                return new Pair<>(item.getQ128(), 128);
            case R.id.q64_radiobutton:
                return new Pair<>(item.getQ64(), 64);
            default:
                return new Pair<>("", 0);
        }
    }

    private void handlePastedUrl(String url) {
        Matcher mat = youtubePat.matcher(url);

        if (mat.find()) {
            SharedPreferenceUtils.saveYoutubeUrl(getBaseContext(), mat.group(0));

            String listId = Uri.parse(mat.group(0)).getQueryParameter("list");
            mYoutubeItems.clear();
            mAdapter.notifyDataSetChanged();

            Observable<YoutubeItem> observable;
            if (listId != null) {
                observable = fetchPlayListData(listId);
            } else {
                observable = fetchItemData(url);
            }
            handleYouTubeObservable(observable);

        } else {
            toast("enter valid url.");
            stopAnim();
        }

    }


    private Observable<YoutubeItem> fetchPlayListData(String listId) {
        return Observable.fromCallable(() ->
                Scrapper.getYouTubePlayListIds(listId))
                .flatMap(Observable::fromIterable)
                .flatMap(this::fetchItemData);
    }


    private Observable<YoutubeItem> fetchItemData(String url) {
        return mYoutubeService.getYoutubeItem(url, Constants.YOUTUBE_CODE);
    }

    private void handleYouTubeObservable(Observable<YoutubeItem> observable) {
        observable.filter(item -> item.getQ240() != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                            mYoutubeItems.add(item);
                            mAdapter.notifyItemInserted(mYoutubeItems.size() - 1);
                        }
                        , throwable -> {
                            throwable.printStackTrace();
                            stopAnim();
                        }, () -> {
                            stopAnim();
                            onYoutubeItemsFitched();
                        });
    }

    private void onYoutubeItemsFitched() {
        mQ720RadioButton.setVisibility(View.VISIBLE);
        mQ360RadioButton.setVisibility(View.VISIBLE);
        mQ240RadioButton.setVisibility(View.VISIBLE);
        mQ128RadioButton.setVisibility(View.VISIBLE);
        mQ64RadioButton.setVisibility(View.VISIBLE);
        if (mYoutubeItems.size() > 0) {
            showQualityView();
            for (YoutubeItem it : mYoutubeItems) {
                if (it.getQ720() == null) mQ720RadioButton.setVisibility(View.GONE);
                if (it.getQ360() == null) mQ360RadioButton.setVisibility(View.GONE);
                if (it.getQ240() == null) mQ240RadioButton.setVisibility(View.GONE);
                if (it.getQ128() == null) mQ128RadioButton.setVisibility(View.GONE);
                if (it.getQ64() == null) mQ64RadioButton.setVisibility(View.GONE);
            }


        } else hideQualityView();
    }


    private int getItemIdx(String tag) {
        for (int i = 0; i < mYoutubeItems.size(); i++) {
            if (mYoutubeItems.get(i).getTitle().equals(tag))
                return i;
        }
        return 0;
    }

    public void updateYoutubeItem(String tag, long bytesRead, long contentLength, boolean done) {

        mHandler.post(() -> {
            int itemIdx = getItemIdx(tag);
            Log.d(TAG, "updateYoutubeItem: " + tag + " " + itemIdx);
            mYoutubeItems.get(itemIdx).setCurrentSize(bytesRead);
            mYoutubeItems.get(itemIdx).setTotalSize(contentLength);
            mYoutubeItems.get(itemIdx).setState(done ? YoutubeItem.DOWNLOADED : YoutubeItem.DOWNLOADING);
            mAdapter.notifyItemChanged(itemIdx);
        });
    }

    private File getFileFromItem(YoutubeItem item) {
        String fileName = item.getTitle();
        int quality = item.getSelectedQ();
        String fullname = Utils.getValidFileName(fileName) + Utils.getTypeOfQ(quality);


        File downloadedFile = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                Utils.getValidFileName(fullname));

        item.setFilePath(downloadedFile.getAbsolutePath());
        return downloadedFile;
    }

    private Observable<File> downloadFile(String url, YoutubeItem item) {

        File downloadedFile = getFileFromItem(item);
        item.setResumeSize(downloadedFile.length());

        Log.d(TAG, "downloadFile: " + downloadedFile.length());

        Request request = createRequest(url, item, downloadedFile.length());
        return Observable.fromCallable(() ->
                client.newCall(request).execute()).flatMap(response -> {
            BufferedSink sink = Okio.buffer(Okio.appendingSink(downloadedFile));
            sink.writeAll(response.body().source());
            sink.close();
            return Observable.just(downloadedFile);
        });

    }


    private Request createRequest(String url, YoutubeItem item, long length) {
        return new Request.Builder()
                .url(url)
                .tag(item)
                .header("Range", "bytes=" + length + "-")
                .build();

    }


    private void stopAnim() {
        mYoutubeDownloadImageView.setAnimation(null);
    }


    private void showQualityView() {
        mQualityLinearLayout.setVisibility(View.VISIBLE);
        mURLInputLayout.setVisibility(View.INVISIBLE);
    }

    private void hideQualityView() {
        mQualityLinearLayout.setVisibility(View.INVISIBLE);
        mURLInputLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (mQualityLinearLayout.getVisibility() == View.VISIBLE) {
            hideQualityView();
        } else {
            moveTaskToBack(true);
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

        } else {
            toast("allow storage to store videos");
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}
