package com.ang.acb.nasaapp.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ang.acb.nasaapp.data.local.dao.RoverPhotoDao;
import com.ang.acb.nasaapp.data.local.entity.RoverPhoto;
import com.ang.acb.nasaapp.data.local.entity.RoverSearchResult;

/**
 * The Room database for this app.
 */
@Database(entities = {RoverPhoto.class, RoverSearchResult.class}, version = 4, exportSchema = false)
@TypeConverters({StringConverter.class, DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // TODO Declare all DAOs here...
    public abstract RoverPhotoDao roverPhotoDao();
}
