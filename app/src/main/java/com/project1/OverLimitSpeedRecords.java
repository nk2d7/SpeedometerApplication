package com.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OverLimitSpeedRecords extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button backBtn;
    ListView recordsList;
    Spinner optionsSpinner;
    ArrayAdapter<CharSequence> optionsAdapter;
    ArrayAdapter<String> recordsAdapter;
    ArrayList<String> recordsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_over_limit_speed_records2);


        backBtn=(Button) findViewById(R.id.backBtn);
        recordsList=(ListView) findViewById(R.id.recordsList);
        optionsSpinner=(Spinner) findViewById(R.id.optionsSpinner);

        optionsAdapter=ArrayAdapter.createFromResource(this,R.array.options, android.R.layout.simple_spinner_item);
        optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(optionsAdapter);


        backBtn.setOnClickListener(this);
        optionsSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()){
            case R.id.backBtn:

                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }

    }

    //options based on what option we chose from the adaptor
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();
        DatabaseHelper dataBaseHelper = new DatabaseHelper(this);
        if(choice.equals("All records")){

            //recordsArrayList.clear();
            recordsArrayList = dataBaseHelper.getRecords();
            recordsAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,recordsArrayList);
            recordsList.setAdapter(recordsAdapter);

        }else{

            recordsArrayList.clear();
            recordsArrayList = dataBaseHelper.getRecordsFrom7DaysAgo();
            recordsAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,recordsArrayList);
            recordsList.setAdapter(recordsAdapter);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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