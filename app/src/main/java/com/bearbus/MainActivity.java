package com.bearbus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


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
    }
}
