package com.ang.acb.nasaapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ang.acb.nasaapp.data.remote.ApiService;
import com.ang.acb.nasaapp.data.vo.Resource;
import com.ang.acb.nasaapp.data.vo.earth.EarthImage;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
public class EarthPhotoRepository {

    private ApiService apiService;

    private final MutableLiveData<Resource<EarthImage>> result = new MutableLiveData<>();

    @Inject
    EarthPhotoRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<EarthImage>> getEarthImage(LatLng latLng) {
        // Send loading state to the UI.
        result.postValue(Resource.loading(null));

        apiService.getEarthImage((float) latLng.longitude, (float) latLng.latitude)
                .enqueue(new Callback<EarthImage>() {
                    @Override
                    public void onResponse(@NotNull Call<EarthImage> call,
                                           @NotNull Response<EarthImage> response) {
                        if (response.isSuccessful()) {
                            result.postValue(Resource.success(response.body()));
                        } else {
                            Timber.d("getApod -> onResponse: %s", response.code());
                            result.postValue(Resource.error(response.message(), null));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<EarthImage> call,
                                          @NotNull Throwable throwable) {
                        Timber.d("getApod -> onFailure: %s", throwable.getMessage());
                        result.postValue(Resource.error(throwable.getMessage(), null));
                    }
        });

        return result;
    }
}
