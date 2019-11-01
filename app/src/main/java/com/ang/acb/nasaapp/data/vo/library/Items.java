package com.ang.acb.nasaapp.data.vo.library;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Items {

    @SerializedName("data")
    private List<Data> data;

    @SerializedName("links")
    private List<Links> links;

    @SerializedName("href")
    private String href;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Links> getLinks() {
        return links;
    }

    public void setLinks(List<Links> links) {
        this.links = links;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
