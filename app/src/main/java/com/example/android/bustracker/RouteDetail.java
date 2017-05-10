package com.example.android.bustracker;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bustracker.directions.Bound;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yishuyan on 5/4/17.
 */

public class RouteDetail extends FragmentActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    public static final String LOG_TAG = RouteDetail.class.getSimpleName();
    ListView listView;

    StepsAdapter adapter;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final LatLng mDefaultLocation = new LatLng(40.453901, -79.943153);
    private static final int DEFAULT_ZOOM = 12;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private boolean mLocationPermissionGranted;
    private Location mLastLocation;
    private Marker marker;
    private Route route;
    private Leg leg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_detail);
        Intent intent = getIntent();
        route = (Route) intent.getParcelableExtra("currentRoute");
        leg = route.getLegs().get(0);

//        TextView departView = (TextView) findViewById(R.id.estimated_depart);
//        departView.setText(leg.getDeparture_time().getText());
//
//        TextView arrivalView = (TextView) findViewById(R.id.estimated_arrive);
//        arrivalView.setText(leg.getArrival_time().getText());
//        TextView distanceView = (TextView) findViewById(R.id.total_distance);
//        distanceView.setText(leg.getDistance().getText());

        TextView durationView = (TextView) findViewById(R.id.total_time);
        durationView.setText(leg.getDuration().getText());

        listView = (ListView) findViewById(R.id.step_view);
        List<Step> stepList = leg.getSteps();
        adapter = new StepsAdapter(this, stepList);
        listView.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapdir);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        //mGoogleApiClient.connect();
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
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        plotDirections();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(LOG_TAG, "Location services connection failed with code " +
                    connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
            } else {
                //updateMap();
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        //your code here
        Log.w(LOG_TAG, "location changed");
        mLastLocation = location;
        //updateMap();
    }

    public void plotDirections() {
        // show start and destination
        mMap.addMarker(new MarkerOptions()
                .position(leg.getStart_location().toLatLng())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        );
        mMap.addMarker(new MarkerOptions()
                .position(leg.getEnd_location().toLatLng())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );
        // show directions
        PolylineOptions busOptions = new PolylineOptions();
        List<PatternItem> patternItems = new ArrayList<>();
        PatternItem dot = new Dot();
        patternItems.add(dot);
        for (Step step : leg.getSteps()) {
            if (step.getTravel_mode().equals("WALKING")) {
                List<LatLng> latLngs = PolyUtil.decode(step.getPolyline().getPoints());
                mMap.addPolyline(new PolylineOptions()
                        .addAll(latLngs)
                        .color(Color.BLUE)
                        .width(15)
                        .pattern(patternItems));
            } else if (step.getTravel_mode().equals("TRANSIT")) {
                List<LatLng> latLngs = PolyUtil.decode(step.getPolyline().getPoints());
                mMap.addPolyline(new PolylineOptions()
                        .addAll(latLngs)
                        .color(Color.BLUE)
                        .width(15));
            }
        }
        // set bounds
        Bound bounds = route.getBounds();
        LatLngBounds latLngBounds = new LatLngBounds(
                bounds.getSouthwest().toLatLng(),
                bounds.getNortheast().toLatLng());
        int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.5);
        int width = getResources().getDisplayMetrics().widthPixels;
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, height, width, 100));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100));
    }
}
