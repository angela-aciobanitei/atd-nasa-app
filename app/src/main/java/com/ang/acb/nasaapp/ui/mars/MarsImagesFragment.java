package com.ang.acb.nasaapp.ui.mars;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.nasaapp.R;

import org.jetbrains.annotations.NotNull;

import dagger.android.support.AndroidSupportInjection;


public class MarsImagesFragment extends Fragment {

    // Required empty public constructor
    public MarsImagesFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger for injecting Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mars_images, container, false);
    }
}
