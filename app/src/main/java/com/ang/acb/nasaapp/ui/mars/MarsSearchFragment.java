package com.ang.acb.nasaapp.ui.mars;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.data.local.entity.RoverPhoto;
import com.ang.acb.nasaapp.databinding.FragmentMarsSearchBinding;
import com.ang.acb.nasaapp.ui.common.MainActivity;
import com.ang.acb.nasaapp.utils.GridMarginDecoration;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class MarsSearchFragment extends Fragment {

    private static final String ARG_ROOM_PHOTO_ID = "ARG_ROOM_PHOTO_ID";

    private FragmentMarsSearchBinding binding;
    private RoverPhotosAdapter roverPhotosAdapter;
    private MarsSearchViewModel marsSearchViewModel;

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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get in instance of the binding class.
        binding = FragmentMarsSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
        initAdapter();
        populateUi();
        initSearchInputListener();
        handleRetryEvents();
    }


    private void initViewModel() {
        marsSearchViewModel = ViewModelProviders
                .of(getHostActivity(), viewModelFactory)
                .get(MarsSearchViewModel.class);
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                getHostActivity(), getResources().getInteger(R.integer.columns_count));
        binding.rvRovers.setLayoutManager(layoutManager);
        binding.rvRovers.addItemDecoration(new GridMarginDecoration(
                getHostActivity(), R.dimen.grid_item_spacing));
        roverPhotosAdapter = new RoverPhotosAdapter(this::onPhotoClick);
        binding.rvRovers.setAdapter(roverPhotosAdapter);
    }

    private void onPhotoClick(RoverPhoto roverPhoto) {
        // On photo click navigate to rover photo details fragment.
        Bundle args = new Bundle();
        args.putLong(ARG_ROOM_PHOTO_ID, roverPhoto.getId());
        NavHostFragment.findNavController(MarsSearchFragment.this)
                .navigate(R.id.action_mars_search_to_mars_photo_details, args);

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
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    private void populateUi() {
        marsSearchViewModel.getSearchResults().observe(getViewLifecycleOwner(), result -> {
            binding.setResource(result);
            int searchCount = (result == null || result.data == null) ? 0 : result.data.size();
            binding.setSearchCount(searchCount);
            if (result != null && result.data != null) {
                roverPhotosAdapter.updateData(result.data);
            }
            binding.executePendingBindings();
        });
    }

    private void handleRetryEvents() {
        // Handle retry event in case of network failure.
        binding.setRetryCallback(() -> marsSearchViewModel.retry());
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
