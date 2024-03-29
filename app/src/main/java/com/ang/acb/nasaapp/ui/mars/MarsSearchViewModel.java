package com.ang.acb.nasaapp.ui.mars;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.nasaapp.data.local.entity.MarsPhoto;
import com.ang.acb.nasaapp.data.repository.MarsPhotosRepository;
import com.ang.acb.nasaapp.data.vo.Resource;
import com.ang.acb.nasaapp.utils.AbsentLiveData;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

/**
 * See: https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 * See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
 */
public class MarsSearchViewModel extends ViewModel {

    private final MutableLiveData<String> query = new MutableLiveData<>();
    private final LiveData<Resource<List<MarsPhoto>>> searchResults;

    @Inject
    MarsSearchViewModel(MarsPhotosRepository repository) {
        searchResults = Transformations.switchMap(query, search -> {
            if (search == null || search.trim().length() == 0) {
                return AbsentLiveData.create();
            } else {
                return repository.searchMarsPhotos(search);
            }
        });
    }

    public LiveData<Resource<List<MarsPhoto>>> getSearchResults() {
        return searchResults;
    }

    public void setQuery(@NonNull String originalInput) {
        String input = originalInput.toLowerCase(Locale.getDefault()).trim();
        if (Objects.equals(input, query.getValue())) return;
        query.setValue(input);
    }

    void retry() {
        if (query.getValue() != null) {
            query.setValue(query.getValue());
        }
    }
}
