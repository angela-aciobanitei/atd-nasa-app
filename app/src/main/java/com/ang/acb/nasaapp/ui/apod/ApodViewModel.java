package com.ang.acb.nasaapp.ui.apod;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.nasaapp.data.repository.ApodRepository;
import com.ang.acb.nasaapp.data.vo.Resource;
import com.ang.acb.nasaapp.data.vo.apod.Apod;

import javax.inject.Inject;

public class ApodViewModel extends ViewModel {

    private ApodRepository apodRepository;
    private LiveData<Resource<Apod>> apod;

    @Inject
    public ApodViewModel(ApodRepository apodRepository) {
        this.apodRepository = apodRepository;
    }

    public LiveData<Resource<Apod>> getApod(){
        if (apod == null) {
            apod = apodRepository.getApod();
        }

        return apod;
    }

    public void retry() {
        // TODO
    }
}
