package com.ang.acb.nasaapp.ui.mars;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.data.local.entity.MarsPhoto;
import com.ang.acb.nasaapp.databinding.FragmentMarsSearchBinding;
import com.ang.acb.nasaapp.ui.common.MainActivity;
import com.ang.acb.nasaapp.utils.GridMarginDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class MarsSearchFragment extends Fragment {

    public static final String ARG_MARS_PHOTO_ROOM_ID = "ARG_MARS_PHOTO_ROOM_ID";

    private FragmentMarsSearchBinding binding;
    private MarsPhotosAdapter marsPhotosAdapter;
    private MarsSearchViewModel marsSearchViewModel;
    private AtomicBoolean isEnterTransitionStarted;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public MarsSearchFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // Note: Inject as early as possible.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get in instance of the binding class.
        binding = FragmentMarsSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupToolbarTitle();
        initViewModel();
        prepareTransition();
        initAdapter();
        populateUi();
        initSearchInputListener();
    }

    private void setupToolbarTitle() {
        ActionBar actionBar = getHostActivity().getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(getString(R.string.mars_search_toolbar_title));
    }

    private void initViewModel() {
        marsSearchViewModel = ViewModelProviders.of(getHostActivity(), viewModelFactory)
                .get(MarsSearchViewModel.class);
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                getHostActivity(), getResources().getInteger(R.integer.columns_count));
        binding.rvMarsPhotos.setLayoutManager(layoutManager);
        binding.rvMarsPhotos.addItemDecoration(new GridMarginDecoration(
                getHostActivity(), R.dimen.grid_item_spacing));
        marsPhotosAdapter = new MarsPhotosAdapter(new MarsPhotosAdapter.MarsPhotoListener() {
            @Override
            public void onPhotoItemClick(MarsPhoto marsPhoto, ImageView sharedImage) {
                // On photo click navigate to mars photo details fragment
                // passing the shared element transition extras to the Navigator.
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(sharedImage, sharedImage.getTransitionName())
                        .build();
                Bundle args = new Bundle();
                args.putLong(ARG_MARS_PHOTO_ROOM_ID, marsPhoto.getId());
                NavHostFragment.findNavController(MarsSearchFragment.this)
                        .navigate(R.id.action_mars_search_to_mars_photo_details,
                                args, null, extras);
            }

            @Override
            public void onPhotoLoaded() {
                schedulePostponedEnterTransition();
            }
        });
        binding.rvMarsPhotos.setAdapter(marsPhotosAdapter);
    }

    private void prepareTransition() {
        isEnterTransitionStarted = new AtomicBoolean();
        setEnterTransition(TransitionInflater.from(getContext())
                .inflateTransition(android.R.transition.move));
        setSharedElementEnterTransition(TransitionInflater.from(getContext())
                .inflateTransition(android.R.transition.move));
    }

    private void schedulePostponedEnterTransition() {
        if (isEnterTransitionStarted.getAndSet(true)) return;
        // Before calling startPostponedEnterTransition(), make sure that
        // the view is drawn using ViewTreeObserver's OnPreDrawListener.
        // https://medium.com/@ayushkhare/shared-element-transitions-4a645a30c848
        binding.rvMarsPhotos.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        binding.rvMarsPhotos.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        // Parent has been drawn. Start transition.
                        startPostponedEnterTransition();
                        return true;
                    }
                }
        );
    }


    private void initSearchInputListener() {
        binding.input.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view);
                return true;
            }
            return false;
        });

        binding.input.setOnKeyListener((view, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                doSearch(view);
                return true;
            }
            return false;
        });
    }

    private void doSearch(View view) {
        String query = binding.input.getText().toString();
        dismissKeyboard(view.getWindowToken());
        binding.setQuery(query);
        marsSearchViewModel.setQuery(query);
    }

    private void dismissKeyboard(IBinder windowToken) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputMethodManager)
                    .hideSoftInputFromWindow(windowToken, 0);
        }
    }

    private void populateUi() {
        marsSearchViewModel.getSearchResults().observe(getViewLifecycleOwner(), result -> {
            binding.setResource(result);
            binding.setRetryCallback(() -> marsSearchViewModel.retry());
            int searchCount = (result == null || result.data == null) ? 0 : result.data.size();
            binding.setSearchCount(searchCount);
            if (result != null && result.data != null) {
                marsPhotosAdapter.updateData(result.data);
            }
            binding.executePendingBindings();
            // Delay transition until all data is loaded.
            postponeEnterTransition();
        });
    }

    private MainActivity getHostActivity(){
        return (MainActivity) getActivity();
    }
}
