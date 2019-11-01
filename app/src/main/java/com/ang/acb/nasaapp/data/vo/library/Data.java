package com.ang.acb.nasaapp.data.vo.library;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("keywords")
    private List<String> keywords;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("date_created")
    private String dateCreated;

    @SerializedName("center")
    private String center;

    @SerializedName("title")
    private String title;

    @SerializedName("nasa_id")
    private String nasaId;

    @SerializedName("description_508")
    private String description508;

    public void setKeywords(List<String> keywords){
        this.keywords = keywords;
    }

    public List<String> getKeywords(){
        return keywords;
    }

    public void setMediaType(String mediaType){
        this.mediaType = mediaType;
    }

    public String getMediaType(){
        return mediaType;
    }

    public void setDateCreated(String dateCreated){
        this.dateCreated = dateCreated;
    }

    public String getDateCreated(){
        return dateCreated;
    }

    public void setCenter(String center){
        this.center = center;
    }

    public String getCenter(){
        return center;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setNasaId(String nasaId){
        this.nasaId = nasaId;
    }

    public String getNasaId(){
        return nasaId;
    }

    public void setDescription508(String description508){
        this.description508 = description508;
    }

    public String getDescription508(){
        return description508;
    }
}
