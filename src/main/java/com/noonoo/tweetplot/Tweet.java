package com.noonoo.tweetplot;

import java.sql.Date;

public class Tweet implements java.io.Serializable {
   
    private static final long serialVersionUID = 1L;
    private long tweetId, userId;
    private String text;
    private Date createdAt;

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public long getTweetId() {
        return this.tweetId;
    }

    public long getUserId() {
        return this.userId;
    }

    public String getText() {
        return this.text;
    }

    public void setData(long tweetId, String text, Date createdAt, long userId) {
        this.tweetId = tweetId;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
    }
}
