package com.ang.acb.nasaapp.ui.apod;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ang.acb.nasaapp.BuildConfig;
import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.databinding.FragmentApodBinding;
import com.ang.acb.nasaapp.ui.common.MainActivity;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class ApodFragment extends Fragment {

    private FragmentApodBinding binding;
    private ApodViewModel apodViewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public ApodFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger for injecting Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        // Inflate the layout for this fragment and get an instance of the binding class.
        binding = FragmentApodBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();
        observeResult();
    }

    private void setupToolbarTitle(String title) {
        ActionBar actionBar = getHostActivity().getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    private void initViewModel() {
        apodViewModel = ViewModelProviders
                .of(getHostActivity(), viewModelFactory)
                .get(ApodViewModel.class);
    }

    private void observeResult() {
        apodViewModel.getApod().observe(getViewLifecycleOwner(), apodResource -> {
            binding.setResource(apodResource);
            binding.setRetryCallback(() -> apodViewModel.retry());
            if (apodResource != null && apodResource.data != null) {
                binding.setApod(apodResource.data);
                setupToolbarTitle(apodResource.data.getTitle());
            }
            binding.executePendingBindings();
        });
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
