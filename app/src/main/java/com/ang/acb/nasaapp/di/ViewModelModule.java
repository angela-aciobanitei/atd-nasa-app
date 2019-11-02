package com.ang.acb.nasaapp.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.nasaapp.ui.apod.ApodViewModel;
import com.ang.acb.nasaapp.ui.mars.MarsPhotoViewModel;
import com.ang.acb.nasaapp.ui.mars.MarsSearchViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    // TODO Bind ViewModels here ...

    @Binds
    @IntoMap
    @ViewModelKey(ApodViewModel.class)
    abstract ViewModel bindApodViewModel(ApodViewModel apodViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MarsSearchViewModel.class)
    abstract ViewModel bindMarsSearchPhotosViewModel(MarsSearchViewModel marsSearchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MarsPhotoViewModel.class)
    abstract ViewModel bindMarsPhotoViewModel(MarsPhotoViewModel marsPhotoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
