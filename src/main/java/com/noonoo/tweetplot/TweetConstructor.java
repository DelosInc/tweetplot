package com.noonoo.tweetplot;

import org.apache.spark.api.java.function.Function;

import twitter4j.Status;

public class TweetConstructor implements Function<Status, Tweet> {

    private static final long serialVersionUID = 1L;

    @Override
    public Tweet call(Status v1) throws Exception {
        Tweet tweet = new Tweet();
        tweet.setData(v1.getId(), v1.getText());
        return tweet;
    }
};