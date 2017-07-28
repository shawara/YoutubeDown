package com.android.shawara.socialdownloader.helpers;

import android.util.Log;

import com.android.shawara.socialdownloader.model.YoutubeItem;
import com.android.shawara.socialdownloader.ui.activity.YoutubeActivity;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by shawara on 7/26/2017.
 */

public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final ProgressListener progressListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }


    public static class ProgressListener {
        private String mTag;
        private YoutubeActivity mActivity;

        public ProgressListener(YoutubeActivity activity, String tag) {
            mActivity = activity;
            mTag = tag;
        }

        void update(long bytesRead, long contentLength, boolean done) {
            Log.d("hhhh", "update: " + mTag + " " + bytesRead);
            mActivity.updateYoutubeItem(mTag, bytesRead, contentLength, done);
        }
    }
}
