package com.ang.acb.nasaapp.data.vo.mars;

import com.google.gson.annotations.SerializedName;

public class CameraItem {

    @SerializedName("name")
    private String name;

    @SerializedName("full_name")
    private String fullName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
