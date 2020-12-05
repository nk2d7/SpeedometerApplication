package com.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.github.barteksc.pdfviewer.PDFView;

//class that enables the app to show pdf files we have stored in assets folder
public class viewerPdf extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_viewer_pdf);

        pdfView=findViewById(R.id.pdfView);
        pdfView.fromAsset("usermanual1.pdf").load();
    }



    @Override
    public void onBackPressed() {
        finish();
        // System.exit(0);
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