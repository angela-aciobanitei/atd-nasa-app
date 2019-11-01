package com.ang.acb.nasaapp.data.vo.earth;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("date")
    private String date;

    @SerializedName("cloud_score")
    private double cloudScore;

    @SerializedName("url")
    private String url;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCloudScore() {
        return cloudScore;
    }

    public void setCloudScore(double cloudScore) {
        this.cloudScore = cloudScore;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
