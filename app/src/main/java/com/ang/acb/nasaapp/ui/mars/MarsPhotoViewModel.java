package com.ang.acb.nasaapp.ui.mars;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.nasaapp.data.local.entity.MarsPhoto;
import com.ang.acb.nasaapp.data.repository.MarsPhotosRepository;
import com.ang.acb.nasaapp.utils.AbsentLiveData;

import javax.inject.Inject;


/**
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 * See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
 */
public class MarsPhotoViewModel extends ViewModel {

    private MarsPhotosRepository marsPhotosRepository;
    private MutableLiveData<Long> marsPhotoId = new MutableLiveData<>();
    private LiveData<MarsPhoto> marsPhoto;

    @Inject
    public MarsPhotoViewModel(MarsPhotosRepository marsPhotosRepository) {
        this.marsPhotosRepository = marsPhotosRepository;
    }

    public void setMarsPhotoId(long id) {
        marsPhotoId.setValue(id);
    }

    public LiveData<MarsPhoto> getMarsPhoto() {
        if (marsPhoto == null) {
            marsPhoto = Transformations.switchMap(marsPhotoId, id -> {
                if (id == null) return AbsentLiveData.create();
                else return marsPhotosRepository.getMarsPhotoById(id);
            });
        }

        return  marsPhoto;
    }
}
