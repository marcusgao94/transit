package com.example.android.bustracker;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.bustracker.directions.Direction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static String USGS_REQUEST_URL = "https://maps.googleapis.com/maps/api/directions/json?key=AIzaSyAEBBlM76YfcI7of-9Q5UkTM2O_NBjGaNw";


    BusInfoAdapter adapter;
    private EditText start_name, end_name, stop_id;
    private String typeName, url;
    private TextView emptyTextView;
    ListView listView;

    // For the navigation bar
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;


    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final LatLng mDefaultLocation = new LatLng(40.453901, -79.943153);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private boolean mLocationPermissionGranted;
    private Location mLastLocation;
    private Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG, "OnCreate is called");

        /**-----------------------------Set a Toolbar to replace the ActionBar.---------------------------*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        /**After press the search button, the list of buses will be displayed on the screen. */
        final Button searchButton = (Button)findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.list_view);
        final Button departTiButton = (Button) findViewById(R.id.depart_time);
//        departTiButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showTimePickerDialog(view);
//            }
//        });
//        final Button departDaButton = (Button) findViewById(R.id.depart_date);
//        departDaButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePickerDialog(view);
//            }
//        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchClicked();
            }
        });

        /**-----------------------------Add floating button in main Page, to check the upcoming bus.---------------------------*/
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LatestRouteActivity.class);
                startActivity(intent);
            }
        });


        /**-----------------------------Add Google Map in main page.---------------------------*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    /**-----------------------------The code below is about the navigation bar.---------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

//    public void selectDrawerItem(MenuItem menuItem) {
//        // Create a new fragment and specify the fragment to show based on nav item clicked
//        Fragment fragment = null;
//        Class fragmentClass;
//        switch(menuItem.getItemId()) {
//            case R.id.nav_first_fragment:
//                fragmentClass = FirstFragment.class;
//                break;
//            case R.id.nav_second_fragment:
//                fragmentClass = SecondFragment.class;
//                break;
//            case R.id.nav_third_fragment:
//                fragmentClass = ThirdFragment.class;
//                break;
//            default:
//                fragmentClass = FirstFragment.class;
//        }
//
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
//
//        // Highlight the selected item has been done by NavigationView
//        menuItem.setChecked(true);
//        // Set action bar title
//        setTitle(menuItem.getTitle());
//        // Close the navigation drawer
//        mDrawer.closeDrawers();
//    }

    /**-----------------------------The code below is about the google map action.---------------------------*/
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            default:
                return ;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // request location
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null) {
                LocationRequest mLocationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(5000)
                        .setFastestInterval(1000)
                        .setMaxWaitTime(5000);
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }
            else {
                updateMap();
            }
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(LOG_TAG, "Location services connection failed with code " +
                    result.getErrorCode());
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(LOG_TAG, "Play services connection suspended");
    }

    @Override
    public void onLocationChanged(final Location location) {
        //your code here
        Log.w(LOG_TAG, "location changed");
        mLastLocation = location;
        updateMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout)findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        updateMap();
    }

    public void updateMap() {
        if (mLastLocation != null) {
            if (marker != null)
                marker.remove();
            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            mLastLocation.getLatitude(),
                            mLastLocation.getLongitude()))
                    .title("Team Six's here ^_^"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastLocation.getLatitude(),
                            mLastLocation.getLongitude()), DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            Log.w(LOG_TAG, "Current location is null. Using defaults");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }



    // Text input and search bar

    private class DyfiAsyncTask extends AsyncTask<String,Void,Direction> {
        @Override
        protected Direction doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            MyAPIClient myClient = new MyAPIClient(urls[0]);
            Direction direction = myClient.getDirections();

            return direction;
        }

        @Override
        protected void onPostExecute(Direction direction) {
            updateUi(direction);
        }
    }

    /**-----------------------------The code below is about search action.---------------------------*/
    private void searchClicked() {
        if (isConnectedNetwork(this)) {
            Log.i(LOG_TAG, "Connected");
            start_name = (EditText)findViewById(R.id.type_start);
            String start_place = start_name.getText().toString().replace(" ", "+");
            end_name = (EditText)findViewById(R.id.type_end);
            String end_place = end_name.getText().toString().replace(" ", "+");

            url = USGS_REQUEST_URL + "&origin="
            + start_place + "+Pittsburgh&destination=" + end_place + "+Pittsburgh";

            DyfiAsyncTask dyfi = new DyfiAsyncTask();
            dyfi.execute(url);
//            if (typeName.isEmpty()) {
//                Toast.makeText(MainActivity.this, "Please enter search item!", Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//
//            }
        } else {
            Log.i(LOG_TAG, "No connection");
            Toast.makeText(MainActivity.this, "No Internet Connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update the UI with the given earthquake information.
     */
    private void updateUi(Direction direction) {
        Intent result_intent = new Intent(MainActivity.this, SearchResultActivity.class);
        result_intent.putExtra("search_result", direction);
        startActivity(result_intent);
    }

    public static boolean isConnectedNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**-----------------------------The code below is about select time and date.---------------------------*/

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }
}
