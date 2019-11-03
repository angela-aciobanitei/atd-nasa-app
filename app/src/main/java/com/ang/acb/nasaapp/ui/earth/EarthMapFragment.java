package com.ang.acb.nasaapp.ui.earth;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ang.acb.nasaapp.BuildConfig;
import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.ui.common.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class EarthMapFragment extends Fragment implements OnMapReadyCallback {

    // Used when checking for runtime permissions.
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 123;

    // Provides the entry point to the Fused Location Provider API.
    private FusedLocationProviderClient fusedLocationClient;

    // Represents a geographical location.
    private Location lastLocation;

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
        setupMapFragment();
        initLocationClient();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    private void setupMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    private void initLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getHostActivity());
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

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(getHostActivity(), task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        lastLocation = task.getResult();
                        Timber.d(lastLocation.getLatitude() + " " + lastLocation.getLongitude());
                        showSnackbar(lastLocation.getLatitude() + " " + lastLocation.getLongitude());
                    } else {
                        Timber.w("getLastLocation:exception: %s", task.getException());
                        showSnackbar(getString(R.string.no_location_detected));
                    }
                })
                .addOnFailureListener(getHostActivity(), exception ->
                        Timber.w("getLastLocation:exception: %s", exception));
    }

    private void showSnackbar(final String text) {
        View container = getHostActivity().findViewById(R.id.main_root_layout);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(getHostActivity().findViewById(android.R.id.content),
                      getString(mainTextStringId),
                      Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getHostActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(getHostActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getHostActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user
        // denied the request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Timber.i("Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok, view -> {
                // Request permission
                startLocationPermissionRequest();
            });

        } else {
            Timber.i("Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Timber.i("onRequestPermissionResult");
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request
                // is cancelled and you receive empty arrays.
                Timber.i("User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings, view -> {
                    // Build intent that displays the App settings screen.
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",
                              BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
            }
        }
    }

    private void showExplanation() {
        Snackbar snackbar = Snackbar.make(
                getHostActivity().findViewById(android.R.id.content),
                R.string.earth_map_explanation, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.ok, view -> snackbar.dismiss());
        snackbar.show();

    }

    private MainActivity getHostActivity(){
        return (MainActivity) getActivity();
    }

}
