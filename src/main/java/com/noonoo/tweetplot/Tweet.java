package com.noonoo.tweetplot;

public class Tweet implements java.io.Serializable {
    private static final long SerialVersionUID = 1L;
    private long id;
    private String text;

    public String getWord() {
        return text;
    }

    public long getId() {
        return id;
    }

    public void setData(long id, String text) {
        this.id = id;
        this.text = text;
    }
}
