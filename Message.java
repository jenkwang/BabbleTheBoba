package com.example.jenkwang.babbletheboba;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Message implements Comparable<Message> {
    private String postid;
    private String title;
    private String content;
    private int score;

    public Message() {
    }

    public Message(String postid, String title, String content, int score) {
        this.postid = postid;
        this.title = title;
        this.content = content;
        this.score = score;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) { this.postid = postid; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) { this.score = score; }

    //public void incScore() {
     //   score++;
    //}

    //public void decScore() {
      //  score--;
    //}

    public int compareTo (Message m) {
        int other = ((Message)m).getScore();
        //biggest score to smallest
        return other - this.score;

    }
}
