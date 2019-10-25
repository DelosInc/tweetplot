package com.noonoo.tweetplot;

import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import twitter4j.Status;

public class Dumper implements VoidFunction<JavaRDD<Status>> {

    private static final long serialVersionUID = 1L;
    private SparkConf sparkConf;

    public Dumper(SparkConf sparkConf) { 
        this.sparkConf = sparkConf;
    }

    @Override
    public void call(JavaRDD<Status> t) throws Exception {
        
        SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();

        JavaRDD<Tweet> tweetRDD = t.map(new TweetConstructor());
        JavaRDD<Profile> profileRDD = t.map(new ProfileConstructor());
        JavaRDD<Hashtag> hashtagRDD = t.flatMap(new HashtagConstructor());
        JavaRDD<HashtagTweet> hashtagTweetRDD = t.flatMap(new HashtagTweetConstructor());
        JavaRDD<Url> urlRDD = t.flatMap(new UrlConstructor());

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

        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "rootpassword");
        Class.forName("org.mariadb.jdbc.Driver");

        tweetsDataFrame.write().mode(SaveMode.Append).jdbc("jdbc:mysql://localhost/tweetsink", "tweetsink.Tweets", properties);
        profilesDataFrame.write().mode(SaveMode.Append).jdbc("jdbc:mysql://localhost/tweetsink", "tweetsink.Profiles", properties);
        hashtagsDataFrame.write().mode(SaveMode.Append).jdbc("jdbc:mysql://localhost/tweetsink", "tweetsink.Hastags", properties);
        hashtagTweetsDataFrame.write().mode(SaveMode.Append).jdbc("jdbc:mysql://localhost/tweetsink", "tweetsink.HashtagsLookup", properties);
        urlsDataFrame.write().mode(SaveMode.Append).jdbc("jdbc:mysql://localhost/tweetsink", "tweetsink.Urls", properties);

    }
}
