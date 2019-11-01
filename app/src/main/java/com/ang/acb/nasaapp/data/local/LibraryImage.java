package com.ang.acb.nasaapp.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "library_image")
public class LibraryImage {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String date;
    private String title;
    private String description;
    @ColumnInfo(name = "image_url")
    private String imageUrl;

    public LibraryImage(long id, String date, String title, String description, String imageUrl) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Ignore
    public LibraryImage(String date, String title, String description, String imageUrl) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
