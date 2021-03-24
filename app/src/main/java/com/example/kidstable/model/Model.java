package com.example.kidstable.model;

public class Model {

    public int level;
    public boolean isRunning, isComplete;
    public String type;

    public Model(int level, boolean isRunning, boolean isComplete, String type) {
        this.level = level;
        this.isRunning = isRunning;
        this.isComplete = isComplete;
        this.type = type;
    }
}
