package com.noonoo.tweetplot;

import org.apache.spark.api.java.function.Function;

import twitter4j.Status;
import twitter4j.User;

public class ProfileConstructor implements Function<Status, Profile> {
    
    private static final long serialVersionUID = 1L;

    @Override
    public Profile call(Status v1) throws Exception {
        
        Profile profile = new Profile();
        User user = v1.getUser();
        profile.setData(user.getId(), new java.sql.Date(user.getCreatedAt().getTime()), user.getName(), user.getDescription(), user.getLocation(), user.getFavouritesCount(), user.getFollowersCount(), user.getFriendsCount());
        return profile;
    }
}
