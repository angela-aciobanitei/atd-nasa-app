package com.ang.acb.nasaapp.data.vo.earth;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Assets {

    @SerializedName("count")
    private int count;

    @SerializedName("results")
    private List<ResultsItem> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ResultsItem> getResults() {
        return results;
    }

    public void setResults(List<ResultsItem> results) {
        this.results = results;
    }
}
