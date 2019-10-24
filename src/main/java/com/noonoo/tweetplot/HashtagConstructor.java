package com.noonoo.tweetplot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.spark.api.java.function.FlatMapFunction;

import twitter4j.Status;

public class HashtagConstructor implements FlatMapFunction<Status, Hashtag> {
    
    private static final long serialVersionUID = 1L;

    @Override
    public Iterator<Hashtag> call(Status t) throws Exception {
        
        return Arrays.asList(t.getHashtagEntities()).stream().map(hashtagEntity -> {
            Hashtag hashtag = new Hashtag();
            hashtag.setData(hashtagEntity.getText());
            return hashtag;
        }).collect(Collectors.toList()).iterator();
    }
}
