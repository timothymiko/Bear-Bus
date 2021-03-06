package com.bearbus;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bearbus.Domain.BusStop;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by timothymiko on 3/19/14.
 */


public class ParseApplication extends Application {

    private final String PARSE_APP_ID = "Hr5DPwQzhmzzST1sNzME8ssu3zaDxRZgtLO10Zxk";
    private final String PARSE_CLIENT_KEY = "49AgCaNyWzaFFCgHFPgS3NK0lEjTpLNPDDYBrswX";

    public static ArrayList<BusStop> stops = new ArrayList<BusStop>(); // Array of stops
    public static ArrayList<String> stopTitles = new ArrayList<String>(); // Array of stop titles

    public static boolean isInForeground = true;

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_KEY);


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

    }

    public static boolean haveInternet(Context ctx) {

        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable internet while roaming, just return false
            return false;
        }
        return true;
    }
}
