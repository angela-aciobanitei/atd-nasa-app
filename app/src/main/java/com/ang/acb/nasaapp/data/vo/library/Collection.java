package com.ang.acb.nasaapp.data.vo.library;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Collection {

    @SerializedName("metadata")
    private Metadata metadata;

    @SerializedName("items")
    private List<Items> items;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}
