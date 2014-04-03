package com.bearbus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private GoogleMap map;
    private Spinner pickupSpinner;
    private Spinner dropSpinner;
    private Button btnRequest;
    private final String PARSE_APP_ID = "Hr5DPwQzhmzzST1sNzME8ssu3zaDxRZgtLO10Zxk";
    private final String PARSE_CLIENT_KEY = "49AgCaNyWzaFFCgHFPgS3NK0lEjTpLNPDDYBrswX";

    private BusStop pickupLocation;
    private BusStop dropoffLocation;

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

                        bus = new BusStop(object.getObjectId(), object.getString("stopName"), location.getLatitude(), location.getLongitude());

                        ParseApplication.stops.add(bus);
                        ParseApplication.stopTitles.add(object.getString("stopName"));

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .title(bus.name));
                    }

                    pickupSpinner = (Spinner) findViewById(R.id.pspinner);
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
                            pickupLocation = ParseApplication.stops.get(position);
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
                            dropoffLocation = ParseApplication.stops.get(position);
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


        PushService.setDefaultPushCallback(this, MainActivity.class);

        //button
        this.btnRequest = (Button) this.findViewById(R.id.request);
        this.btnRequest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ArrayList<String> IDs = new ArrayList<String>();
                IDs.add(pickupLocation.id);
                IDs.add(dropoffLocation.id);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Routes");
                query.whereEqualTo("dayOfWeek", Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                query.findInBackground(new FindCallback<ParseObject>() {

                    ArrayList<ParseObject> possibleBuses = new ArrayList<ParseObject>();

                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {

                        JSONArray stopsForRoute;
                        String stop;
                        int count;
                        boolean hasPickupLocation;
                        boolean hasDropoffLocation;

                        for (ParseObject object : parseObjects) {
                            count = 0;
                            hasPickupLocation = false;
                            hasDropoffLocation = false;
                            stopsForRoute = object.getJSONArray("stops");

                            try {
                                while (true) {
                                    stop = stopsForRoute.getString(count);
                                    if (stop.equals(pickupLocation.id))
                                        hasPickupLocation = true;
                                    else if (stop.equals(dropoffLocation.id))
                                        hasDropoffLocation = true;
                                    count++;
                                }

                            } catch (JSONException exception) {
                                exception.printStackTrace();
                            }

                            if (hasPickupLocation && hasDropoffLocation) {
                                ParseQuery query = ParseQuery.getQuery("Bus");
                                try {
                                    possibleBuses.add(query.get(object.getString("busID")));
                                } catch (ParseException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }

                        double shortestLatitudeDelta = 999999999;
                        double shortestLongitudeDelta = 999999999;
                        String shortestBusID = "";


                        for (ParseObject object : parseObjects) {
                            try {

                                ParseGeoPoint curStopLocation = ParseQuery.getQuery("BusStop").get(object.getString("currentStop")).getParseGeoPoint("location");
//                                ParseGeoPoint nextStopLocation = ParseQuery.getQuery("BusStop").get(object.getString("nextStop")).getParseGeoPoint("location");

                                double latitudeDelta = curStopLocation.getLatitude() - pickupLocation.latitude;
                                double longitudeDelta = curStopLocation.getLatitude() - pickupLocation.longitude;

                                boolean isCurStopClosest = (latitudeDelta < shortestLatitudeDelta) && (longitudeDelta < shortestLongitudeDelta);

                                if (isCurStopClosest) {
                                    shortestLatitudeDelta = latitudeDelta;
                                    shortestLongitudeDelta = longitudeDelta;
                                    shortestBusID = object.getObjectId();
                                }

                            } catch (ParseException e3) {
                                e3.printStackTrace();
                            }
                        }

                        try {
                            ParseObject selectedBus = ParseQuery.getQuery("Bus").get(shortestBusID);
                        } catch (ParseException e4) {
                            e4.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error determing bus!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }
}
