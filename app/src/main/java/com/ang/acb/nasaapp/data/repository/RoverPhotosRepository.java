package com.ang.acb.nasaapp.data.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.ang.acb.nasaapp.data.local.db.AppDatabase;
import com.ang.acb.nasaapp.data.local.entity.RoverPhoto;
import com.ang.acb.nasaapp.data.local.entity.RoverSearchResult;
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
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 */
@Singleton
public class RoverPhotosRepository {

    private AppDatabase database;
    private ApiService apiService;
    private AppExecutors executors;

    @Inject
    RoverPhotosRepository(AppDatabase database, ApiService apiService, AppExecutors executors) {
        this.database = database;
        this.apiService = apiService;
        this.executors = executors;
    }

    public LiveData<Resource<List<RoverPhoto>>> searchRoverPhotos(String solQuery) {
        // Note that we are using the NetworkBoundResource<ResultType, RequestType> class
        // that we've created earlier which can provide a resource backed by both the
        // SQLite database and the network. It defines two type parameters, ResultType
        // and RequestType, because the data type used locally might not match the data
        // type returned from the API.
        return new NetworkBoundResource<List<RoverPhoto>, RoverResponse>(executors) {

            @NonNull
            @Override
            protected LiveData<ApiResponse<RoverResponse>> createCall() {
                Timber.d("Create call to NASA API.");
                return apiService.getRoverImagesCameraAll(solQuery);
            }

            @Override
            protected void saveCallResult(@NonNull RoverResponse response) {
                // Save the NASA API response into the database.
                List<Integer> nasaPhotoIds = response.getNasaPhotoIds();
                RoverSearchResult searchResult = new RoverSearchResult(solQuery, nasaPhotoIds);

                database.runInTransaction(() -> {
                    long searchId = database.roverPhotoDao().insertRoverSearchResult(searchResult);
                    Timber.d("Inserted search result with ID= %s in the db", searchId);
                    int savedItems = database.roverPhotoDao().insertPhotosFromResponse(response);
                    Timber.d("Inserted %s rover photos in the db", savedItems);
                });
            }

            @NonNull
            @Override
            protected LiveData<List<RoverPhoto>> loadFromDb() {
                Timber.d("Get the cached rover photos from the database");
                return Transformations.switchMap(database.roverPhotoDao().search(solQuery), searchData -> {
                    if (searchData == null) return AbsentLiveData.create();
                    else return database.roverPhotoDao().loadRoverPhotosByNasaIds(searchData.nasaIds);
                });
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RoverPhoto> dbData) {
                return dbData == null || dbData.isEmpty();
            }

            @NonNull
            @Override
            protected void onFetchFailed() {
                Timber.d("onFetchFailed");
            }
        }.asLiveData();
    }
}
