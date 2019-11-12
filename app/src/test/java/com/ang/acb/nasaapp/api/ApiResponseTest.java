package com.ang.acb.nasaapp.api;

import com.ang.acb.nasaapp.data.remote.ApiResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class ApiResponseTest {

    @Test
    public void exception() {
        Exception exception = new Exception("foo");
        ApiResponse<String> apiResponse = new ApiResponse<>(exception);
        assertThat(apiResponse.getBody(), nullValue());
        assertThat(apiResponse.getCode(), is(500));
        assertThat(apiResponse.getError(), is("foo"));
    }

    @Test
    public void success() {
        ApiResponse<String> apiResponse = new ApiResponse<>(Response.success("foo"));
        assertThat(apiResponse.getError(), nullValue());
        assertThat(apiResponse.getCode(), is(200));
        assertThat(apiResponse.getBody(), is("foo"));
    }

    @Test
    public void error() {
        ApiResponse<String> response = new ApiResponse<String>(Response.error(400,
                ResponseBody.create("blah", MediaType.parse("application/txt"))));
        assertThat(response.getCode(), is(400));
        assertThat(response.getError(), is("blah"));
    }
}

