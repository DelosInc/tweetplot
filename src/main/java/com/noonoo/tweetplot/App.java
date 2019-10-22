package com.noonoo.tweetplot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

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
import twitter4j.User;

public final class App {

    public static void main(String[] args) throws Exception {

        if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
            Logger.getRootLogger().setLevel(Level.WARN);
        }

        System.setProperty("twitter4j.oauth.consumerKey", "");      //TODO: Create Auth object
        System.setProperty("twitter4j.oauth.consumerSecret", "");
        System.setProperty("twitter4j.oauth.accessToken", "");
        System.setProperty("twitter4j.oauth.accessTokenSecret", "");

        SparkConf sparkConf = new SparkConf().setAppName("tweetplot");

        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));
        String[] filters = {"Machine Learning"};
        JavaReceiverInputDStream<Status> stream = TwitterUtils.createStream(jssc, filters);

        JavaDStream<Status> tweets = stream.flatMap(new FlatMapFunction<Status, Status>() {
            private static final long serialVersionUID = 1L;
            
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
                tweet.setData(status.getId(), status.getText(), status.getFavoriteCount(), new java.sql.Date(status.getCreatedAt().getTime()), status.getUser().getId());
                return tweet;
            });

            JavaRDD<Profile> profileRDD = rdd.map(status -> {
                Profile profile = new Profile();
                User user = status.getUser();
                profile.setData(user.getId(), new java.sql.Date(user.getCreatedAt().getTime()), user.getName(), user.getDescription(), user.getLocation(), user.getFavouritesCount(), user.getFollowersCount(), user.getFriendsCount());
                return profile;
            });

            JavaRDD<Hashtag> hashtagRDD = rdd.flatMap(new FlatMapFunction<Status, Hashtag>() {
                private static final long serialVersionUID = 1L;
                
                @Override
                public Iterator<Hashtag> call(Status status) {
                    return Arrays.asList(status.getHashtagEntities()).stream().map(hashtagEntity -> {
                        Hashtag hashtag = new Hashtag();
                        hashtag.setData(hashtagEntity.getText());
                        return hashtag;
                    }).collect(Collectors.toList()).iterator();
                }
            });

            JavaRDD<HashtagTweet> hashtagTweetRDD = rdd.flatMap(new FlatMapFunction<Status, HashtagTweet>() {
                private static final long serialVersionUID = 1L;
                
                @Override
                public Iterator<HashtagTweet> call(Status status) {
                    return Arrays.asList(status.getHashtagEntities()).stream().map(hashtag -> {
                        HashtagTweet hashtagTweet =  new HashtagTweet();
                        hashtagTweet.setData(status.getId(), hashtag.getText());
                        return hashtagTweet;
                    }).collect(Collectors.toList()).iterator();
                }
            });

            JavaRDD<Url> urlRDD = rdd.flatMap(new FlatMapFunction<Status, Url>() {
                private static final long serialVersionUID = 1L;
                
                @Override
                public Iterator<Url> call(Status status) {
                    return Arrays.asList(status.getURLEntities()).stream().map(urlEntity -> {
                        Url url = new Url();
                        url.setData(status.getId(), urlEntity.getExpandedURL());
                        return url;
                    }).collect(Collectors.toList()).iterator();
                }
            });

            Dataset<Row> tweetsDataFrame = spark.createDataFrame(tweetRDD, Tweet.class);
            Dataset<Row> profilesDataFrame = spark.createDataFrame(profileRDD, Profile.class);
            Dataset<Row> hashtagsDataFrame = spark.createDataFrame(hashtagRDD, Hashtag.class);
            Dataset<Row> hashtagTweetsDataFrame = spark.createDataFrame(hashtagTweetRDD, HashtagTweet.class);
            Dataset<Row> urlsDataFrame = spark.createDataFrame(urlRDD, Url.class);
            tweetsDataFrame.show();
            profilesDataFrame.show();
            hashtagsDataFrame.show();
            hashtagTweetsDataFrame.show();
            urlsDataFrame.show();
        });
        jssc.start();
        jssc.awaitTermination();
    }
};