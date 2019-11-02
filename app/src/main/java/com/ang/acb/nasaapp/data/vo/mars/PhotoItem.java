package com.ang.acb.nasaapp.data.vo.mars;

import com.google.gson.annotations.SerializedName;

public class PhotoItem {

    @SerializedName("id")
    private int id;

    @SerializedName("sol")
    private int sol;

    @SerializedName("earth_date")
    private String earthDate;

    @SerializedName("camera")
    private Camera camera;

    @SerializedName("roverItem")
    private RoverItem roverItem;

    @SerializedName("img_src")
    private String imgSrc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public RoverItem getRoverItem() {
        return roverItem;
    }

    public void setRoverItem(RoverItem roverItem) {
        this.roverItem = roverItem;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
