package com.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetSpeedLimitActivity extends AppCompatActivity implements View.OnClickListener {

    MyTTS myTts;
    EditText speedLimitEditText;
    Button setSpeedLimitBtn,backBtn;
    TextView currentSpeedLimitTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_set_speed_limit);

        myTts = new MyTTS(getApplicationContext());
        speedLimitEditText=(EditText) findViewById(R.id.speedLimitEditText);
        setSpeedLimitBtn=(Button) findViewById(R.id.setSpeedLimitBtn);
        backBtn=(Button) findViewById(R.id.backBtn);
        currentSpeedLimitTextView=(TextView) findViewById(R.id.currentSpeedLimitTextView);


        Toast.makeText(this,"The new speed limit must be an integer above 0 !",Toast.LENGTH_LONG).show();

        //getting current speed limit
        getCurrentSpeedLimit();

        setSpeedLimitBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()){
            case R.id.setSpeedLimitBtn:

                setSpeedLimit();

                break;
            case R.id.backBtn:

                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }

    }

    //gets current speed limit from shared preferences and showcases it in screen
    public  void getCurrentSpeedLimit(){

         currentSpeedLimitTextView.setText(SaveSharedPreferences.getSpeedLimit(this)+" km/h ");

    }


    //sets a new speed limit in sharedPreferences based on what the user entered
    public void setSpeedLimit(){

        String newLimit=speedLimitEditText.getText().toString();

        if (newLimit.equals(null)){

            Toast.makeText(this,"Please enter a new speed limit first!",Toast.LENGTH_LONG).show();

        }else{

            SaveSharedPreferences.setSpeedLimit(this,newLimit);
            myTts.speak("New Speed Limit is :  "+newLimit+ " km per hour");
            getCurrentSpeedLimit();
            speedLimitEditText.getText().clear();


        }

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