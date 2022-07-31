package com.lwt.go4lunch.ui.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.lwt.go4lunch.R;
import com.lwt.go4lunch.databinding.ActivitySettingBinding;
import com.lwt.go4lunch.ui.Fragment.setting.SettingsHeaders;

import java.util.Objects;

public class SettingActivity extends BaseActivity <ActivitySettingBinding> implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    Toolbar mToolbar;

    @Override
    ActivitySettingBinding getViewBinding() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsHeaders())
                .commit();


        this.configureToolBar(getResources().getString(R.string.settings));
        if (mToolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    private void configureToolBar(String string) {
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();

        this.configureToolBar(pref.getTitle().toString());
        return true;
    }



}
