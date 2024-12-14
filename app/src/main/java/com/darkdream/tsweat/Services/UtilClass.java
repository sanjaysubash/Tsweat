package com.darkdream.tsweat.Services;

import android.content.Context;
import android.content.SharedPreferences;

public class UtilClass {

    public static void savePreference(Context context,String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("T-SWEAT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();  // Use apply() for asynchronous save
    }

    // Retrieve a string value from SharedPreferences
    public static String getPreference(Context context,String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("T-SWEAT", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);  // Return null if the value doesn't exist
    }
    public static void clearPreference(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("T-SWEAT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();  // Use apply() to remove the key-value pair asynchronously
    }

}
