package com.chuntingyu.weather.tools.coredata;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper {
    private final String TAG = "LocationHelper";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double lat;
    private double lon;
    private LocationHelper locationHelper = null;

    public LocationHelper(Context context) {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    Log.e(TAG, "Lat = " + location.getLatitude() + ", Lon = " + location.getLongitude());
                }
            }
        });
    }

    private LocationHelper() {
        if (locationHelper != null) {
            
        }
    }

    public void getInstance() {

    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }
}
