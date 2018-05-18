package com.qc.stepcounter;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.text.DecimalFormat;


public class StepActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private Context context;
    private SensorManager sensorManager;
    private Sensor senAccelerometer;
    private double ca = 0;
    private double calories = 0;
    private int loggedsteps = 0;
    private int stepstakgensofar = 0;
    private Sensor senStepCounter;
    private ShareDialog shareDialog;
    TextView textViewsteps;
    static TextView textViewspeed;
    static TextView textViewdistance;
    TextView textViewcal;
    Button button;
    int step = 0;
    float[] gravity;
    final float timesconstant = 0.8f;
    float[] linear_acceleration;
    Location myService;
    static boolean current;
    LocationManager locationManager;
    Button start, pause, stop;
    static ProgressDialog locate;
    static int obSERVERint = 0;
    private Sensor senStepDetector;
    double num2;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Location.LocalBinder binder = (Location.LocalBinder) service;
            myService = binder.getService();
            current = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            current = false;

        }
    };

    void startService() {
        if (current)
            return;
        Intent intent = new Intent(getApplicationContext(), Location.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        current = true;
    }

    void endService() {
        if (current)
            return;
        unbindService(serviceConnection);
        current = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        button = findViewById(R.id.share);
        shareDialog = new ShareDialog(this);
        textViewsteps = findViewById(R.id.step_value);
        textViewspeed = findViewById(R.id.speed_value);
        textViewdistance = findViewById(R.id.distance_value);
        textViewcal = findViewById(R.id.calories_value);
        gravity = new float[3];
        linear_acceleration = new float[3];
        context = this;
        start = findViewById(R.id.start);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        button.setOnClickListener(this);

        if (savedInstanceState != null) {
            stepstakgensofar = savedInstanceState.getInt("A");
            ca = savedInstanceState.getDouble("B");
            textViewsteps.setText(String.valueOf(stepstakgensofar));
            textViewcal.setText(String.valueOf(ca));
        }
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        senStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        connectsensors();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION


                }, 1000);
            }
        }

    }




    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor s = event.sensor;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_STEP_COUNTER:
                if (loggedsteps < 1) {
                    loggedsteps = (int) event.values[0];
                }
                calories = (double) event.values[0] - loggedsteps;
                ca = calories / 150;
                textViewcal.setText(new DecimalFormat("#.##").format(ca));
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                stepstakgensofar++;
                textViewsteps.setText(String.valueOf(stepstakgensofar));
               String you= textViewsteps.getText().toString();
                 num2=Double.parseDouble(you);
                textViewdistance.setText(new DecimalFormat("#.###").format(num2/900));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                //////code by google
                gravity[0] = timesconstant * gravity[0] + (1 - timesconstant) * event.values[0];
                gravity[1] = timesconstant * gravity[1] + (1 - timesconstant) * event.values[1];
                gravity[2] = timesconstant * gravity[2] + (1 - timesconstant) * event.values[2];
/////////code by me
                linear_acceleration[0] = event.values[0] - gravity[0];
                linear_acceleration[1] = event.values[1] - gravity[1];
                linear_acceleration[2] = event.values[2] - gravity[2];
                double num = Math.sqrt((Math.pow(linear_acceleration[0], 2) + Math.pow(linear_acceleration[1], 2) + Math.pow(linear_acceleration[2], 2)));
                ///textViewspeed.setText(String.valueOf(num*5));
                break;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void connectsensors() {
        if (!checksensors()) {
            return;}
        sensorManager.registerListener(this, senStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, senStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }




    public boolean checksensors() {
        PackageManager pm = context.getPackageManager();
        int currentApiVersion = Build.VERSION.SDK_INT;
        return currentApiVersion >= 19
                && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
                && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
    }



    private void turnoffsensors() {
        if (!checksensors()) {
            return;
        }
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    void checkGps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("A", stepstakgensofar);
        outState.putDouble("B", ca);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stepstakgensofar = savedInstanceState.getInt("A");
        ca = savedInstanceState.getDouble("B");
        textViewsteps.setText(String.valueOf(stepstakgensofar));
        textViewcal.setText(String.valueOf(ca));
    }

    @Override
    public void onPause() {
        super.onPause();
        turnoffsensors();
    }

    @Override
    public void onResume() {
        super.onResume();
        connectsensors();
    }

    @Override
    public void onDestroy() {
        turnoffsensors();
        super.onDestroy();
        if (current)
            endService();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                checkGps();
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    return;
                }
                if (current == false)
                    startService();
                locate = new ProgressDialog(StepActivity.this);
                locate.setIndeterminate(true);
                locate.setCancelable(false);
                locate.setMessage("Getting Location");
                locate.show();
                start.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                pause.setText("Pause");
                stop.setVisibility(View.VISIBLE);
                break;

            case R.id.pause:
                if (pause.getText().toString().equalsIgnoreCase("pause")) {
                    pause.setText("Resume");
                    obSERVERint = 1;

                } else if (pause.getText().toString().equalsIgnoreCase("Resume")) {
                    checkGps();
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        return;
                    }
                    pause.setText("Pause");
                    obSERVERint = 0;

                }
                break;
            case R.id.stop:
                if (current)
                    endService();
                start.setVisibility(View.VISIBLE);
                pause.setText("Pause");
                pause.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                obSERVERint = 0;
                break;

            case R.id.share:
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Just wanna share my walking experience ")
                            .setContentDescription(
                                    step + "i have taken today!!!")
                            .setContentUrl(Uri.parse("http://moziru.com/images/feet-clipart-step-8.png"))
                            .setImageUrl(Uri.parse("http://moziru.com/images/feet-clipart-step-8.png"))
                            .setQuote("i walked so many steps today about :" + step)

                            .build();

                    shareDialog.show(linkContent);
                }
                break;


        }
    }
}
