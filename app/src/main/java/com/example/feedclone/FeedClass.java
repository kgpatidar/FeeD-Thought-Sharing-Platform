package com.example.feedclone;

public class FeedClass {
    String feedId;
    String feedUser;
    String feed;
    String date;
    String time;

    public FeedClass() {}

    public FeedClass(String feedId, String feedUser, String feed, String date, String time) {
        this.feedId = feedId;
        this.feedUser = feedUser;
        this.feed = feed;
        this.date = date;
        this.time = time;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFeedUser() {
        return feedUser;
    }

    public void setFeedUser(String feedUser) {
        this.feedUser = feedUser;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
