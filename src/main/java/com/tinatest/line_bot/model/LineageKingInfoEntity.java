package com.tinatest.line_bot.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "lineage_king_info")
public class LineageKingInfoEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lineage_king_info_id_seq")
    @SequenceGenerator(name = "lineage_king_info_id_seq", sequenceName = "lineage_king_info_id_seq")
    private String id;

    @Column(name = "king_name")
    private String kingName;

    @Column(name = "location")
    private String location;

    @Column(name = "period_min")
    private int periodMin;

    @Column(name = "last_appear")
    private Date lastAppear;

    @Column(name = "next_appear")
    private Date nextAppear;

    @Column(name = "random")
    private Boolean random;

    @Column(name = "update_date")
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKingName() {
        return kingName;
    }

    public void setKingName(String kingName) {
        this.kingName = kingName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPeriodMin() {
        return periodMin;
    }

    public void setPeriodMin(int periodMin) {
        this.periodMin = periodMin;
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

    public Boolean getRandom() {
        return random;
    }

    public void setRandom(Boolean random) {
        this.random = random;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
