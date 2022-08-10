package com.lwt.go4lunch.ui.Activities;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.lwt.go4lunch.R;
import com.lwt.go4lunch.databinding.ActivityMainBinding;
import com.lwt.go4lunch.services.map.APIClient;
import com.lwt.go4lunch.services.map.ApiInterface;
import com.lwt.go4lunch.services.map.PlacesPOJO;
import com.lwt.go4lunch.services.map.RecyclerViewAdapter;
import com.lwt.go4lunch.services.map.RestaurantModel;
import com.lwt.go4lunch.services.map.ResultDistanceMatrix;
import com.lwt.go4lunch.ui.Fragment.ListViewFragment;
import com.lwt.go4lunch.ui.Fragment.MapsFragment;
import com.lwt.go4lunch.ui.Fragment.WorkmatesFragment;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity<ActivityMainBinding> implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    //current location
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    //NearBy
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    List<RestaurantModel> restaurantModelList;
    ApiInterface apiService;

    String latLngString;
    LatLng latLng;

    RecyclerView recyclerView;
    EditText editText;
    Button button;
    List<PlacesPOJO.CustomA> results;


    @Override
    ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout file as the content view.
        setContentView(R.layout.activity_main);
        //use for current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        //Bottom Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            loadFragment(new MapsFragment());
        }
        nearbySearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_lunch:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new RestaurantDetail()).commit();
//                break;
//            case R.id.nav_favorite:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new FavoriteRestaurant()).commit();
//                break;
//            case R.id.nav_settings:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new SettingsFragment()).commit();
//                break;
//        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_menu:
//                return true;

            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }
    //Get current location
    private void getCurrentLocation()    {
        if (ActivityCompat.checkSelfPermission(
                this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
            return;

        }
        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location !=null){
                    currentLocation=location;
//                    Toast.makeText(getApplicationContext(),(int) currentLocation.getLatitude(),Toast.LENGTH_LONG)
//                            .show();
                    Bundle result = new Bundle();
                    result.putParcelable("bundleKey",location);
                    getSupportFragmentManager().setFragmentResult("locationKey", result);
                }
            }
        });

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Toast.makeText(getApplicationContext()," location result is  " + locationResult, Toast.LENGTH_LONG).show();

                if (locationResult == null) {
                    Toast.makeText(getApplicationContext(),"current location is null ", Toast.LENGTH_LONG).show();

                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Toast.makeText(getApplicationContext(),"current location is " + location.getLongitude(), Toast.LENGTH_LONG).show();

                        //TODO: UI updates.
                    }
                }
            }
        };

    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (REQUEST_CODE){
//            case REQUEST_CODE:
//                if(grantResults.length> 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
//                    getCurrentLocation();
//                }
//                break;
//        }
//    }

    //Nearby search

    public void nearbySearch (){
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            else {
                fetchLocation();
            }
        } else {
            fetchLocation();
        }


        apiService = APIClient.getClient().create(ApiInterface.class);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString().trim();
                String[] split = s.split("\\s+");


                if (split.length != 2) {
                    Toast.makeText(getApplicationContext(), "Please enter text in the required format", Toast.LENGTH_SHORT).show();
                } else
                    fetchStores(split[0], split[1]);
            }
        });

    }

    private void fetchStores(String placeType, String businessName) {

        /**
         * For Locations In India McDonalds stores aren't returned accurately
         */

        Call<PlacesPOJO.Root> call = apiService.doPlaces(placeType, latLngString, businessName, true, "distance", APIClient.GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<PlacesPOJO.Root>() {
            @Override
            public void onResponse(Call<PlacesPOJO.Root> call, Response<PlacesPOJO.Root> response) {
                PlacesPOJO.Root root = response.body();


                if (response.isSuccessful()) {

                    if (root.status.equals("OK")) {

                        results = root.customA;
                        restaurantModelList = new ArrayList<>();
                        for (int i = 0; i < results.size(); i++) {

                            if (i == 10)
                                break;
                            PlacesPOJO.CustomA info = results.get(i);


                            fetchDistance(info);

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No matches found near you", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PlacesPOJO.Root> call, Throwable t) {
                // Log error here since request failed
                call.cancel();
            }
        });

    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
        case ALL_PERMISSIONS_RESULT:

            for (String perms : permissionsToRequest) {
            if (!hasPermission(perms)) {
                permissionsRejected.add(perms);
            }
        }

        if (permissionsRejected.size() > 0) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                    }
                                }
                            });
                    return;
                }
            }

        } else {
            fetchLocation();
        }

        break;
    }}

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void fetchLocation() {

        SmartLocation.with(this).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        latLngString = location.getLatitude() + "," + location.getLongitude();
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                });
    }

    private void fetchDistance(final PlacesPOJO.CustomA info) {

        Call<ResultDistanceMatrix> call = apiService.getDistance(APIClient.GOOGLE_PLACE_API_KEY, latLngString, info.geometry.locationA.lat + "," + info.geometry.locationA.lng);
        call.enqueue(new Callback<ResultDistanceMatrix>() {
            @Override
            public void onResponse(Call<ResultDistanceMatrix> call, Response<ResultDistanceMatrix> response) {

                ResultDistanceMatrix resultDistance = response.body();
                if ("OK".equalsIgnoreCase(resultDistance.status)) {

                    ResultDistanceMatrix.InfoDistanceMatrix infoDistanceMatrix = resultDistance.rows.get(0);
                    ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement distanceElement = (ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement) infoDistanceMatrix.elements.get(0);
                    if ("OK".equalsIgnoreCase(distanceElement.status)) {
                        ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDuration = distanceElement.duration;
                        ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDistance = distanceElement.distance;
                        String totalDistance = String.valueOf(itemDistance.text);
                        String totalDuration = String.valueOf(itemDuration.text);

                        restaurantModelList.add(new RestaurantModel(info.name, info.vicinity, totalDistance, totalDuration));


                        if (restaurantModelList.size() == 10 || restaurantModelList.size() == results.size()) {
                            RecyclerViewAdapter adapterStores = new RecyclerViewAdapter(results, restaurantModelList);
                            recyclerView.setAdapter(adapterStores);
                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<ResultDistanceMatrix> call, Throwable t) {
                call.cancel();
            }
        });

    }
}





