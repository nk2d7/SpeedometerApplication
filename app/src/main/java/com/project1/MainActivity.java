package com.project1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button exitBtn,overSpeedLimitRecordsBtn,trackSpeedBtn,setSpeedLimitBtn,voiceBtn,helpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_main);

        exitBtn=(Button) findViewById(R.id.exitBtn);
        overSpeedLimitRecordsBtn=(Button) findViewById(R.id.overSpeedLimitRecordsBtn);
        trackSpeedBtn=(Button) findViewById(R.id.trackSpeedBtn);
        setSpeedLimitBtn=(Button) findViewById(R.id.setSpeedLimitBtn);
        voiceBtn=(Button) findViewById(R.id.voiceBtn);
        helpBtn=(Button) findViewById(R.id.helpBtn);

        exitBtn.setOnClickListener(this);
        overSpeedLimitRecordsBtn.setOnClickListener(this);
        trackSpeedBtn.setOnClickListener(this);
        setSpeedLimitBtn.setOnClickListener(this);
        voiceBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);

    }

    //actions based on which button we pressed
    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()){
            case R.id.trackSpeedBtn:
                startActivity(new Intent(this, SpeedTracking.class));
                finish();
                break;
            case R.id.overSpeedLimitRecordsBtn:
                startActivity(new Intent(this, OverLimitSpeedRecords.class));
                finish();
                break;
            case R.id.setSpeedLimitBtn:
                startActivity(new Intent(this, SetSpeedLimitActivity.class));
                finish();
                break;
            case R.id.voiceBtn:
                vcBtn();

                break;
            case R.id.helpBtn:

                startActivity(new Intent(this,viewerPdf.class));
                break;
            case R.id.exitBtn:
                finish();
                System.exit(0);
                break;
        }

    }

    //voice command btn
    public void  vcBtn(){
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please say something!");

        startActivityForResult(intent,742);
    }


    //actions based on the voice command we gave
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

        if (data!=null && resultCode==RESULT_OK){
            ArrayList<String>  matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.contains("tracking")){
                startActivity(new Intent(this, SpeedTracking.class));
                finish();
            }else if (matches.contains("speed limit records")){
                startActivity(new Intent(this, OverLimitSpeedRecords.class));
                finish();
            }else if (matches.contains("set speed limit")){
                startActivity(new Intent(this, SetSpeedLimitActivity.class));
                finish();
            }else if(matches.contains("exit")){
                finish();
                System.exit(0);
            }else if(matches.contains("help")){
                startActivity(new Intent(this,viewerPdf.class));
            }

        }

    }




    public void onBackPressed() {

        finish();
        System.exit(0);
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