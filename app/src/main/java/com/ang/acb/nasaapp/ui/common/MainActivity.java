package com.ang.acb.nasaapp.ui.common;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.ang.acb.nasaapp.BuildConfig;
import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * An activity that inflates a layout that has a NavHostFragment.
 */
public class MainActivity extends AppCompatActivity  implements HasSupportFragmentInjector {

    private ActivityMainBinding binding;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        // Note: a DispatchingAndroidInjector<T> performs members-injection
        // on instances of core Android types (e.g. Activity, Fragment) that
        // are constructed by the Android framework and not by Dagger.
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Note: when using Dagger for injecting Activity
        // objects, inject as early as possible.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        setSupportActionBar(binding.mainToolbar);
        setupBottomNavigationView();
        checkForNasaApiKey();
    }

    private void checkForNasaApiKey() {
        // See: https://github.com/googlemaps/android-places-demos
        if (BuildConfig.NASA_API_KEY.equals("")) {
            Snackbar snackbar = Snackbar.make(
                    this.findViewById(android.R.id.content),
                    R.string.error_api_key, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(android.R.string.ok, view -> snackbar.dismiss());
            snackbar.show();
        }
    }

    private void setupBottomNavigationView() {
        // See: https://www.raywenderlich.com/4332831-navigation-component-for-android-part-2-graphs-and-deep-links
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        ActionBar actionBar = getSupportActionBar();
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.mars_photo_details) {
                if(actionBar != null) actionBar.hide();
            } else {
                if(actionBar != null) actionBar.show();
            }
        });
        NavigationUI.setupWithNavController(binding.mainBottomNavigation, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }
}
