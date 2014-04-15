package com.bearbus;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.bearbus.Domain.Bus;
import com.bearbus.Domain.BusStop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    private GoogleMap map;
    private Spinner pickupSpinner;
    private Spinner dropSpinner;
    private Button btnRequest;
    private final String PARSE_APP_ID = "Hr5DPwQzhmzzST1sNzME8ssu3zaDxRZgtLO10Zxk";
    private final String PARSE_CLIENT_KEY = "49AgCaNyWzaFFCgHFPgS3NK0lEjTpLNPDDYBrswX";

    private Marker[] markers;
    private BusStop pickupLocation;
    private BusStop dropoffLocation;

    private Handler mHandler;
    private static final int REQUEST_OK = 1;
    private static final int REQUEST_DENIED = -1;
    private final long[] vibrationPattern = {100l};

    private static final long BUS_UPDATE_INTERVAl = 2500l;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();

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

        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    try {

                        ParseQuery query = ParseQuery.getQuery("Bus");
                        List<ParseObject> results = query.find();

                        if (markers == null)
                            markers = new Marker[results.size()];

                        Bus bus;
                        ParseObject busObject;
                        ParseGeoPoint location;
                        for ( int i = 0; i < results.size(); i++ ) {
                            busObject = results.get(i);
                            location = busObject.getParseGeoPoint("location");
//                            bus = new Bus(busObject.getObjectId(), busObject.getString("name"), location.getLatitude(), location.getLongitude(), busObject.getString("currentStop"), busObject.getString("nextStop"));

//                            map.clear();
//
//                            map.addMarker(new MarkerOptions()
//                                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
//                                    .title(bus.name));

                            UpdateBusLocations runnable = new UpdateBusLocations();
                            Bundle args = new Bundle();
                            args.putInt(UpdateBusLocations.INDEX, i);
                            args.putString(UpdateBusLocations.BUS_NAME, busObject.getString("name"));
                            args.putDouble(UpdateBusLocations.LATITUDE, location.getLatitude());
                            args.putDouble(UpdateBusLocations.LONGITUDE, location.getLongitude());
                            runnable.args = args;

                            mHandler.post(runnable);
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }, 0, BUS_UPDATE_INTERVAl);
        }


        PushService.setDefaultPushCallback(this, MainActivity.class);

        //button
        this.btnRequest = (Button) this.findViewById(R.id.request);
        this.btnRequest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ArrayList<String> IDs = new ArrayList<String>();
                IDs.add(pickupLocation.id);
                IDs.add(dropoffLocation.id);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Routes");
//                query.whereEqualTo("dayOfWeek", 1);
                query.findInBackground(new FindCallback<ParseObject>() {

                    ArrayList<ParseObject> possibleBuses = new ArrayList<ParseObject>();

                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {

                        JSONArray stopsForRoute;
                        String stop;
                        int count;
                        boolean hasPickupLocation;
                        boolean hasDropoffLocation;
                        boolean hasValidRoute = false;
                        Bus selectedBus;

                        for (int i = 0; i < parseObjects.size(); i++) {

                            ParseObject object = parseObjects.get(i);

                            if (object.getInt("dayOfWeek") == (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1)) {

                                count = 0;
                                hasPickupLocation = false;
                                hasDropoffLocation = false;
                                stopsForRoute = object.getJSONArray("stops");
                                Log.d("parseObject", stopsForRoute.toString());

                                try {
                                    while (count < stopsForRoute.length()) {
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

                                    ParseQuery busQuery = ParseQuery.getQuery("Bus");

                                    try {
                                        hasValidRoute = true;

                                        ParseObject busObject = busQuery.get(object.getString("busID"));

                                        ParseGeoPoint location = busObject.getParseGeoPoint("location");

                                        selectedBus = new Bus(busObject.getObjectId(), busObject.getString("name"), location.getLatitude(), location.getLongitude(), busObject.getString("currentStop"), busObject.getString("nextStop"));

                                        JSONArray operationTime = object.getJSONArray("time");

                                        try {

                                            boolean beforeStartTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < operationTime.getDouble(0);
                                            boolean afterEndTime = operationTime.getDouble(1) < Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

                                            if (beforeStartTime || afterEndTime) {
                                                Toast.makeText(MainActivity.this, "Inactive route", Toast.LENGTH_SHORT).show();
                                            } else {

                                                submitRequestToBus(selectedBus.id);

                                                Toast.makeText(MainActivity.this, "Selected Bus: " + selectedBus.name, Toast.LENGTH_SHORT).show();
                                                break;
                                            }

                                        } catch (JSONException exception) {

                                        }

                                    } catch (ParseException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            }
                        }

                        if (!hasValidRoute)
                            Toast.makeText(MainActivity.this, "Invalid or inactive route.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseApplication.isInForeground = true;
    }

    private void submitRequestToBus(String identifier) {

        final ParseObject request = new ParseObject("Request");
        request.put("dropoffLocation", dropoffLocation.id);
        request.put("pickupLocation", pickupLocation.id);
        request.put("targetBusID", identifier);

        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    final Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {

                            ParseQuery query = ParseQuery.getQuery("Request");
                            try {

                                ParseObject requestObject = query.get(request.getObjectId());
                                int status = requestObject.getInt("status");

                                if (status == REQUEST_OK) {

                                    if (ParseApplication.isInForeground) {
                                        showSuccessDialog();
                                    } else {
                                        sendSuccessNotification();
                                    }

                                    timer.cancel();
                                    timer.purge();

                                } else if (status == REQUEST_DENIED) {
                                    if (ParseApplication.isInForeground) {
                                        showDeniedDialog();
                                    } else {
                                        sendDeniedNotification();
                                    }

                                    timer.cancel();
                                    timer.purge();
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }, 0, 2500);

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ParseApplication.isInForeground = false;
    }

    private void showSuccessDialog() {
        mHandler.post(sucessDialogRunnable);
    }

    final Runnable sucessDialogRunnable = new Runnable() {
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Success")
                    .setMessage("Your request has been approved for transportation from " + pickupLocation.name + " to " + dropoffLocation.name + ".")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builder.create().show();
        }
    };

    private void sendSuccessNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Bear Bus")
                        .setContentText("Your request has been approved.")
                        .setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager.notify(1, mBuilder.build());

    }

    private void showDeniedDialog() {
        mHandler.post(deniedDialogRunnable);
    }

    final Runnable deniedDialogRunnable = new Runnable() {
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Denied")
                    .setMessage("We apologize. Your request for transportation from " + pickupLocation.name + " to " + dropoffLocation.name + " has been denied by the " + pickupLocation.name + " driver.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builder.create().show();
        }
    };

    private void sendDeniedNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Bear Bus")
                        .setContentText("Your request has been denied.")
                        .setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager.notify(1, mBuilder.build());
    }

    private class UpdateBusLocations implements Runnable {

        public static final String INDEX = "index";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String BUS_NAME = "bus";

        public Bundle args;

        @Override
        public void run() {
            Log.d("UpdateBusLocations", "Updating map location of " + args.getString(BUS_NAME));

            if (args != null) {

                if (markers[args.getInt(INDEX)] == null) {
                    markers[args.getInt(INDEX)] = map.addMarker(new MarkerOptions()
                            .position(new LatLng(args.getDouble(LATITUDE), args.getDouble(LONGITUDE)))
                            .title(args.getString(BUS_NAME))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                } else {
                    markers[args.getInt(INDEX)].setPosition(new LatLng(args.getDouble(LATITUDE), args.getDouble(LONGITUDE)));
                }
            }
        }
    }
}
