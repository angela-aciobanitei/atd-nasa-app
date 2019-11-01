package com.ang.acb.nasaapp.data.vo.earth;

import com.google.gson.annotations.SerializedName;

class ResultsItem {

    @SerializedName("date")
    private String date;

    @SerializedName("id")
    private String id;

    private Image image;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
