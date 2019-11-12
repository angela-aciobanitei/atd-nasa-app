package com.ang.acb.nasaapp.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ang.acb.nasaapp.data.remote.ApiResponse;

import retrofit2.Response;

public class ApiUtil {

    public static <T> LiveData<ApiResponse<T>> successCall(T data) {
        return createCall(Response.success(data));
    }

    public static <T> LiveData<ApiResponse<T>> createCall(Response<T> response) {
        MutableLiveData<ApiResponse<T>> data = new MutableLiveData<>();
        data.setValue(new ApiResponse<>(response));
        return data;
    }
}
