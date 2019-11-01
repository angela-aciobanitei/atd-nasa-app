package com.ang.acb.nasaapp.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ang.acb.nasaapp.data.vo.apod.Apod;

/**
 * The Room database for this app.
 */
@Database(entities = {LibraryImage.class, MarsRover.class, Apod.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    //todo declare DAOs here
}
