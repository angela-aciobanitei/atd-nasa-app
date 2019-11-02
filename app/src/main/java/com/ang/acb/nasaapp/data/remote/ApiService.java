package com.ang.acb.nasaapp.data.remote;

import androidx.lifecycle.LiveData;

import com.ang.acb.nasaapp.data.vo.apod.Apod;
import com.ang.acb.nasaapp.data.vo.earth.Assets;
import com.ang.acb.nasaapp.data.vo.earth.Image;
import com.ang.acb.nasaapp.data.vo.mars.RoverResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Defines the REST API access points for Retrofit.
 */
public interface ApiService {

    // Used for APOD section
    @GET("/planetary/apod")
    Call<Apod> getApod();

    // Used for MARS section
    @GET
    Call<RoverResponse> getRoverImagesForCamera(@Url String url,
                                                @Query("sol") String sol,
                                                @Query("camera") String camera);

    // Used for MARS section
    @GET("/mars-photos/api/v1/rovers/curiosity/photos?")
    LiveData<ApiResponse<RoverResponse>> getRoverImagesCameraAll(@Query("sol") String sol);

    // Used for EARTH section
    @GET("/planetary/earth/assets")
    LiveData<Assets> getAssets(@Query("lon") String longitude,
                               @Query("lat") String latitude,
                               @Query("begin")String beginDate);

    // Used for EARTH section
    @GET("/planetary/earth/imagery/?cloud_score=True")
    LiveData<Image> getEarthImage(@Query("lon") String longitude,
                                  @Query("lat") String latitude,
                                  @Query("date")String date);
}
