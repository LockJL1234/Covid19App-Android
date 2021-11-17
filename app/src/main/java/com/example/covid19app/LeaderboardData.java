package com.example.covid19app;

//model class for leaderboard data
public class LeaderboardData {
    private String score, username;

    //default constructor for Firebase
    public LeaderboardData() {
    }

    public LeaderboardData(String score, String username) {
        this.score = score;
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
