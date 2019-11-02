package com.ang.acb.nasaapp.ui.mars;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.nasaapp.R;
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

        // TODO postponeEnterTransition();
        // TODO setSharedElementEnterTransition(TransitionInflater.from(getContext())
        //  .inflateTransition(android.R.transition.move));

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get an instance of the binding class.
        binding = FragmentMarsPhotoBinding.inflate(inflater, container, false);

        // Set the string value of the pin ID as the unique transition name
        // for the image view that will be used in the shared element transition.
        // TODO ViewCompat.setTransitionName(binding.marsPhoto, String.valueOf(marsPhotoId));
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
                .asBitmap()
                .load(photoUrl)
                .dontAnimate()
                .placeholder(R.color.imagePlaceholder)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Bitmap> target, boolean isFirstResource) {
                        // TODO startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target,
                                                   com.bumptech.glide.load.DataSource dataSource,
                                                   boolean isFirstResource) {
                        // TODO startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(binding.marsPhoto);
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
