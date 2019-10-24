package com.noonoo.tweetplot;

public class Url implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private long tweetId;
    private String expandedUrl;

    public long getTweetId() {
        return this.tweetId;
    }

    public String getExpandedUrl() {
        return this.expandedUrl;
    }

    public void setData(long tweetId, String expandedUrl) {
        this.tweetId = tweetId;
        this.expandedUrl = expandedUrl;
    }
}