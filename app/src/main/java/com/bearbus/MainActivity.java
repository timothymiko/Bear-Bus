package com.bearbus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import com.parse.Parse;
import com.parse.PushService;
import android.widget.TextView;
import com.bearbus.R;





public class MainActivity extends ActionBarActivity {

    private final String PARSE_APP_ID = "Hr5DPwQzhmzzST1sNzME8ssu3zaDxRZgtLO10Zxk";
    private final String PARSE_CLIENT_KEY = "49AgCaNyWzaFFCgHFPgS3NK0lEjTpLNPDDYBrswX";
    ArrayList stops= new ArrayList();
    private Spinner spinner;
    private Spinner dropSpinner;
    private Button btnRequest;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(this, PARSE_APP_ID, PARSE_APP_ID);
        PushService.setDefaultPushCallback(this, MainActivity.class);
        //button
        this.btnRequest = (Button) this.findViewById(R.id.request);
        this.btnRequest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            }
        });

        List<String> SpinnerArray = new ArrayList<String>();
        SpinnerArray.add("Parking Lot 3");
        SpinnerArray.add("Parking Lot 11");
        SpinnerArray.add("Parking Lot 35");
        SpinnerArray.add("Recreation Center");
        SpinnerArray.add("Meadow Brook Road");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, SpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.pspinner);
        spinner.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, SpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner dspinner = (Spinner) findViewById(R.id.dspinner);
        dropSpinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                Object item = arg0.getItemAtPosition(arg2);
                if (item!=null) {
                    Toast.makeText(MainActivity.this, item.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, "Selected",
                        Toast.LENGTH_SHORT).show();

            }





            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/



    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.pspinner);
        dropSpinner = (Spinner) findViewById(R.id.dspinner);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }
}
