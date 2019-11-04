package com.ang.acb.nasaapp.data.remote;

import androidx.lifecycle.LiveData;

import com.ang.acb.nasaapp.data.vo.apod.Apod;
import com.ang.acb.nasaapp.data.vo.earth.Assets;
import com.ang.acb.nasaapp.data.vo.earth.EarthImage;
import com.ang.acb.nasaapp.data.vo.mars.RoverResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Defines the REST API access points for Retrofit.
 */
public interface ApiService {

    // Used for APOD section
    @GET("/planetary/apod")
    Call<Apod> getApod();

    // Used for MARS section
    @GET("/mars-photos/api/v1/rovers/curiosity/photos?")
    LiveData<ApiResponse<RoverResponse>> getRoverImagesCameraAll(@Query("sol") String sol);

    // Used for EARTH section
    // Query Parameters:
    //      lat	float Latitude
    //      lon	float Longitude
    //      dim	float width and height of image in degrees
    //      date YYYY-MM-DD	date of image; if not supplied, then the most recent image is returned
    //      cloud_score	bool calculate the percentage of the image covered by clouds, default is False.
    //      api_key	string	api.nasa.gov key for expanded usage
    @GET("/planetary/earth/imagery/?date=2014-02-01")
    Call<EarthImage> getEarthImage(@Query("lon") float longitude,
                                   @Query("lat") float latitude);
}
