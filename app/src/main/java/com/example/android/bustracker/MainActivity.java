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
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.bustracker.directions.Direction;
import com.example.android.bustracker.directions.Leg;
import com.example.android.bustracker.directions.Route;
import com.example.android.bustracker.directions.Step;
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
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static String USGS_REQUEST_URL = "https://maps.googleapis.com/maps/api/directions/json?key=AIzaSyAEBBlM76YfcI7of-9Q5UkTM2O_NBjGaNw";


    BusInfoAdapter adapter;
    private EditText start_name, end_name, stop_id;
    private String typeName, url;
    private TextView emptyTextView;
    ListView listView;

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
        //emptyTextView = (TextView)findViewById(R.id.empty_view);
        // listView.setEmptyView(emptyTextView);
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

    public void plotDirections(Direction direction) {
        PolylineOptions busOptions = new PolylineOptions();
        List<PatternItem> patternItems = new ArrayList<>();
        PatternItem dot = new Dot();
        patternItems.add(dot);
        if (direction.getStatus().equals("OK")) {
            Route route = direction.getRoutes().get(0);
            for (Leg leg : route.getLegs()) {
                for (Step step : leg.getSteps()) {
                    if (step.getTravel_mode().equals("WALKING")) {
                        List<LatLng> latLngs = PolyUtil.decode(step.getPolyline().getPoints());
                        mMap.addPolyline(new PolylineOptions()
                                .addAll(latLngs)
                                .color(Color.BLUE)
                                .width(10)
                                .pattern(patternItems));
                    }
                    else if (step.getTravel_mode().equals("TRANSIT")) {
                        List<LatLng> latLngs = PolyUtil.decode(step.getPolyline().getPoints());
                        mMap.addPolyline(new PolylineOptions()
                                .addAll(latLngs)
                                .color(Color.BLUE)
                                .width(10));
                    }
                }
            }
        }
    }

    // Text input and search bar

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private class DyfiAsyncTask extends AsyncTask<String,Void,Direction> {
        @Override
        protected Direction doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            Direction direction = QueryUtils.extractFeatureFromJson(urls[0]);
//            MyAPIClient myClient = new MyAPIClient();
//            Direction direction = myClient.getDirections();

            return direction;
        }

        @Override
        protected void onPostExecute(Direction direction) {
            List<Route> routes = direction.getRoutes();
            updateUi(routes);
            plotDirections(direction);
        }
    }

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
    private void updateUi(final List<Route> lists) {
        adapter = new BusInfoAdapter(this, lists);
        listView.setAdapter(adapter);
        // Check one route, and check the detail route information in another screen.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, RouteDetail.class);
                if (lists == null) {
                    Log.i(LOG_TAG, "buses is null");
                }
                Log.i(LOG_TAG, "buses size: " + lists.size());
                Route clickedRoute = lists.get(position);
                Leg currentLeg = clickedRoute.getLegs().get(0);
                intent.putExtra("legDetail", currentLeg);
                startActivity(intent);
            }
        });
    }

    public static boolean isConnectedNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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
