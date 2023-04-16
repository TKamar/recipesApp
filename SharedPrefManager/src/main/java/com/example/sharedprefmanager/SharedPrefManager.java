/**
 * A class for managing shared preferences in Android applications.
 */
package com.example.sharedprefmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SharedPrefManager {

    private static final String PREF_NAME = "my_pref";
    private static final String KEY_URIS = "uris";
    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;

    /**
     * Constructor for initializing the shared preference manager.
     *
     * @param context the context of the application.
     */
    private SharedPrefManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Returns a single instance of the SharedPrefManager class.
     *
     * @param context the context of the application.
     * @return the single instance of the SharedPrefManager class.
     */
    public static SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    /**
     * Returns a list of stored URIs.
     *
     * @return the list of stored URIs.
     */
    public List<String> getUris() {
        String urisString = sharedPreferences.getString(KEY_URIS, "");
        String[] urisArray = TextUtils.split(urisString, ",");
        List<String> uris = new ArrayList<>();
        for (String uri : urisArray) {
            uris.add(uri);
        }
        return uris;
    }

    /**
     * Adds a URI to the list of stored URIs.
     *
     * @param uri the URI to be added.
     */
    public void addUri(Uri uri) {
        List<String> uris = getUris();
        if (!uris.contains(uri.toString())) {
            uris.add(uri.toString());
            sharedPreferences.edit().putString(KEY_URIS, TextUtils.join(",", uris)).commit();
        }
    }

    /**
     * Removes a URI from the list of stored URIs.
     *
     * @param uri the URI to be removed.
     */
    public void removeUri(Uri uri) {
        List<String> uris = getUris();
        uris.remove(uri.toString());
        sharedPreferences.edit().putString(KEY_URIS, TextUtils.join(",", uris)).commit();
    }

    /**
     * Clears all stored URIs.
     */
    public void clearUris() {
        sharedPreferences.edit().remove(KEY_URIS).commit();
    }

    /**
     * Get a boolean preference value with a specified key.
     *
     * @param key          The key used to store the boolean value.
     * @param defaultValue The default value to return if the preference does not exist.
     * @return The boolean value for the given key or the default value if the preference does not exist.
     */
    public boolean getBooleanPreference(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Set a boolean preference value with a specified key.
     *
     * @param key   The key used to store the boolean value.
     * @param value The value to store for the given key.
     */
    public void setBooleanPreference(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * Get an integer preference value with a specified key.
     *
     * @param key          The key used to store the integer value.
     * @param defaultValue The default value to return if the preference does not exist.
     * @return The integer value for the given key or the default value if the preference does not exist.
     */
    public int getIntegerPreference(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Set an integer preference value with a specified key.
     *
     * @param key   The key used to store the integer value.
     * @param value The value to store for the given key.
     */
    public void setIntegerPreference(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * Get a float preference value with a specified key.
     *
     * @param key          The key used to store the float value.
     * @param defaultValue The default value to return if the preference does not exist.
     * @return The float value for the given key or the default value if the preference does not exist.
     */
    public float getFloatPreference(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * Set a float preference value with a specified key.
     *
     * @param key   The key used to store the float value.
     * @param value The value to store for the given key.
     */
    public void setFloatPreference(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    /**
     * Retrieves a String preference value for the given key from the SharedPreferences.
     * If the preference does not exist, it returns the default value provided.
     *
     * @param key          The key of the preference to retrieve
     * @param defaultValue The default value to return if the preference does not exist
     * @return The String value of the preference, or defaultValue if the preference does not exist
     */
    public String getStringPreference(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Sets a String preference value for the given key in the SharedPreferences.
     *
     * @param key   The key of the preference to set
     * @param value The value to set for the preference
     */
    public void setStringPreference(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * Retrieves a Set of String preference value for the given key from the SharedPreferences.
     * If the preference does not exist, it returns the default value provided.
     *
     * @param key          The key of the preference to retrieve
     * @param defaultValue The default value to return if the preference does not exist
     * @return The Set of String values of the preference, or defaultValue if the preference does not exist
     */
    public Set<String> getStringSetPreference(String key, Set<String> defaultValue) {
        return sharedPreferences.getStringSet(key, defaultValue);
    }

    /**
     * Sets a Set of String preference value for the given key in the SharedPreferences.
     *
     * @param key   The key of the preference to set
     * @param value The value to set for the preference
     */
    public void setStringSetPreference(String key, Set<String> value) {
        sharedPreferences.edit().putStringSet(key, value).apply();
    }

    /**
     * Retrieves a List of preference values for the given key from the SharedPreferences.
     * If the preference does not exist, it returns an empty List.
     *
     * @param key   The key of the preference to retrieve
     * @param clazz The class of the List elements
     * @return The List of preference values for the given key, or an empty List if the preference does not exist
     */
    public <T> List<T> getListPreference(String key, Class<T[]> clazz) {
        String json = sharedPreferences.getString(key, null);
        if (json == null) {
            return new ArrayList<>();
        }
        T[] array = new Gson().fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * Sets a List of preference values for the given key in the SharedPreferences.
     *
     * @param key   The key of the preference to set
     * @param value The List of values to set for the preference
     */
    public <T> void setListPreference(String key, List<T> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        sharedPreferences.edit().putString(key, json).apply();
    }
}