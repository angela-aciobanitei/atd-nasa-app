package com.ang.acb.nasaapp.ui.earth;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ang.acb.nasaapp.BuildConfig;
import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.ui.common.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class EarthMapFragment extends Fragment implements OnMapReadyCallback {

    // Used when checking for runtime permissions.
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 123;

    private GoogleMap map;

    // Required empty public constructor
    public EarthMapFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger for injecting Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_earth_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!checkLocationPermission()) requestPermissions();
        setupMapFragment();
    }


    private void setupMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Timber.d("onMapReady()");
        // Initialize the map.
        map = googleMap;

        // Enable zoom controls. These are disabled by default.
        map.getUiSettings().setZoomControlsEnabled(true);

        addMapStyle(map);
        addDefaultMarkerToMap();
    }

    private void addMapStyle(GoogleMap googleMap) {
        try {
            // See: https://developers.google.com/maps/documentation/javascript/styling
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                              getHostActivity(), R.raw.style_json));
            if (!success) Timber.e("Map styling parsing failed.");
        } catch (Resources.NotFoundException e) {
            Timber.e("Can't find style. Error: %s", e.getMessage());
        }
    }

    private void addDefaultMarkerToMap() {
        LatLng latLng = new LatLng(37.4220595,-122.0849086);
        Marker marker = map.addMarker(new MarkerOptions().position(latLng));
        marker.setDraggable(true);

        // Move the camera instantly to default marker with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    private boolean checkReady() {
        if (map == null) {
            Toast.makeText(getHostActivity(), R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onClearMap() {
        if (!checkReady()) return;
        map.clear();
    }

    private boolean checkLocationPermission(){
        int permissionState = ActivityCompat.checkSelfPermission(getHostActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getHostActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user
        // denied the request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Snackbar.make(getHostActivity().findViewById(android.R.id.content),
                          R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, view -> {
                        // Request permission
                        ActivityCompat.requestPermissions(getHostActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSIONS_REQUEST_CODE);
                    })
                    .show();
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getHostActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO Permission was granted.
            } else {
                // Permission denied.
                Snackbar.make(getHostActivity().findViewById(android.R.id.content),
                              R.string.permission_denied_explanation, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, view -> {
                            // Display the App Settings screen.
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                      BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .show();
            }
        }
    }

    private MainActivity getHostActivity(){
        return (MainActivity) getActivity();
    }

}
