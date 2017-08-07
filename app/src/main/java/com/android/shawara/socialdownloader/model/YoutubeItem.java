package com.android.shawara.socialdownloader.model;

/**
 * Created by shawara on 7/25/2017.
 */

public class YoutubeItem {
    public final static int DOWNLOADED = 0, DOWNLOADING = 1, IN_QUEUE = 2, STOPPED = 3, READY = 4;
    public static final String[] states = {"Downloaded", "Downloading...", "in Queue", "Stopped", "Ready"};

    private String mFilePath;
    private long totalSize;
    private long currentSize;
    private int state = 4;
    private long resumeSize;
    private int selectedQ;

    public int getSelectedQ() {
        return selectedQ;
    }

    public void setSelectedQ(int selectedQ) {
        this.selectedQ = selectedQ;
    }

    public long getResumeSize() {
        return resumeSize;
    }

    public void setResumeSize(long resumeSize) {
        this.resumeSize = resumeSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long curentSize) {
        this.currentSize = curentSize;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private String q720;
    private String q360;
    private String q240;
    private String q128;
    private String q64;
    private String thumbnail_url;
    private String title;

    public String getQ720() {
        return q720;
    }

    public void setQ720(String q720) {
        this.q720 = q720;
    }

    public String getQ360() {
        return q360;
    }

    public void setQ360(String q360) {
        this.q360 = q360;
    }

    public String getQ240() {
        return q240;
    }

    public void setQ240(String q240) {
        this.q240 = q240;
    }

    public String getQ128() {
        return q128;
    }

    public void setQ128(String q128) {
        this.q128 = q128;
    }

    public String getQ64() {
        return q64;
    }

    public void setQ64(String q64) {
        this.q64 = q64;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public int getProgress() {
        return (totalSize + resumeSize) > 0 ? (int) ((100 * (currentSize + resumeSize)) / (totalSize + resumeSize)) : 0;
    }
}
