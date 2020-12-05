package com.project1;

import android.view.View;

//class that prevents the user form clicking a button two times or more in a row
public class Utils {

    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            public void run() {
                view.setEnabled(true);
            }
        }, 500);
    }
}