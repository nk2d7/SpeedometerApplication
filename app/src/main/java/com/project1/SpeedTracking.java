package com.project1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SpeedTracking extends AppCompatActivity implements OnClickListener, LocationListener{


    MyTTS myTts;
    SaveSharedPreferences preferences;
    Button backBtn;
    TextView speedTextView,xlatitudeTextView,yLongitudeTextView,alertTextView;
    ImageView backgroundImageView;

    double x, y;// latitude,longitude

    LocationManager locationManager;
    Location location;

    // The minimum distance to change Updates in meters
    private static long MIN_DISTANCE_CHANGE_FOR_UPDATES;
    // The minimum time between updates in milliseconds
    private static long MIN_TIME_BW_UPDATES;

    //speed limit
    double speedLimit;
    //our device's speed
    double speed;

    String timeStamp;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_speed_tracking);

        myTts = new MyTTS(getApplicationContext());
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        alertTextView=(TextView) findViewById(R.id.alertTextView);
        xlatitudeTextView=(TextView) findViewById(R.id.xlatitudeTextView);
        yLongitudeTextView=(TextView) findViewById(R.id.yLongitudeTextView);
        backBtn = (Button) findViewById(R.id.backBtn);
        backgroundImageView = (ImageView) findViewById(R.id.backgroundImageView);

        backBtn.setOnClickListener(this);

        MIN_TIME_BW_UPDATES = 1000;//milliseconds
        MIN_DISTANCE_CHANGE_FOR_UPDATES = 15;//m
        speedLimit=0;




        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //checking if gps is enabled or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS is not enabled !!
            Toast.makeText(this, "Please open device's gps and try again!",
                    Toast.LENGTH_LONG).show();
            //redirection to main menu(main activity)
            startActivity(new Intent(this, MainActivity.class));
            //close this activity
            finish();

        } else {




            alertTextView.setText(" Speed Limit : "+ preferences.getSpeedLimit(this).toString() );
            //calling the gps function in order to activate specific operations and display information
            gps();

        }


    }

    //actions based on which button we pressed
    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()) {
            case R.id.backBtn:

                //redirection to main menu(main activity)
                startActivity(new Intent(this, MainActivity.class));
                //close this activity
                finish();
                break;
        }
    }

    public void  gps(){

        //checking if the app is allowed to use the gps and if not,request user for permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},234);

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

    }

    //checks the current speed of the device and acts according the result
    public void  checkSpeed(double speed,double speedLimit){

        if(speed > speedLimit){

            //if the speed is above the current limit
            // the app notifies the user ,changes the background and saves the data in db
            backgroundImageView.setBackgroundResource(R.drawable.speedtrackingred);
            alertTextView.setText("You are going too fast ! Current Speed Limit is :  "+speedLimit+ " km/h");
            myTts.speak("You are going too fast ! Current Speed Limit is :  "+speedLimit+ " km per hour");
            Calendar theEnd = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            timeStamp = dateFormat.format(theEnd.getTime());

            DatabaseHelper dataBaseHelper = new DatabaseHelper(this);
            boolean done = dataBaseHelper.insertRecord(speed,x,y,timeStamp);

            if(done){

                Log.e("CheckDbInsertion","Insertion successfully done");

            }

        }else{


            backgroundImageView.setBackgroundResource(R.drawable.speedtracking);
            alertTextView.setText(" Speed Limit : "+ preferences.getSpeedLimit(this).toString() );


        }

    }


    @Override
    public void onLocationChanged(Location location) {

        //getting longitude and latitude
        x = location.getLatitude();
        y = location.getLongitude();

        //displaying longitude and latitude
       xlatitudeTextView.setText(String.valueOf(x));
       yLongitudeTextView.setText(String.valueOf(y));


        //getting device's speed
        //location.getSpeed() returns meters per second.Here we converted it to kilometers per hour

        speed=(location.getSpeed()*3600)/1000;

        //getting speed limit from shared preferences
        preferences.getSharedPreferences(this);
        try {

           speedLimit = Float.parseFloat( preferences.getSpeedLimit(this).toString());

            //displaying device's speed
           speedTextView.setText(speed+" km/h");

           checkSpeed(speed, speedLimit);


        } catch(NumberFormatException nfe) {

            System.out.println("Could not parse " + nfe);

        }

        //displaying device's speed
        speedTextView.setText(speed+" km/h");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void hideSystemUI() {
        // Enables sticky immersive mode.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    public void onResume(){
        super.onResume();
        hideSystemUI();
    }


}