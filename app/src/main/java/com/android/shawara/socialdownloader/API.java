package com.android.shawara.socialdownloader;

import com.android.shawara.socialdownloader.model.YouTublePlayList;
import com.android.shawara.socialdownloader.model.YoutubeItem;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by shawara on 7/25/2017.
 */

public interface API {
    String BASE_URL = "http://socialdownload.herokuapp.com/api/";
    //String YOUTUBE_API = "https://www.googleapis.com/youtube/v3/";

    @FormUrlEncoded
    @POST("ytdl")
    Observable<YoutubeItem> getYoutubeItem(@Field("url") String url, @Field("code") String code);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFileByUrl(@Url String fileUrl);

//    @GET("playlistItems?part=contentDetails&key=AIzaSyBuMlBrQIdlJE-FA7SpzZ5UBedDHadrhz4")
//    Observable<YouTublePlayList> getYoutubePlaylist(@Query("playlistId") String playListId, @Query("pageToken") String pageToken);
}

