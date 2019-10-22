package com.noonoo.tweetplot;

import java.math.BigInteger;

public class HashtagTweet implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private long tweetId, hashId;

    public long getTweetId() {
        return this.tweetId;
    }

    public long getHashId() {
        return this.hashId;
    }

    public void setData(long tweetId, String hashtag) {
        this.hashId = new BigInteger(1, hashtag.getBytes()).longValue();
        this.tweetId = tweetId;
    }
}