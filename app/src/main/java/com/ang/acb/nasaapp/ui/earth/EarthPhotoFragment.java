package com.ang.acb.nasaapp.ui.earth;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.databinding.FragmentEarthPhotoBinding;
import com.ang.acb.nasaapp.ui.common.MainActivity;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class EarthPhotoFragment extends Fragment {

    public static final String ARG_LATITUDE = "ARG_LATITUDE";
    public static final String ARG_LONGITUDE = "ARG_LONGITUDE";

    private FragmentEarthPhotoBinding binding;
    private EarthPhotoViewModel viewModel;
    private double latitude;
    private double longitude;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public EarthPhotoFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger for injecting Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            latitude = args.getDouble(ARG_LATITUDE);
            longitude = args.getDouble(ARG_LONGITUDE);
            Timber.d("Latitude: %s, longitude: %s", latitude, longitude);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get an instance of the binding class.
        binding = FragmentEarthPhotoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
        observeResult();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(getHostActivity(), viewModelFactory)
                .get(EarthPhotoViewModel.class);
        // FIXME: For testing
        viewModel.setPosition(new LatLng( 1.5, 100.75));
        // viewModel.setPosition(new LatLng( latitude, longitude));
    }

    private void observeResult() {
        viewModel.getEarthImage().observe(getViewLifecycleOwner(), earthImageResource -> {
            binding.setResource(earthImageResource);
            binding.setRetryCallback(() -> viewModel.retry());
            if (earthImageResource != null && earthImageResource.data != null) {
                binding.setEarthPhoto(earthImageResource.data);
                setupToolbarTitle(earthImageResource.data.getDate());
            }
            binding.executePendingBindings();
        });
    }

    private void setupToolbarTitle(String title) {
        ActionBar actionBar = getHostActivity().getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    private MainActivity getHostActivity(){
        return (MainActivity) getActivity();
    }
}
