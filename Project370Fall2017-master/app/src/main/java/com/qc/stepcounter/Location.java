package com.qc.stepcounter;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;

public class Location extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final long I = 1000 * 2;
    private static final long FT = 1000;
    LocationRequest request;
    GoogleApiClient apiClient;
    android.location.Location location, location1, location2;
    static double distance = 0;
    double speed;
    double to_feet_min = 196.85;
    public static double lat;
    public static double longe;
    int count = 0;

    public Location() {
    }

    private final IBinder localBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        createLocationRequest();
        apiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .build();
        apiClient.connect();
        return localBinder;
    }

    protected void createLocationRequest() {
        request = new LocationRequest();
        request.setInterval(I);
        request.setFastestInterval(FT);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    public static double getLat() {
        return lat;
    }

    public static double getLonge() {
        return longe;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    apiClient, request, this);
        } catch (SecurityException e) {
        }
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);
        distance = 0;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    public class LocalBinder extends Binder {

        public Location getService() {
            return Location.this;
        }


    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        StepActivity.locate.dismiss();
        lat = location.getLatitude();
        longe = location.getLongitude();
        this.location = location;
        speed = (location.getSpeed());
        DataChanged();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private void DataChanged() {
        if (StepActivity.obSERVERint == 0) {
            if (speed > 0.0) {
                StepActivity.textViewspeed.setText(new DecimalFormat("#.###").format(speed));
            }

        }

    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopLocationUpdates();
        if (apiClient.isConnected())
            apiClient.disconnect();
        location1 = null;
        location2 = null;
        distance = 0;
        return super.onUnbind(intent);
    }

}