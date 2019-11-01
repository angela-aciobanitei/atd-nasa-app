package com.ang.acb.nasaapp.data.vo.mars;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoverResponse {

    @SerializedName("photos")
    private List<PhotoItem> photos;

    public void setPhotos(List<PhotoItem> photos){
        this.photos = photos;
    }

    public List<PhotoItem> getPhotos(){
        return photos;
    }
}
