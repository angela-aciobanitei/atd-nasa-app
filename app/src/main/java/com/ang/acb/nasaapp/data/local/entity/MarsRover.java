package com.ang.acb.nasaapp.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ang.acb.nasaapp.data.vo.mars.PhotoItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "rover")
public class MarsRover {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "nasa_id")
    private int nasaId;

    @ColumnInfo(name = "rover_name")
    private String roverName;

    @ColumnInfo(name = "rover_image_url")
    private String roverImageUri;

    @ColumnInfo(name = "sol_setting")
    private String solSetting;

    @ColumnInfo(name = "camera_setting")
    private String cameraSetting;

    @ColumnInfo(name = "camera_list")
    private List<String> cameraList;

    @Ignore
    private List<PhotoItem> roverImages;
    private boolean isSelected;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNasaId() {
        return nasaId;
    }

    public void setNasaId(int nasaId) {
        this.nasaId = nasaId;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }

    public String getRoverImageUri() {
        return roverImageUri;
    }

    public void setRoverImageUri(String roverImageUri) {
        this.roverImageUri = roverImageUri;
    }

    public String getSolSetting() {
        return solSetting;
    }

    public void setSolSetting(String solSetting) {
        this.solSetting = solSetting;
    }

    public String getCameraSetting() {
        return cameraSetting;
    }

    public void setCameraSetting(String cameraSetting) {
        this.cameraSetting = cameraSetting;
    }

    public List<String> getCameraList() {
        cameraList = new ArrayList<>(Arrays.asList(
                "All", "fhaz", "rhaz", "mast", "chemcam",
                "mahli", "mardi", "navcam", "pancam", "minites"));
        return cameraList;
    }

    public void setCameraList(List<String> cameraList) {
        this.cameraList = cameraList;
    }

    public List<PhotoItem> getRoverImages() {
        return roverImages;
    }

    public void setRoverImages(List<PhotoItem> roverImages) {
        this.roverImages = roverImages;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
