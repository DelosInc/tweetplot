package com.noonoo.tweetplot;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
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

        Dataset<Row> tweetsDataFrame = spark.createDataFrame(tweetRDD, Tweet.class);

        tweetsDataFrame.createOrReplaceTempView("tweetView");

        Dataset<Row> viewTweetsFrame = spark.sql("select * from tweetView");
        viewTweetsFrame.show();        
    }
};