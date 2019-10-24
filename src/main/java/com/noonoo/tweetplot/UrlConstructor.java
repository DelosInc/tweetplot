package com.noonoo.tweetplot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.spark.api.java.function.FlatMapFunction;

import twitter4j.Status;

public class UrlConstructor implements FlatMapFunction<Status, Url> {
    
    private static final long serialVersionUID = 1L;

    @Override
    public Iterator<Url> call(Status t) throws Exception {
        
        return Arrays.asList(t.getURLEntities()).stream().map(urlEntity -> {
            Url url = new Url();
            url.setData(t.getId(), urlEntity.getExpandedURL());
            return url;
        }).collect(Collectors.toList()).iterator();
    } 
}
