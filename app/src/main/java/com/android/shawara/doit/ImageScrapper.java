package com.android.shawara.doit;

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

}
