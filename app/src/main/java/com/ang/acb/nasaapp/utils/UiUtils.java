package com.ang.acb.nasaapp.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.palette.graphics.Palette;

public class UiUtils {

    public static float dipToPixels(Context context, float dipValue) {
        // See: https://stackoverflow.com/questions/8399184/convert-dip-to-px-in-android
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static Palette.Swatch getDominantColor(Palette palette) {
        // To extract prominent colors from an image, we can use the Platte
        // class. When a palette is generated, a number of colors with different
        // profiles are extracted from the image: vibrant, dark vibrant,
        // light vibrant, muted, dark muted, light muted, and dominant.
        // These can be retrieved from the appropriate getter method.
        // See: https://developer.android.com/training/material/palette-colors
        Palette.Swatch result = palette.getDominantSwatch();
        if (palette.getVibrantSwatch() != null) {
            result = palette.getVibrantSwatch();
        } else if (palette.getMutedSwatch() != null) {
            result = palette.getMutedSwatch();
        }
        return result;
    }
}
