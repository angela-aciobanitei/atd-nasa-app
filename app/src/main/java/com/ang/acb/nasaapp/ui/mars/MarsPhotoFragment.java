package com.ang.acb.nasaapp.ui.mars;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.data.local.entity.MarsPhoto;
import com.ang.acb.nasaapp.databinding.FragmentMarsPhotoBinding;
import com.ang.acb.nasaapp.ui.common.MainActivity;
import com.ang.acb.nasaapp.utils.GlideApp;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.ang.acb.nasaapp.ui.mars.MarsSearchFragment.ARG_MARS_PHOTO_ROOM_ID;


public class MarsPhotoFragment extends Fragment {

    private FragmentMarsPhotoBinding binding;
    private MarsPhotoViewModel marsPhotoViewModel;
    private long marsPhotoId;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public MarsPhotoFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            marsPhotoId = getArguments().getLong(ARG_MARS_PHOTO_ROOM_ID);
        }

        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext())
                .inflateTransition(android.R.transition.move));

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get an instance of the binding class.
        binding = FragmentMarsPhotoBinding.inflate(inflater, container, false);

        // Set the string value of the pin ID as the unique transition name
        // for the image view that will be used in the shared element transition.
        ViewCompat.setTransitionName(binding.marsPhoto, String.valueOf(marsPhotoId));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
        observeMarsPhoto();
    }

    private void initViewModel() {
        marsPhotoViewModel = ViewModelProviders.of(getHostActivity(), viewModelFactory)
                .get(MarsPhotoViewModel.class);
        marsPhotoViewModel.setMarsPhotoId(marsPhotoId);
    }

    private void observeMarsPhoto() {
        marsPhotoViewModel.getMarsPhoto().observe(getViewLifecycleOwner(), marsPhoto -> {
            binding.setMarsPhoto(marsPhoto);
            loadPhoto(marsPhoto.getImgSrc());
            binding.executePendingBindings();
        });
    }

    private void loadPhoto(String photoUrl) {
        GlideApp.with(this)
                // Calling GlideApp.with() returns a RequestBuilder.
                // By default you get a Drawable RequestBuilder, but
                // you can change the requested type using as... methods.
                // For example, asBitmap() returns a Bitmap RequestBuilder.
                .asBitmap()
                .load(photoUrl)
                // Tell Glide not to use its standard crossfade animation.
                .dontAnimate()
                // Display a placeholder until the image is loaded and processed.
                .placeholder(R.color.imagePlaceholder)
                // Keep track of errors and successful image loading.
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Bitmap> target, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target,
                                                   com.bumptech.glide.load.DataSource dataSource,
                                                   boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(binding.marsPhoto);
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
