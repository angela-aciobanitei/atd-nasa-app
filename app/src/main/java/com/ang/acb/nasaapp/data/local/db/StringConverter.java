package com.ang.acb.nasaapp.data.local.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Type converters to allow Room to reference complex data types.
 */
public class StringConverter {

    // Use Gson, a Java serialization/deserialization library, to convert Java Objects into JSON and back.
    // See: https://stackoverflow.com/questions/44986626/android-room-database-how-to-handle-arraylist-in-an-entity

    @TypeConverter
    public static List<Integer> stringToIntegerList(String value) {
        if (value == null) return Collections.emptyList();
        // Get the type of the collection.
        Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
        // De-serialization
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String integerListToString(List<Integer> list) {
        Gson gson = new Gson();
        // Serialization
        return gson.toJson(list);
    }
}
