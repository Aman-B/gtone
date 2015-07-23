package com.bewtechnologies.gtone;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * Created by MAHE on 7/14/2015.
 */
public class usersetting extends MainActivity{

    public TextView selected_place;
    private String place_name;
    private LinearLayout usrst;
    private TextView gotdata;
    private Button delDb;

    //for db
    private SQLiteDatabase gtone;
    private LocationDBHelper dbHelper;
    private String s;



    //for checking settings
    Button chkset;


    //for check match
    private double lat;
     private double lang;


    //for phone-mode
    AudioManager adm;

    //coordinates in db
    static double dblatitude;
    static double dblongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView sv = (ScrollView)findViewById(R.id.scroll);
        sv.removeAllViews();

        adm= (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        Intent intent = getIntent();
        if(intent.getStringExtra("place")!=null) {
            place_name = intent.getStringExtra("place");
        }
        else
        {
            place_name="Cannot retrieve place, some error occured.";
        }

        /*if(intent.getIntExtra("com.bewtechnologies.gtone.restore",ringstate)==AudioManager.RINGER_MODE_NORMAL)
        {
            adm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        }

        else if(intent.getIntExtra("com.bewtechnologies.gtone.restore",ringstate)==AudioManager.RINGER_MODE_VIBRATE)
        {
            adm.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }

        else
        {
            adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
*/

//        setContentView(R.layout.user_setting);
       // LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        usrst=(LinearLayout)findViewById(R.id.user_setting);
       if (usrst==null) {

           View contentView = getLayoutInflater().inflate(R.layout.user_setting, null, false);
           mDrawerLayout.addView(contentView);
           selected_place = (TextView) findViewById(R.id.selection);
            gotdata = (TextView) findViewById(R.id.gotdata);
           delDb = (Button) findViewById(R.id.clear_db);
       }

            selected_place.setText("Place name : " + place_name);

            //Getting data from db
            dbHelper = new LocationDBHelper(getApplicationContext());
            gtone= dbHelper.getReadableDatabase();

        Cursor c = gtone.rawQuery("Select * from location",null);

        int i=0;
        if(c!=null&&c.getCount()>0)

        {
            c.moveToFirst();
            do {


                 s =s+" " + c.getString(0)+" "+ c.getColumnName(1) + " " +
                         c.getString(1) +" " + c.getString(2)+ " "+c.getColumnName(3) +" "+ c.getString(3)+ " " + c.getString(4)+ " "+c.getString(5)+ "\n";




            }while (c.moveToNext());
            Log.i("My string :", s);
        }

        gotdata.setText("Got data : " + s);


        delDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gtone= dbHelper.getWritableDatabase();
                gtone.delete(LocationContract.LocationEntry.TABLE_NAME,null,null);
            }
        });


        /**
         * Button which will be used to check all settings possible -- The Ultimo.
         */

        chkset=(Button) findViewById(R.id.chk);
        chkset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                /** //Silent phone
                 *AudioManager adm= (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                 *adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                */
            }
        });

        dbHelper.close();


    }


    public boolean checkMatch(double latitude, double longitude,Context context) {

       boolean IsNear=false;

       // Toast.makeText(context,"Inside check match",Toast.LENGTH_SHORT).show();


       double dblat;
       double dblong;


        //Getting data from db
        dbHelper = new LocationDBHelper(context);
        gtone= dbHelper.getReadableDatabase();




            Cursor c = gtone.rawQuery("Select lat, long from location", null);
            c.moveToFirst();

            int i = 0;
            if (c != null && c.getCount() > 0) {
                do {
                    dblat = c.getDouble(0);
                    dblong = c.getDouble(1);

                    Log.i("Inside check match recevied coordinates -->", "Here: " + latitude + longitude);

                    Log.i("Inside check match db coordinates -->", "Here: " + dblat + dblong);

                    double dist = measure(latitude, longitude, dblat, dblong);
                    Log.i("Value of dist--->", "here : " + dist);
                    if (dist < 100) {
                        Log.i("Inside if -->", "Yes we are.");
                        dblatitude=dblat;
                        dblongitude=dblong;
                        IsNear = true;
                        break;
                    }

                } while (c.moveToNext());




           /* if(IsNear==true)
            {
                //Silent phone

                adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Toast.makeText(context,"The settings are a go!",Toast.LENGTH_SHORT).show();
            }
*/

        }
        Log.i("Exiting checkmatch-->"," yes we are.");
        return IsNear;


    }


    //the other checkmatch to not bother user once he cancelled the notification at a place.

    public boolean checkMatch(double elat,double elong,double latitude, double longitude) {

        boolean IsNear=false;




        double dist= measure(elat,elong,latitude,longitude);

        Log.i("Inside checkmatch 2-->"," coord="+"elat :"+ elat+" elong"+ elong+" alatitude"+ latitude+" alongitude"+longitude);

        Log.i("Inside checkmatch 2-->"," yes we are.");
        Log.i("Value of dist---> 2 ", "here : " + dist);

        if(dist<100)
        {
            IsNear=true;
            return  IsNear;
        }


            return IsNear;







    }








    public double measure(double lat1, double lon1, double lat2, double lon2){  // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }



}
