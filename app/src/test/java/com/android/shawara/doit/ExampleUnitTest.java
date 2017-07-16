package com.android.shawara.doit;

import org.junit.Test;

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
        String downloadUrl = ImageScrapper.getDownloadUrl("https://www.instagram.com/p/BWUnSA-FfqX/");
        String downloadUrl2 = ImageScrapper.getDownloadUrl("fadsfdsa");

        assertFalse(downloadUrl.isEmpty());

        assertTrue(downloadUrl2.isEmpty());
    }
}