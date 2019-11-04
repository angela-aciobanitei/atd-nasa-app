package com.ang.acb.nasaapp.data.repository;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ang.acb.nasaapp.data.remote.ApiService;
import com.ang.acb.nasaapp.data.vo.Resource;
import com.ang.acb.nasaapp.data.vo.apod.Apod;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


@Singleton
public class ApodRepository {

    private ApiService apiService;

    private final MutableLiveData<Resource<Apod>> result = new MutableLiveData<>();

    @Inject
    ApodRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<Apod>> getApod() {
        // Send loading state to the UI.
        result.postValue(Resource.loading(null));

        apiService.getApod().enqueue(new Callback<Apod>() {
            @Override
            public void onResponse(@NotNull Call<Apod> call, @NotNull Response<Apod> response) {
                if (response.isSuccessful()) {
                    result.postValue(Resource.success(response.body()));
                } else {
                    Timber.d("getApod -> onResponse: %s", response.code());
                    result.postValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Apod> call, @NotNull Throwable throwable) {
                Timber.d("getApod -> onFailure: %s", throwable.getMessage());
                result.postValue(Resource.error(throwable.getMessage(), null));
            }
        });

        return result;
    }
}
