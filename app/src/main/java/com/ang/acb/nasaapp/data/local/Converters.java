package com.ang.acb.nasaapp.data.local;

import androidx.room.TypeConverter;

import com.ang.acb.nasaapp.data.vo.mars.Camera;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Type converters to allow Room to reference complex data types.
 */
public class Converters {

    // Use Gson, a Java serialization/deserialization library, to convert Java Objects into JSON and back.
    // See: https://stackoverflow.com/questions/44986626/android-room-database-how-to-handle-arraylist-in-an-entity
    private static Gson gson = new Gson();

    @TypeConverter
    public static String toJson(List<Camera> list) {
        // Serialization
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Camera> fromJson(String jsonStr) {
        if (jsonStr == null) return Collections.emptyList();

        // Get the type of the collection.
        Type listType = new TypeToken<List<Camera>>() {}.getType();

        // De-serialization
        return gson.fromJson(jsonStr, listType);
    }

    @TypeConverter
    public static List<String> fromString(String value) {
        if (value == null) return Collections.emptyList();
        // Get the type of the collection.
        Type listType = new TypeToken<List<String>>() {}.getType();
        // De-serialization
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        // Serialization
        return new Gson().toJson(list);
    }
}
