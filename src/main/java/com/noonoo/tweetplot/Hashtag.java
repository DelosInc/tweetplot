package com.noonoo.tweetplot;

import java.math.BigInteger;

public class Hashtag implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private long hashId;
    private String hashtag;

    public long getHashId() {
        return this.hashId;
    }

    public String getHashtag() {
        return this.hashtag;
    }

    public void setData(String hashtag) {
        this.hashId = new BigInteger(1, hashtag.getBytes()).intValue();
        this.hashtag = hashtag;
    }
}