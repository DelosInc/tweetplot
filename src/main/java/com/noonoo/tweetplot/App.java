package com.noonoo.tweetplot;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
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
        System.setProperty("twitter4j.oauth.accessToken", "");
        System.setProperty("twitter4j.oauth.accessTokenSecret", "");

        SparkConf sparkConf = new SparkConf().setAppName("tweeetplot");

        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));
        String[] filters = {"Machine Learning"};
        JavaReceiverInputDStream<Status> stream = TwitterUtils.createStream(jssc, filters);

        JavaDStream<Status> tweets = stream.flatMap(new FlatMapFunction<Status, Status>() {
            @Override
            public Iterator<Status> call(Status s) {
                return Arrays.asList(s).iterator();
            }
        });
        tweets.print();

        tweets.foreachRDD((rdd) -> {
            SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();

            JavaRDD<Tweet> tweetRDD = rdd.map(status -> {
                Tweet tweet = new Tweet();
                tweet.setData(status.getId(), status.getText());
                return tweet;
            });
            Dataset<Row> tweetsDataFrame = spark.createDataFrame(tweetRDD, Tweet.class);

            tweetsDataFrame.createOrReplaceTempView("tweetView");

            Dataset<Row> viewTweetsFrame = spark.sql("select * from tweetView");
            viewTweetsFrame.show();
        });
        jssc.start();
        jssc.awaitTermination();
    }

};