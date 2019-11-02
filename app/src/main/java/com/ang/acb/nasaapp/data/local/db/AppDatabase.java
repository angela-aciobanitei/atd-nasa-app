package com.ang.acb.nasaapp.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ang.acb.nasaapp.data.local.dao.ApodDao;
import com.ang.acb.nasaapp.data.local.entity.MarsRover;
import com.ang.acb.nasaapp.data.vo.apod.Apod;

/**
 * The Room database for this app.
 */
@Database(entities = {Apod.class, MarsRover.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    //todo declare DAOs here
    public abstract ApodDao apodDao();
}
