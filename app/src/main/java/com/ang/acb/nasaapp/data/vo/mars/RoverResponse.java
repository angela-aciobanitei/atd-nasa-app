package com.ang.acb.nasaapp.data.vo.mars;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RoverResponse {

    @SerializedName("photos")
    private List<PhotoItem> photosItems;

    public void setPhotoItems(List<PhotoItem> photosItems){
        this.photosItems = photosItems;
    }

    public List<PhotoItem> getPhotoItems(){
        return photosItems;
    }

    @NonNull
    public List<Integer> getNasaPhotoIds() {
        List<Integer> nasaPhotoIds = new ArrayList<>();
        for (PhotoItem photoItem : photosItems) {
            nasaPhotoIds.add(photoItem.getId());
        }
        return nasaPhotoIds;
    }
}
