package com.tinatest.line_bot.dto;

public class KingInfoRequest {

    private String id;
    private String name;
    private String location;
    private int period;
    private boolean random;
    private String lastAppear;
    private String nextAppear;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public String getLastAppear() {
        return lastAppear;
    }

    public void setLastAppear(String lastAppear) {
        this.lastAppear = lastAppear;
    }

    public String getNextAppear() {
        return nextAppear;
    }

    public void setNextAppear(String nextAppear) {
        this.nextAppear = nextAppear;
    }
}
