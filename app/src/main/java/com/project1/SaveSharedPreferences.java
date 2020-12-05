package com.project1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {

    static final String SPEED_LIMIT="60";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    //saving the new speed limit
    public static void setSpeedLimit(Context ctx,String limit){

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SPEED_LIMIT,limit);
        editor.commit();

    }

    //getting the current speed limit
    public static String getSpeedLimit(Context ctx)
    {

        return getSharedPreferences(ctx).getString(SPEED_LIMIT, "60");

    }

}