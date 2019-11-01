package com.ang.acb.nasaapp.data.vo.library;

import com.google.gson.annotations.SerializedName;

public class LibraryImageResponse {

    @SerializedName("collection")
    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}
