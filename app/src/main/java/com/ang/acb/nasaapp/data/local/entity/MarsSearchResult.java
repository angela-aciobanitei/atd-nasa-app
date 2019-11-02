package com.ang.acb.nasaapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.List;

@Entity(primaryKeys = {"query"}, tableName = "mars_search_results")
public class MarsSearchResult {

    @NonNull
    public final String query;
    public final List<Integer> nasaIds;

    public MarsSearchResult(@NonNull String query, List<Integer> nasaIds) {
        this.query = query;
        this.nasaIds = nasaIds;
    }
}
