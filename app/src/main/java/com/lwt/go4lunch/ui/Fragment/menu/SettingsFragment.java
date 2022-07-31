package com.lwt.go4lunch.ui.Fragment.menu;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.lwt.go4lunch.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

}
