package com.noonoo.tweetplot;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;

import twitter4j.Status;

public final class App {

    public static void main(String[] args) throws Exception {

        if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
            
            Logger.getRootLogger().setLevel(Level.WARN);
        }

        System.setProperty("twitter4j.oauth.consumerKey", "");      //TODO: Create Auth object
        System.setProperty("twitter4j.oauth.consumerSecret", "");
        System.setProperty("twitter4j.oauth.accessToken", "-");
        System.setProperty("twitter4j.oauth.accessTokenSecret", "");

        SparkConf sparkConf = new SparkConf().setAppName("tweetplot");

        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));
        String[] filters = {"Cat"};
        JavaReceiverInputDStream<Status> stream = TwitterUtils.createStream(jssc, filters);

        JavaDStream<Status> tweets = stream.flatMap(new TweetParser());     //TweetParser returns Iterator<Status>

        tweets.print();

        tweets.foreachRDD(new Dumper(sparkConf));
        tweets.print();

        jssc.start();
        jssc.awaitTermination();
    }
}
