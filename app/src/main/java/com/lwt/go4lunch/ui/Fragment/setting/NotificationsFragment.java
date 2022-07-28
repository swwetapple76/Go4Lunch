package com.lwt.go4lunch.ui.Fragment.setting;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.lwt.go4lunch.R;

import java.util.Objects;

public class NotificationsFragment extends PreferenceFragmentCompat {

    //FIELDS
    private static final String PREF_NOTIFICATION_KEY = "notification_firebase";

    private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.notifications_settings, rootKey);

        mOnSharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(PREF_NOTIFICATION_KEY)) {
                SwitchPreferenceCompat switchPreference = findPreference(key);
                Objects.requireNonNull(switchPreference)
                        .setSwitchTextOn("Notifications ok");
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);

        SwitchPreferenceCompat switchPreferenceCompat = findPreference(PREF_NOTIFICATION_KEY);
        Objects.requireNonNull(switchPreferenceCompat).setSwitchTextOn("Notifications ok");
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }
}
