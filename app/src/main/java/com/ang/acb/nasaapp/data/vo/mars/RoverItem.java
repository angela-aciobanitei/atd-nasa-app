package com.ang.acb.nasaapp.data.vo.mars;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoverItem {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("launch_date")
    private String launchDate;

    @SerializedName("landing_date")
    private String landingDate;

    @SerializedName("status")
    private String status;

    @SerializedName("max_sol")
    private int maxSol;

    @SerializedName("max_date")
    private String maxDate;

    @SerializedName("cameras")
    private List<CameraItem> cameras;

    @SerializedName("total_photos")
    private int totalPhotos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public String getLandingDate() {
        return landingDate;
    }

    public void setLandingDate(String landingDate) {
        this.landingDate = landingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMaxSol() {
        return maxSol;
    }

    public void setMaxSol(int maxSol) {
        this.maxSol = maxSol;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public List<CameraItem> getCameras() {
        return cameras;
    }

    public void setCameras(List<CameraItem> cameras) {
        this.cameras = cameras;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }
}
