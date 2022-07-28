package com.lwt.go4lunch.ui.Fragment.setting;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.lwt.go4lunch.R;

public class SettingsHeaders extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_headers, rootKey);
    }
}
