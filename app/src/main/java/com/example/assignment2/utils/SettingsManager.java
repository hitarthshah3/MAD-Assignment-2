package com.example.assignment2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    private static final String PREF_NAME = "FoodieSettings";

    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_LOCATION_SERVICES = "location_services";
    private static final String KEY_FIREBASE_SYNC = "firebase_sync";
    private static final String KEY_AUTO_BACKUP = "auto_backup";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_RESTAURANT_LOCATION = "restaurant_location";

    private final SharedPreferences prefs;

    public SettingsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isNotificationsEnabled() {
        return prefs.getBoolean(KEY_NOTIFICATIONS, true);
    }

    public void setNotificationsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply();
    }

    public boolean isLocationServicesEnabled() {
        return prefs.getBoolean(KEY_LOCATION_SERVICES, true);
    }

    public void setLocationServicesEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_LOCATION_SERVICES, enabled).apply();
    }

    public boolean isFirebaseSyncEnabled() {
        return prefs.getBoolean(KEY_FIREBASE_SYNC, true);
    }

    public void setFirebaseSyncEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_FIREBASE_SYNC, enabled).apply();
    }

    public boolean isAutoBackupEnabled() {
        return prefs.getBoolean(KEY_AUTO_BACKUP, false);
    }

    public void setAutoBackupEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_AUTO_BACKUP, enabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public void setDarkModeEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public String getRestaurantLocation() {
        return prefs.getString(KEY_RESTAURANT_LOCATION, "Bandra West, Mumbai");
    }

    public void setRestaurantLocation(String location) {
        prefs.edit().putString(KEY_RESTAURANT_LOCATION, location).apply();
    }
}
