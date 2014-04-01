package com.bearbus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends ActionBarActivity {

    //private GoogleMap map;
    ArrayList stops= new ArrayList();
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
        /*map = ((SupportMapFragment) getSupportFragmentManager()
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
                .title("O'rena/Recreation Center"));*/

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
        SpinnerArray.add("Parking Lot 35/27");
        SpinnerArray.add("O'rena/Recreation Center");
        SpinnerArray.add("Meadow Brook Road");
        SpinnerArray.add("Human Health Building");
        SpinnerArray.add("Hamlin Circle");
        SpinnerArray.add("Ann V. Nicholson 4000's");
        SpinnerArray.add("Oakland Center/Wilson");
        SpinnerArray.add("Extended Stay America (University Drive)");
        SpinnerArray.add("Pawley/Varner");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, SpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner[] spinner = {(Spinner) findViewById(R.id.pspinner)};
        spinner[0].setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, SpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner2 = (Spinner) findViewById(R.id.dspinner);
        spinner2.setAdapter(adapter1);

        spinner[0].setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                Object item = arg0.getItemAtPosition(arg2);
                if (item != null) {
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

        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

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

            public void addListenerOnSpinnerItemSelection() {
                spinner[0] = (Spinner) findViewById(R.id.pspinner);
                dropSpinner = (Spinner) findViewById(R.id.dspinner);
                spinner[0].setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            }
        });
    }
}
