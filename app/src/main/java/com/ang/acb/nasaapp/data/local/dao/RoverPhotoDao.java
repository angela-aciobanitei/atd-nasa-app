package com.ang.acb.nasaapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.nasaapp.data.local.entity.RoverPhoto;
import com.ang.acb.nasaapp.data.local.entity.RoverSearchResult;
import com.ang.acb.nasaapp.data.vo.mars.PhotoItem;
import com.ang.acb.nasaapp.data.vo.mars.RoverResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for database access on {@link RoverSearchResult} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public abstract class RoverPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertRoverPhoto(RoverPhoto roverPhoto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRoverPhotos(List<RoverPhoto> photos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public  abstract long insertRoverSearchResult(RoverSearchResult search);

    public int insertPhotosFromResponse(RoverResponse response) {
        List<PhotoItem> photosItems = response.getPhotoItems();
        List<Long> roomPhotoIds = new ArrayList<>();
        for (PhotoItem photoItem : photosItems) {
            if (photoItem != null) {
                roomPhotoIds.add(insertRoverPhoto(new RoverPhoto(
                        photoItem.getId(),
                        photoItem.getSol(),
                        photoItem.getImgSrc(),
                        photoItem.getEarthDate()
                )));
            }
        }

        return roomPhotoIds.size();
    }

    @Delete
    public abstract void deleteRoverSearchResult(RoverSearchResult search);

    @Delete
    public abstract void deleteRoverPhoto(RoverPhoto photo);

    @Query("DELETE FROM rover_photo WHERE id = :id")
    public abstract void deleteRoverPhotoById(long id);

    @Query("SELECT * FROM rover_search_results WHERE `query` = :query")
    public abstract LiveData<RoverSearchResult> search(String query);

    @Query("SELECT * FROM rover_photo WHERE id = :id")
    public abstract LiveData<RoverPhoto> getRoverPhotoByRoomId(long id);

    @Transaction
    @Query("SELECT * FROM rover_photo WHERE nasa_id = :nasaId")
    public abstract LiveData<RoverPhoto> getRoverPhotoByNasaId(String nasaId);

    @Transaction
    @Query("SELECT * FROM rover_photo WHERE nasa_id in (:nasaIds)")
    public abstract LiveData<List<RoverPhoto>> loadRoverPhotosByNasaIds(List<Integer> nasaIds);

}
