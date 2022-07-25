package com.lwt.go4lunch.ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.lwt.go4lunch.R;
import com.lwt.go4lunch.databinding.ActivityLoginBinding;
import com.lwt.go4lunch.databinding.ActivityMainBinding;
import com.lwt.go4lunch.ui.Fragment.ListViewFragment;
import com.lwt.go4lunch.ui.Fragment.MapsFragment;
import com.lwt.go4lunch.ui.Fragment.WorkmatesFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding>  {

    private BottomNavigationView bottomNavigationView;
    @Override
    ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
       @Override
       public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout file as the content view.
        setContentView(R.layout.activity_main);
        //Bottom Bar
         BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
         bottomNavigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
                    }

    private BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_map:
                   loadFragment(new MapsFragment());
                    return true;
                case R.id.action_listView:

                    loadFragment(new ListViewFragment());
                    return true;
                case R.id.action_workmates:

                    loadFragment(new WorkmatesFragment());
                    return true;

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //AppBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu:
                return true;

            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}