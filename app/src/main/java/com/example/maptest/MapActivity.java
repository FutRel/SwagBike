package com.example.maptest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.example.maptest.databinding.ActivityMapsBinding;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private LocationManager locationManager;
    Button follow;
    SensorManager sensorManager;
    Sensor sensorAccelerometer;
    private long changeStyle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        follow = findViewById(R.id.follow);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        final long[] t = {0};
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float var = ((((int)sensorEvent.values[0] + 1) / 15) * 15);
            if(t[0] % 40 == 0) {
                myMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder(myMap
                                .getCameraPosition())
                        .bearing(var)
                        .build()));
                try {Thread.sleep(500);} catch (InterruptedException e) {}
            }
            t[0]++;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };
    LocationListener followListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            try {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, myMap.getCameraPosition().zoom));
            }catch (Exception ex){
                LatLng userLocation = new LatLng(0, 0);
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, myMap.getCameraPosition().zoom));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            try {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
            }catch (Exception ex){
                LatLng userLocation = new LatLng(0, 0);
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        UiSettings uiSettings = myMap.getUiSettings();
        myMap.setBuildingsEnabled(false);
        myMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.my_style));

        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setIndoorLevelPickerEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);

        uiSettings.setZoomGesturesEnabled(false);
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) return;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                300, 1, followListener);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try {
            LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            myMap.clear();
            myMap.addMarker(new MarkerOptions().position(userLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
        }catch (Exception ex){
            LatLng userLocation = new LatLng(0, 0);
            myMap.clear();
            myMap.addMarker(new MarkerOptions().position(userLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
        }
        sensorManager.registerListener(sensorEventListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void onClick(View view) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
    }
    public void onClickplus(View view) {myMap.animateCamera(CameraUpdateFactory.zoomIn());}
    public void onClickminus(View view) {myMap.animateCamera(CameraUpdateFactory.zoomOut());}
    public void follow(View view) {
        UiSettings uiSettings = myMap.getUiSettings();
        if (follow.getText().toString().equals("follow")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, myMap.getCameraPosition().zoom));

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 1, followListener);
            locationManager.removeUpdates(locationListener);
            sensorManager.registerListener(sensorEventListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);

            follow.setText("unfollow");
            uiSettings.setZoomGesturesEnabled(false);
            uiSettings.setScrollGesturesEnabled(false);
            uiSettings.setRotateGesturesEnabled(false);
            uiSettings.setTiltGesturesEnabled(false);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 2, locationListener);
            locationManager.removeUpdates(followListener);
            sensorManager.unregisterListener(sensorEventListener);

            follow.setText("follow");
            uiSettings.setZoomGesturesEnabled(true);
            uiSettings.setScrollGesturesEnabled(true);
            uiSettings.setRotateGesturesEnabled(true);
            uiSettings.setTiltGesturesEnabled(true);

            myMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder(myMap
                            .getCameraPosition())
                    .bearing(0)
                    .build()));
        }
    }
    public void change(View view){
        if (changeStyle % 2 == 1)myMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.my_style));
        else myMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.bl_wh));
        changeStyle++;

    }
}