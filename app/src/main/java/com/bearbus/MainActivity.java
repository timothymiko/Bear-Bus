package com.bearbus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends ActionBarActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a handle to the Map Fragment
        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        // Set map options
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);

        // Center map on Oakland University
        LatLng oaklandUniversity = new LatLng(42.673776, -83.207247);
        CameraPosition newPosition = CameraPosition.fromLatLngZoom(oaklandUniversity, 14.0f);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(newPosition));

        // Markers for Northern On-Campus
        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.677324, -83.218395))
                .title("Human Health Building"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.677356, -83.214662))
                .title("Hamlin Circle"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.67891, -83.211062))
                .title("P11 North Side"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.679576, -83.210498))
                .title("Ann V. Nicholson 4000's"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.675285, -83.218084))
                .title("Oakland Center/Wilson"));

        // Markers for Central On-Campus
        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.67639, -83.210573))
                .title("P11 South Side"));

        // Markers for Southern On-Campus
        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.672848, -83.22473))
                .title("Extended Stay America (University Drive)"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.668888, -83.215573))
                .title("P35/37"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.672379,-83.212354))
                .title("Pawley/Varner"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.674077, -83.213969))
                .title("O'rena/Recreation Center"));
    }
}
