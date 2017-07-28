package com.android.shawara.socialdownloader.model;

import java.util.List;

/**
 * Created by shawara on 7/27/2017.
 */

public class YouTublePlayList {

    /**
     * kind : youtube#playlistItemListResponse
     * etag : "m2yskBQFythfE4irbTIeOgYYfBU/uEAvfZMV-QuKgV5yKOz2utykGYo"
     * nextPageToken : CAUQAA
     * pageInfo : {"totalResults":8,"resultsPerPage":5}
     * items : [{"kind":"youtube#playlistItem","etag":"\"m2yskBQFythfE4irbTIeOgYYfBU/dCfNSKf5AnPEI69_4g1kil-IbT0\"","id":"UExfaF92SzNjZXhSTUs0eXk3NkdZZ0ZQRUtsMHIxT2xyVC41NkI0NEY2RDEwNTU3Q0M2","contentDetails":{"videoId":"yVlSIxHpEDw","videoPublishedAt":"2017-06-27T17:58:27.000Z"}},{"kind":"youtube#playlistItem","etag":"\"m2yskBQFythfE4irbTIeOgYYfBU/83lV65AEyjAo6T5oUG7L8h2FCvI\"","id":"UExfaF92SzNjZXhSTUs0eXk3NkdZZ0ZQRUtsMHIxT2xyVC4yODlGNEE0NkRGMEEzMEQy","contentDetails":{"videoId":"h4jqi_s4gAQ","videoPublishedAt":"2017-06-28T21:27:20.000Z"}},{"kind":"youtube#playlistItem","etag":"\"m2yskBQFythfE4irbTIeOgYYfBU/kg8KvO6Yp7tc-Idp_BqKUmAA5CI\"","id":"UExfaF92SzNjZXhSTUs0eXk3NkdZZ0ZQRUtsMHIxT2xyVC4wMTcyMDhGQUE4NTIzM0Y5","contentDetails":{"videoId":"IPlzUKT7xOI","videoPublishedAt":"2017-06-29T22:20:22.000Z"}},{"kind":"youtube#playlistItem","etag":"\"m2yskBQFythfE4irbTIeOgYYfBU/knMG5B6IoAya1IeCU9n4W-1TTAs\"","id":"UExfaF92SzNjZXhSTUs0eXk3NkdZZ0ZQRUtsMHIxT2xyVC41MjE1MkI0OTQ2QzJGNzNG","contentDetails":{"videoId":"tsRnrzUYy7s","videoPublishedAt":"2017-06-30T21:07:50.000Z"}},{"kind":"youtube#playlistItem","etag":"\"m2yskBQFythfE4irbTIeOgYYfBU/6utTwxgZ9t7mtkNoVEo7UwDsRtk\"","id":"UExfaF92SzNjZXhSTUs0eXk3NkdZZ0ZQRUtsMHIxT2xyVC4wOTA3OTZBNzVEMTUzOTMy","contentDetails":{"videoId":"mIgvnqEz0jw","videoPublishedAt":"2017-07-01T21:21:52.000Z"}}]
     */

    private List<ItemsBean> items;
    public String nextPageToken;


    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }


    public static class ItemsBean {
        /**
         * kind : youtube#playlistItem
         * etag : "m2yskBQFythfE4irbTIeOgYYfBU/dCfNSKf5AnPEI69_4g1kil-IbT0"
         * id : UExfaF92SzNjZXhSTUs0eXk3NkdZZ0ZQRUtsMHIxT2xyVC41NkI0NEY2RDEwNTU3Q0M2
         * contentDetails : {"videoId":"yVlSIxHpEDw","videoPublishedAt":"2017-06-27T17:58:27.000Z"}
         */

        private ContentDetailsBean contentDetails;


        public ContentDetailsBean getContentDetails() {
            return contentDetails;
        }

        public void setContentDetails(ContentDetailsBean contentDetails) {
            this.contentDetails = contentDetails;
        }

        public static class ContentDetailsBean {
            /**
             * videoId : yVlSIxHpEDw
             */

            private String videoId;

            public String getVideoId() {
                return videoId;
            }

            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }


        }
    }
}
