package com.android.shawara.socialdownloader;

import android.net.Uri;

import com.android.shawara.socialdownloader.model.YoutubeItem;
import com.android.shawara.socialdownloader.utils.Constants;
import com.android.shawara.socialdownloader.utils.Scrapper;
import com.android.shawara.socialdownloader.utils.Utils;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test

    public void scrappDownloadUrl() throws Exception {
        String downloadUrl = Scrapper.getDownloadUrl("https://www.instagram.com/p/BWUnSA-FfqX/");
        String downloadUrl2 = Scrapper.getDownloadUrl("fadsfdsa");

        assertFalse(downloadUrl.isEmpty());

        assertTrue(downloadUrl2.isEmpty());
    }

    @Test

    public void testRxJava() throws Exception {
//        Observable.just("Hello, world!")
//                .map(s -> s + " -Dan")
//                .subscribe(s -> System.out.println(s));

        API service = Utils.createRetrofitService(API.class, API.BASE_URL);

        service.getYoutubeItem("https://www.youtube.com/watch?v=3F57Yq-9ZLk", Constants.YOUTUBE_CODE)
                .subscribe(youtubeItem -> System.out.println(youtubeItem.getQ128()),
                        throwable -> System.out.print(throwable));
    }


    @Test
    public void valdFileName() throws Exception {
        System.out.println(Utils.getValidFileName("[fd#@%$#^$%&^%&#%sa[f/\\:"));
    }

    @Test
    public void testYoutubeList() throws IOException {
        String url1 = "dasdsa https://www.youtube.com/playlist?list=PL_h_vK3cexRMK4yy76GYgFPEKl0r1OlrT fdsaggfad",
                url2 = "https://www.youtube.com/watch?v=FxgBeDOE4ng",
                url3 = "https://www.youtu.be/FxgBeDOE4ng";
        Pattern pat = Pattern.compile("((https?\\:\\/\\/)?(www\\.)?(youtube\\.com|youtu\\.?be)\\/[^\\s]+)");
        System.out.println(pat.matcher(url1).matches());
        System.out.println(pat.matcher(url2).matches());
        System.out.println(pat.matcher(url3).matches());

        Matcher m=pat.matcher(url1);

        System.out.println(m.find());
        System.out.println(m.group(0));

    }

}


