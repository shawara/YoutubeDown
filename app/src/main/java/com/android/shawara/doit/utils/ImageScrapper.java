package com.android.shawara.doit.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by shawara on 7/15/2017.
 */

public class ImageScrapper {

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

}
