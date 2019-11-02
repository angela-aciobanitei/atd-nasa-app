package com.ang.acb.nasaapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.nasaapp.data.vo.apod.Apod;

import java.util.List;

/**
 * Interface for database access on {@link Apod} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface ApodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Apod apod);

    @Delete
    void delete(Apod apod);

    @Query("DELETE FROM apod WHERE id = :id")
    void deleteById(long id);

    @Transaction
    @Query("SELECT * FROM apod WHERE id = :id")
    LiveData<Apod> getById(long id);

    @Transaction
    @Query("SELECT * FROM apod")
    LiveData<List<Apod>> getAll();
}
