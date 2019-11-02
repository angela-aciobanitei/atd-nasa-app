package com.ang.acb.nasaapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.nasaapp.data.local.entity.MarsPhoto;
import com.ang.acb.nasaapp.data.local.entity.MarsSearchResult;
import com.ang.acb.nasaapp.data.vo.mars.PhotoItem;
import com.ang.acb.nasaapp.data.vo.mars.RoverResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for database access on {@link MarsSearchResult} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public abstract class MarsPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertMarsPhoto(MarsPhoto marsPhoto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMarsPhotos(List<MarsPhoto> photos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public  abstract long insertMarsSearchResult(MarsSearchResult search);

    public int insertPhotosFromResponse(RoverResponse response) {
        List<PhotoItem> photosItems = response.getPhotoItems();
        List<Long> roomPhotoIds = new ArrayList<>();
        for (PhotoItem photoItem : photosItems) {
            if (photoItem != null) {
                roomPhotoIds.add(insertMarsPhoto(new MarsPhoto(
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
    public abstract void deleteMarsSearchResult(MarsSearchResult search);

    @Delete
    public abstract void deleteMarsPhoto(MarsPhoto photo);

    @Query("DELETE FROM mars_photo WHERE id = :id")
    public abstract void deleteMarsPhotoById(long id);

    @Query("SELECT * FROM mars_search_results WHERE `query` = :query")
    public abstract LiveData<MarsSearchResult> search(String query);

    @Query("SELECT * FROM mars_photo WHERE id = :id")
    public abstract LiveData<MarsPhoto> getMarsPhotoByRoomId(long id);

    @Transaction
    @Query("SELECT * FROM mars_photo WHERE nasa_id = :nasaId")
    public abstract LiveData<MarsPhoto> getMarsPhotoByNasaId(String nasaId);

    @Transaction
    @Query("SELECT * FROM mars_photo WHERE nasa_id in (:nasaIds)")
    public abstract LiveData<List<MarsPhoto>> loadRoverPhotosByNasaIds(List<Integer> nasaIds);

}
