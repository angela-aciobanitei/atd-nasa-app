package com.ang.acb.nasaapp.data.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.ang.acb.nasaapp.data.local.db.AppDatabase;
import com.ang.acb.nasaapp.data.local.entity.MarsPhoto;
import com.ang.acb.nasaapp.data.local.entity.MarsSearchResult;
import com.ang.acb.nasaapp.data.remote.ApiResponse;
import com.ang.acb.nasaapp.data.remote.ApiService;
import com.ang.acb.nasaapp.data.vo.Resource;
import com.ang.acb.nasaapp.data.vo.mars.RoverResponse;
import com.ang.acb.nasaapp.utils.AbsentLiveData;
import com.ang.acb.nasaapp.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Repository module for handling data operations.
 *
 * See: https://developer.android.com/jetpack/docs/guide#truth
 * See: https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
 */
@Singleton
public class MarsPhotosRepository {

    private AppDatabase database;
    private ApiService apiService;
    private AppExecutors executors;

    @Inject
    MarsPhotosRepository(AppDatabase database, ApiService apiService, AppExecutors executors) {
        this.database = database;
        this.apiService = apiService;
        this.executors = executors;
    }

    public LiveData<Resource<List<MarsPhoto>>> searchMarsPhotos(String solQuery) {
        // Note that we are using the NetworkBoundResource<ResultType, RequestType> class
        // that we've created earlier which can provide a resource backed by both the
        // SQLite database and the network. It defines two type parameters, ResultType
        // and RequestType, because the data type used locally might not match the data
        // type returned from the API.
        return new NetworkBoundResource<List<MarsPhoto>, RoverResponse>(executors) {

            @NonNull
            @Override
            protected LiveData<ApiResponse<RoverResponse>> createCall() {
                Timber.d("Create call to NASA API to get Mars Rover photos.");
                return apiService.getRoverImagesCameraAll(solQuery);
            }

            @Override
            protected void saveCallResult(@NonNull RoverResponse response) {
                // Save the NASA API response into the database using a WorkerThread
                List<Integer> nasaPhotoIds = response.getNasaPhotoIds();
                MarsSearchResult searchResult = new MarsSearchResult(solQuery, nasaPhotoIds);

                executors.diskIO().execute(() -> {
                    database.runInTransaction(() -> {
                        long searchId = database.marsPhotoDao().insertMarsSearchResult(searchResult);
                        Timber.d("Inserted search result with ID= %s into the database.", searchId);
                        int savedItems = database.marsPhotoDao().insertPhotosFromResponse(response);
                        Timber.d("Inserted %s Mars photos into the database.", savedItems);
                    });
                });
            }

            @NonNull
            @Override
            protected LiveData<List<MarsPhoto>> loadFromDb() {
                Timber.d("Get the cached Mars photos from the database");
                return Transformations.switchMap(database.marsPhotoDao().search(solQuery), searchData -> {
                    if (searchData == null) return AbsentLiveData.create();
                    else return database.marsPhotoDao().loadMarsPhotosByNasaIds(searchData.nasaIds);
                });
            }

            @Override
            protected boolean shouldFetch(@Nullable List<MarsPhoto> dbData) {
                return dbData == null || dbData.isEmpty();
            }

            @NonNull
            @Override
            protected void onFetchFailed() {
                Timber.d("searchMarsPhotos -> onFetchFailed");
            }
        }.asLiveData();
    }

    public LiveData<MarsPhoto> getMarsPhotoById(Long id) {
        return database.marsPhotoDao().loadMarsPhotoByRoomId(id);
    }
}
