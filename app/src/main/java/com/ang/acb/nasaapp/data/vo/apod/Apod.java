package com.ang.acb.nasaapp.data.vo.apod;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "apod")
public class Apod {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @SerializedName("date")
    private String date;

    @SerializedName("title")
    private String title;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("url")
    private String url;

    @ColumnInfo(name = "hd_url")
    @SerializedName("hdurl")
    private String hdUrl;

    @SerializedName("copyright")
    private String copyright;

    @ColumnInfo(name = "media_type")
    @SerializedName("media_type")
    private String mediaType;

    @ColumnInfo(name = "service_version")
    @SerializedName("service_version")
    private String serviceVersion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHdUrl() {
        return hdUrl;
    }

    public void setHdUrl(String hdUrl) {
        this.hdUrl = hdUrl;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }
}
