package com.example.covid19app;

//model class for leaderboard data
public class LeaderboardData {
    private String username;
    private int score;

    //default constructor for Firebase
    public LeaderboardData() {
    }

    public LeaderboardData(int score, String username) {
        this.score = score;
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
