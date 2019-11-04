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
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.nasaapp.BuildConfig;
import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.ui.common.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;


import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static com.ang.acb.nasaapp.ui.earth.EarthPhotoFragment.ARG_LATITUDE;
import static com.ang.acb.nasaapp.ui.earth.EarthPhotoFragment.ARG_LONGITUDE;


/**
 * A fragment that displays the last known location as a marker on a Google Map. The user can
 * drag the marker to pick a location, then hit Search to get a photo from NASA Earth Imagery API.
 * See: https://github.com/android/location-samples/tree/master/BasicLocation
 */
public class EarthMapFragment extends Fragment implements OnMapReadyCallback,
                                      LocationSource.OnLocationChangedListener {

    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 123;

    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;
    private GoogleMap map;
    private Marker marker;
    private double markerLat;
    private double markerLng;

    // Required empty public constructor
    public EarthMapFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger for injecting Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
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
        // Initialize the map.
        map = googleMap;

        // Enable zoom controls. These are disabled by default.
        map.getUiSettings().setZoomControlsEnabled(true);

        addMapStyle(map);
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

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(getHostActivity(), task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        lastLocation = task.getResult();
                        Timber.d("Last known location: " + lastLocation.getLatitude()
                                + ", " + lastLocation.getLongitude());
                        showSnackbar("Last known location:\n " + lastLocation.getLatitude()
                                + ", " + lastLocation.getLongitude());
                        addMarkerToMap(lastLocation.getLatitude(), lastLocation.getLongitude());
                    } else {
                        Timber.w("getLastLocation:exception: %s", task.getException());
                        showSnackbar(getString(R.string.no_location_detected));
                    }
                })
                .addOnFailureListener(getHostActivity(), exception ->
                        Timber.w("getLastLocation:exception: %s", exception));
    }

    private void addMarkerToMap(double lat, double lng) {
        markerLat = lat;
        markerLng = lng;

        LatLng latLng = new LatLng(lat,lng);

        // Note: By default, a marker is not draggable, it must be set to be draggable.
        marker = map.addMarker(new MarkerOptions().position(latLng).draggable(true));

        // Move the camera instantly to default marker with a zoom of 1.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 1));
        map.animateCamera(CameraUpdateFactory.zoomTo(1), 2000, null);

        // Listen for drag events on the marker.
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng position = marker.getPosition();
                markerLat = position.latitude;
                markerLng = position.longitude;
                Timber.d("onMarkerDragEnd: " + markerLat + ", " + markerLng);
                showSnackbar("Marker coordinates:\n " + markerLat + ", " + markerLng);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        // Update marker position
        marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.earth_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.earth_search) {
            // Navigate to earth photo details fragment.
            Bundle args = new Bundle();
            args.putDouble(ARG_LATITUDE, markerLat);
            args.putDouble(ARG_LONGITUDE, markerLng);
            Timber.d("onOptionsItemSelected: %s, %s", markerLat, markerLng);
            NavHostFragment.findNavController(EarthMapFragment.this)
                    .navigate(R.id.action_earth_search_to_earth_photo_details, args);
        }
        return super.onOptionsItemSelected(item);
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
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                getHostActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user
        // denied the request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Timber.i("Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok, view ->
                    startLocationPermissionRequest());

        } else {
            // Request permission. It's possible this can be auto answered if device
            // policy sets the permission in a given state or the user denied the
            // permission previously and checked "Never ask again".
            Timber.i("Requesting permission.");
            startLocationPermissionRequest();
        }
    }


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

    private void showSnackbar(final String text) {
        View container = getHostActivity().findViewById(R.id.main_root_layout);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int textStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(getHostActivity().findViewById(android.R.id.content),
                      getString(textStringId), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener)
                .show();
    }

    private MainActivity getHostActivity(){
        return (MainActivity) getActivity();
    }
}
