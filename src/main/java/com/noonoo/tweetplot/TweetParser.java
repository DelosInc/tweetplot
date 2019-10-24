package com.noonoo.tweetplot;

import java.util.Iterator;
import java.util.Arrays;

import org.apache.spark.api.java.function.FlatMapFunction;

import twitter4j.Status;

public class TweetParser implements FlatMapFunction<Status, Status> {

    private static final long serialVersionUID = 1L;
    
    @Override
    public Iterator<Status> call(Status s) throws Exception {
        return Arrays.asList(s).iterator();
    }
};
