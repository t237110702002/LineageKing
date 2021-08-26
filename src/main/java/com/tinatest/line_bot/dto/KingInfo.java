package com.tinatest.line_bot.dto;

import java.util.Date;

public class KingInfo {

    private String name;
    private String location;
    private int period;
    private boolean random;
    private Date lastAppear;
    private Date nextAppear;

    public KingInfo(String name, String location, int period, boolean random, Date lastAppear, Date nextAppear) {
        this.name = name;
        this.location = location;
        this.period = period;
        this.random = random;
        this.lastAppear = lastAppear;
        this.nextAppear = nextAppear;
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

    public Date getLastAppear() {
        return lastAppear;
    }

    public void setLastAppear(Date lastAppear) {
        this.lastAppear = lastAppear;
    }

    public Date getNextAppear() {
        return nextAppear;
    }

    public void setNextAppear(Date nextAppear) {
        this.nextAppear = nextAppear;
    }

}
