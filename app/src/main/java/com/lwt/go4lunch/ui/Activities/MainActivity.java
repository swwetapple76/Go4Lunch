package com.lwt.go4lunch.ui.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lwt.go4lunch.R;
import com.lwt.go4lunch.databinding.ActivityMainBinding;
import com.lwt.go4lunch.ui.Fragment.ListViewFragment;
import com.lwt.go4lunch.ui.Fragment.MapsFragment;
import com.lwt.go4lunch.ui.Fragment.WorkmatesFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements PopupMenu.OnMenuItemClickListener {

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

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return null;
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

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, popup.getMenu());
        popup.show();
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.drawer_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_lunch:
                lunch(item);
                return true;
            case R.id.nav_settings:
                setting(item);
                return true;
            case R.id.logout:
                logout(item);
                return true;
            default:
                return false;
        }
    }

    private void logout(MenuItem item) {
    }

    private void setting(MenuItem item) {
    }

    private void lunch(MenuItem item) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}