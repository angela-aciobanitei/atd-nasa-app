package com.ang.acb.nasaapp.data.vo.mars;

import com.google.gson.annotations.SerializedName;

public class Camera {

    @SerializedName("id")
    private int id;

    @SerializedName("rover_id")
    private int roverId;

    @SerializedName("name")
    private String name;

    @SerializedName("full_name")
    private String fullName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoverId() {
        return roverId;
    }

    public void setRoverId(int roverId) {
        this.roverId = roverId;
    }

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
