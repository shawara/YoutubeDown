package com.android.shawara.socialdownloader.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by shawara on 7/15/2017.
 */

public class Scrapper {
    private final static String TAG = "Scrapper";

    public static String getDownloadUrl(String url) throws IOException {
        Document doc = Jsoup.connect("http://www.dinsta.com/photos/")
                .data("url", url)
                .userAgent("Mozilla")
                .post();

        //dev#goaway -> div.img -> img
        Element img = doc.getElementById("goaway").getElementsByClass("img").get(0).child(0);
        String dUrl = img.attr("src");

        return dUrl;
    }

    public static void DownloadImage(Context context, String downloadUrl) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setDescription("Social Downloader").setTitle("Instagram image")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        "IMG_" + System.currentTimeMillis() / 1000 + ".jpg");

        downloadManager.enqueue(request);
    }


    public static List<String> getYouTubePlayListIds(String playListId) throws IOException {
        List<String> urls = new ArrayList<>();

        Document doc = Jsoup.connect("https://www.youtube.com/playlist?list=" + playListId).userAgent("Mozilla").get();
        List<Element> elements = doc.getElementsByClass("pl-video");
        for (Element ele : elements) {
            urls.add(ele.attr("data-video-id"));
        }
        Log.d(TAG, "getYouTubePlayListIds: "+urls.size());
        return urls;
    }


}
