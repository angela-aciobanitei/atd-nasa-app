package com.ang.acb.nasaapp.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "rover_photo")
public class RoverPhoto {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "nasa_id")
    private int nasaId;

    @ColumnInfo(name = "sol")
    private int sol;

    @ColumnInfo(name = "img_src")
    private String imgSrc;

    @ColumnInfo(name = "earth_date")
    private String earthDate;

    public RoverPhoto(long id, int nasaId, int sol, String imgSrc, String earthDate) {
        this.id = id;
        this.nasaId = nasaId;
        this.sol = sol;
        this.imgSrc = imgSrc;
        this.earthDate = earthDate;
    }

    @Ignore
    public RoverPhoto(int nasaId, int sol, String imgSrc, String earthDate) {
        this.nasaId = nasaId;
        this.sol = sol;
        this.imgSrc = imgSrc;
        this.earthDate = earthDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNasaId() {
        return nasaId;
    }

    public void setNasaId(int nasaId) {
        this.nasaId = nasaId;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getSol() {
        return sol;
    }

    public void setSol(int sol) {
        this.sol = sol;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }
}
