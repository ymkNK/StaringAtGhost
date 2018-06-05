package com.example.a233.staringatghost;

public class Msg {
    String id;
    String Score;

    public Msg() {
    }

    @Override
    public String toString() {
        return "Msg{" +
                "id='" + id + '\'' +
                ", Score='" + Score + '\'' +
                '}';
    }

    public Msg(String id, String score) {
        this.id = id;
        Score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }
}
