package com.ang.acb.nasaapp.ui.earth;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.nasaapp.data.repository.EarthPhotoRepository;
import com.ang.acb.nasaapp.data.vo.Resource;
import com.ang.acb.nasaapp.data.vo.earth.EarthImage;
import com.ang.acb.nasaapp.utils.AbsentLiveData;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

public class EarthPhotoViewModel extends ViewModel {

    private EarthPhotoRepository repository;
    private LiveData<Resource<EarthImage>> earthImage;
    private MutableLiveData<LatLng> position = new MutableLiveData<>();

    @Inject
    public EarthPhotoViewModel(EarthPhotoRepository repository) {
        this.repository = repository;
    }

    public void setPosition(LatLng latLng) {
        position.setValue(latLng);
    }

    public LiveData<Resource<EarthImage>> getEarthImage(){
        if (earthImage == null) {
            earthImage = Transformations.switchMap(position, latlng -> {
                if (latlng == null) return AbsentLiveData.create();
                else return repository.getEarthImage(latlng);
            });
        }

        return earthImage;
    }

    void retry() {
        if (position.getValue() != null) {
            position.setValue(position.getValue());
        }
    }
}
