package com.ang.acb.nasaapp.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ang.acb.nasaapp.data.local.dao.MarsPhotoDao;
import com.ang.acb.nasaapp.data.local.entity.MarsPhoto;
import com.ang.acb.nasaapp.data.local.entity.MarsSearchResult;

/**
 * The Room database for this app.
 */
@Database(entities = {MarsPhoto.class, MarsSearchResult.class}, version = 7, exportSchema = false)
@TypeConverters({StringConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // TODO Declare all DAOs here...
    public abstract MarsPhotoDao marsPhotoDao();
}
