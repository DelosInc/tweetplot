package com.noonoo.tweetplot;

import java.sql.Date;;

public class Profile implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private Date createdAt;
    private String name, description, location;
    private int favoritesCount, followersCount, friendsCount;

    public long getId() {
        return this.id;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public int getFavoritesCount() {
        return this.favoritesCount;
    }

    public int getFollowersCount() {
        return this.followersCount;
    }

    public int getFriendsCount() {
        return this.friendsCount;
    }

    public void setData(long id, Date createdAt, String name, String description, String location, int favoritesCount, int followersCount, int friendsCount) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.description = description;
        this.location = location;
        this.favoritesCount = favoritesCount;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
    }
}