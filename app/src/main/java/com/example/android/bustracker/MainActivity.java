package com.example.android.bustracker;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.bustracker.bus_station.BusStation;
import com.example.android.bustracker.bus_station.BusStationResponse;
import com.example.android.bustracker.directions.Direction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraIdleListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static String USGS_REQUEST_URL = "https://maps.googleapis.com/maps/api/directions/json?key=AIzaSyAEBBlM76YfcI7of-9Q5UkTM2O_NBjGaNw";


    BusInfoAdapter adapter;
    private PlaceAutocompleteFragment startFragment, endFragment;
    private String start_place, end_place;
    private String url;
    ListView listView;
    // Record if the leave now, or arrive by, or depart at.
    private String timeOptions;
    private static String timeString;
    private static String dateString;

    static Button departDaButton;
    static Button departTiButton;

    // For the navigation bar
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    private Spinner timeSpinner;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MyAPIClient myAPIClient;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private final LatLng mDefaultLocation = new LatLng(40.4438, -79.9438);
    private static float ZOOM_LEVEL = 17f;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private boolean mLocationPermissionGranted;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG, "OnCreate is called");

        myAPIClient = new MyAPIClient();

        /**-----------------------------Set a Toolbar to replace the ActionBar.---------------------------*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(this);


        /*----------------------set auto complete fragment------------------------*/
        startFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.type_start);
        startFragment.setHint("From");
        startFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                start_place = place.getAddress().toString();
                startFragment.setText(start_place);
            }

            @Override
            public void onError(Status status) {
                Log.e(LOG_TAG, "start place selection error");
            }
        });
        endFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.type_end);
        endFragment.setHint("To");
        endFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                end_place = place.getAddress().toString();
                endFragment.setText(end_place);
            }

            @Override
            public void onError(Status status) {
                Log.e(LOG_TAG, "end place selection error");
            }
        });

        /**After press the search button, the list of buses will be displayed on the screen. */
        final Button searchButton = (Button)findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.list_view);

        timeSpinner = (Spinner) findViewById(R.id.spinner1);

        timeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        departTiButton = (Button) findViewById(R.id.depart_time);
        departTiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });
        departDaButton = (Button) findViewById(R.id.depart_date);
        departDaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Uri webpage = Uri.parse("http://connectcard.org/nowonline.aspx");
        switch(id) {
            case R.id.nav_first_fragment:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.nav_second_fragment:
                startActivity(new Intent(Intent.ACTION_VIEW, webpage));
                break;
            case R.id.nav_third_fragment:
                startActivity(new Intent(MainActivity.this, PreferenceActivity.class));
                break;
            default:
//                fragment = new LoginActivity();
                break;
        }
        return true;
    }


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
            LocationRequest mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000)
                    .setFastestInterval(10000)
                    .setMaxWaitTime(10000);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            new BusStationAsyncTask().execute(mLastLocation);
        } else {
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
    public void onMapLongClick(LatLng latLng) {
        // change end_name from EditText to PlaceAutoCompleteFragment
        Geocoder geocoder = new Geocoder(this, Locale.US);
        try {
            Address address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
            end_place = address.getAddressLine(0);
            endFragment.setText(end_place);
        } catch (Exception e) {
            e.printStackTrace();
            end_place = String.format("%.6f, %.6f", latLng.latitude, latLng.longitude);
            endFragment.setText(end_place);
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        //your code here
        Log.w(LOG_TAG, "location changed");
        mLastLocation = location;
        Geocoder geocoder = new Geocoder(this, Locale.US);
        try {
            Address address = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1)
                    .get(0);
            start_place = address.getAddressLine(0);
            startFragment.setText(start_place);
        } catch (Exception e) {
            e.printStackTrace();
            start_place = String.format("%.6f, %.6f",
                    location.getLatitude(), location.getLongitude());
            endFragment.setText(start_place);
        }
        new BusStationAsyncTask().execute(location);
    }

    @Override
    public void onCameraIdle() {
        ZOOM_LEVEL = mMap.getCameraPosition().zoom;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        mMap.setOnMapLongClickListener(this);
        mMap.setOnCameraIdleListener(this);
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
        updateMap(null);
    }

    public void updateMap(List<BusStation> busStations) {
        if (mLastLocation != null) {
            Log.w(LOG_TAG, mLastLocation.toString());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastLocation.getLatitude(),
                            mLastLocation.getLongitude()), ZOOM_LEVEL));
            if (busStations != null) {
                mMap.clear();
                for (int i = 0; i < Math.min(3, busStations.size()); i++) {
                    BusStation bs = busStations.get(i);
                    mMap.addMarker(new MarkerOptions()
                            .position(bs.getGeometry().getLocation().toLatLng())
                            .title(bs.getName()));
                }
            }

        } else {
            Log.w(LOG_TAG, "Current location is null. Using defaults");
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    mDefaultLocation, ZOOM_LEVEL));
        }
    }

    private class BusStationAsyncTask extends AsyncTask<Location, Void, BusStationResponse> {
        @Override
        protected BusStationResponse doInBackground(Location... locations) {
            if (locations.length < 1 || locations[0] == null) {
                return null;
            }
            BusStationResponse bsr = myAPIClient.getBusStops(locations[0]);
            return bsr;
        }

        @Override
        protected void onPostExecute(BusStationResponse bsr) {
            if (bsr == null || !bsr.getStatus().equals("OK"))
                updateMap(null);
            else
                updateMap(bsr.getResults());
        }
    }

    // Text input and search bar

    private class DyfiAsyncTask extends AsyncTask<String,Void,Direction> {
        @Override
        protected Direction doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            Direction direction = myAPIClient.getDirections(urls[0]);

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
            try {
                Log.i(LOG_TAG, "Connected");
                Log.w(LOG_TAG, String.valueOf(R.id.type_start));
                if (start_place == null || start_place.isEmpty()) {
                    if (mLastLocation != null)
                        start_place = String.format("%.6f, %.6f",
                                mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    else
                        start_place = String.format("%.6f, %.6f",
                                mDefaultLocation.latitude, mDefaultLocation.longitude);
                }
                if (end_place == null || end_place.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter and select destination!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                long time = convertTime(dateString, timeString);
                url = USGS_REQUEST_URL + "&origin="
                        + start_place + "&destination=" + end_place + "";

                if (time != 0) {
                    // time selected
                    String timePara = "";
                    switch (timeOptions) {
                        case "Depart At":
                            timePara = "departure_time=";
                            break;
                        case "Arrive By":
                            timePara = "arrival_time=";
                            break;
                        default:
                            break;
                    }
                    url += "&" + timePara + time;
                }

                Log.i(LOG_TAG, url);

                DyfiAsyncTask dyfi = new DyfiAsyncTask();
                dyfi.execute(url);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception :" + e.toString());
                Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
            }
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
            String hour_x = hourOfDay + "";
            if (hourOfDay   < 9 ){ hour_x  = "0" + hourOfDay;}
//            Log.i("Selected Time - ",  hour_x + ":" + minute);
            timeString = hour_x + ":" + minute;
            departTiButton.setText(timeString);
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
            String day_x = ""+ day, month_x = "" + (month + 1);
            if (day   < 9 ){ day_x   = "0" + day_x;}
            if (month < 9 ){ month_x = "0" + month_x;}
            dateString = month_x + "/" + day_x;
            departDaButton.setText(dateString);
            Log.i("Selected Time - ",  dateString);
        }
    }


    public static long convertTime(String dateString, String timeString) {
        long epoch = 0;
        dateString = dateString + "/" + 2017;
        String time = dateString + " " + timeString+":00";
        Log.i(LOG_TAG, "convert: " + time);
        try {
            epoch = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(time).getTime() / 1000;
            Log.i(LOG_TAG, "convert Time: " + epoch);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return epoch;
    }

    public class CustomOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            timeOptions = parent.getItemAtPosition(pos).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }
}
