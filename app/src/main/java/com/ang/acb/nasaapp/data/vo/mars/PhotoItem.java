package com.ang.acb.nasaapp.data.vo.mars;

import com.google.gson.annotations.SerializedName;

// TODO Define one-to-many relationship with MarsRover
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
}
