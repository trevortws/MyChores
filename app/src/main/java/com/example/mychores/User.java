package com.example.mychores;

/**
 * Created by user on 21/11/2017.
 */

public class User {
    private String db_ID;
    private String firstName;
    private String lastName;
    private int score;
    private int icon;

    public String getDb_ID() {
        return db_ID;
    }

    public void setDb_ID(String db_ID) {
        this.db_ID = db_ID;
    }

    public User() {

    }

    public User(String id, String firstName, String lastName, int score, int icon) {
        this.db_ID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.score = score;
        this.icon = icon;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }
}
