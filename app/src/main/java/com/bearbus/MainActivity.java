package com.bearbus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.bearbus.Domain.BusStop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private GoogleMap map;
    private Spinner spinner;
    private Spinner dropSpinner;
    private Button btnRequest;
    private final String PARSE_APP_ID = "Hr5DPwQzhmzzST1sNzME8ssu3zaDxRZgtLO10Zxk";
    private final String PARSE_CLIENT_KEY = "49AgCaNyWzaFFCgHFPgS3NK0lEjTpLNPDDYBrswX";

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

        if (ParseApplication.haveInternet(getApplicationContext())) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("BusStop");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        BusStop bus;
                        ParseObject object;
                        ParseGeoPoint location;

                        for (int i = 0; i < parseObjects.size(); i++) {

                            object = parseObjects.get(i);
                            location = object.getParseGeoPoint("location");

                            bus = new BusStop(object.getString("stopName"), location.getLatitude(), location.getLongitude());

                            ParseApplication.stops.add(bus);
                            ParseApplication.stopTitles.add(object.getString("stopName"));

                            map.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude())));
                        }

                        final Spinner pickupSpinner = (Spinner) findViewById(R.id.pspinner);
                        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
                                android.R.layout.simple_spinner_item, ParseApplication.stopTitles);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        pickupSpinner.setAdapter(adapter);
                        pickupSpinner.setSelection(0);

                        pickupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {
                                pickupSpinner.setSelection(position, true);
                            }


                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });

                        final Spinner dropoffSpinner = (Spinner) findViewById(R.id.dspinner);
                        dropoffSpinner.setAdapter(adapter);
                        dropoffSpinner.setSelection(0);

                        dropoffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {
                                dropoffSpinner.setSelection(position, true);
                            }


                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });


                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

        PushService.setDefaultPushCallback(this, MainActivity.class);

        //button
        this.btnRequest = (Button) this.findViewById(R.id.request);
        this.btnRequest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            }
        });
    }
}
