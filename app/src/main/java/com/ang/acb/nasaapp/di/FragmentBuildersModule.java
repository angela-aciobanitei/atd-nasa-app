package com.ang.acb.nasaapp.di;

import com.ang.acb.nasaapp.ui.apod.ApodFragment;
import com.ang.acb.nasaapp.ui.earth.EarthImageFragment;
import com.ang.acb.nasaapp.ui.mars.MarsPhotoFragment;
import com.ang.acb.nasaapp.ui.mars.MarsSearchFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ApodFragment contributeApodFragment();

    @ContributesAndroidInjector
    abstract EarthImageFragment contributeEarthImageFragment();

    @ContributesAndroidInjector
    abstract MarsSearchFragment contributeMarsSearchFragment();

    @ContributesAndroidInjector
    abstract MarsPhotoFragment contributeMarsPhotoFragment();
}
