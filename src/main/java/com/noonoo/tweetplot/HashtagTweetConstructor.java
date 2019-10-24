package com.noonoo.tweetplot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.spark.api.java.function.FlatMapFunction;

import twitter4j.Status;

public class HashtagTweetConstructor implements FlatMapFunction<Status, HashtagTweet> {
    
    private static final long serialVersionUID = 1L;

    @Override
    public Iterator<HashtagTweet> call(Status t) throws Exception {
       
        return Arrays.asList(t.getHashtagEntities()).stream().map(hashtag -> {
            HashtagTweet hashtagTweet =  new HashtagTweet();
            hashtagTweet.setData(t.getId(), hashtag.getText());
            return hashtagTweet;
        }).collect(Collectors.toList()).iterator();
    }    
}
