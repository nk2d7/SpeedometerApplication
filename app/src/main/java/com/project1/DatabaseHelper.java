package com.project1;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper{



    public static final String DATABASE_NAME = "Records.db";
    public static final String CONTACTS_TABLE_NAME="speedRecords";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_SPEED = "speed";
    public static final String CONTACTS_COLUMN_LATITUDE = "latitude"; //x
    public static final String CONTACTS_COLUMN_LONGITUDE = "longitude"; //y
    public static final String CONTACTS_COLUMN_TIMESTAMP = "timestamp";

    private SQLiteDatabase myDatabase;
    private final Context myContext;
    Cursor c;


    public DatabaseHelper(@Nullable Context context) {

        super(context,DATABASE_NAME , null, 1);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table speedRecords " +
                        "(id INTEGER primary key,speed REAL,latitude REAL,longitude REAL,timestamp TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }


    //returns the number of rows a table has
    public int getRowsCount(String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM "+tableName;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;

    }

    //insert a new record on db(auto increment id we get from method getRowsCount,x,y,data+time)
    public boolean insertRecord(double speed,double latitude,double longitude,String timestamp){

        SQLiteDatabase db = this.getWritableDatabase();
        int count = getRowsCount("speedRecords")+1;//finding the id value of our new record

        String sql="INSERT INTO speedRecords (id,speed,latitude,longitude, timestamp)"+"VALUES ('"+count+"','"+speed+"','"+latitude+"','"+longitude+"','"+timestamp+"')";

        try
        {
            db.execSQL(sql);
            return true;

        }
        catch (Exception e)
        {
            Log.e("ERROR", e.toString());
            return  false;
        }

    }

   //getting all the records that exist in db
    public ArrayList<String> getRecords(){

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> myStringValues = new ArrayList<String>();

        Cursor  cursor = db.rawQuery("select * from speedRecords",null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = "DateTime: "+cursor.getString(cursor.getColumnIndex("timestamp"))+",Speed: "+cursor.getString(cursor.getColumnIndex("speed"))+",Latitude: "+
                        cursor.getString(cursor.getColumnIndex("latitude"))+",Longitude: " + cursor.getString(cursor.getColumnIndex("longitude"));

                myStringValues.add(name);
                cursor.moveToNext();
            }
        }

        return myStringValues;
    }


    public ArrayList<String> getRecordsFrom7DaysAgo(){

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> myStringValues = new ArrayList<String>();


        Calendar theEnd = Calendar.getInstance();
        Calendar theStart = (Calendar) theEnd.clone();

        theStart.add(Calendar.DAY_OF_MONTH, -3);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String start = dateFormat.format(theStart.getTime());
        String end = dateFormat.format(theEnd.getTime());

        //  date boundaries in TEXT format

        Cursor  cursor=db.rawQuery("select * from speedRecords where timestamp BETWEEN '"+start+"'"+" AND '"+end+"'",null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = "DateTime: "+cursor.getString(cursor.getColumnIndex("timestamp"))+",Speed: "+cursor.getString(cursor.getColumnIndex("speed"))+",Latitude: "+
                        cursor.getString(cursor.getColumnIndex("latitude"))+",Longitude: " + cursor.getString(cursor.getColumnIndex("longitude"));

                myStringValues.add(name);
                cursor.moveToNext();
            }
        }

        return myStringValues;
    }
}
