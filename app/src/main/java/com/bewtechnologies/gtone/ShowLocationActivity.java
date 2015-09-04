package com.bewtechnologies.gtone;

/**
 * Created by Aman on 7/16/2015.
 */

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ShowLocationActivity implements LocationListener {
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
  // private Context context;


    //Database

    private LocationDBHelper dbHelper;
    private SQLiteDatabase gtone;

    public static double latitude;
    public static double longitude;


    public  static  boolean match;



    public ShowLocationActivity(Context context){

        /*latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);*/

        // Get the location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);




        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
           // Toast.makeText(context,"Location  available"+location.getLatitude(),Toast.LENGTH_SHORT).show();

            //keeping current location
            latitude=location.getLatitude();
            longitude=location.getLongitude();

//            usersetting cm= new usersetting();
//            match=cm.checkMatch(location.getLatitude(),location.getLongitude(),context);


        }


        else {
          //  latituteField.setText("Location not available");
            //longitudeField.setText("Location not available");
          /*  usersetting cm = new usersetting();
            match=cm.checkMatch(latitude,longitude,context);*/

            Toast.makeText(context,"Location not avaialable,using prev ones",Toast.LENGTH_SHORT).show();

            //ask user to enable gps
            try {
                int locationmode= Settings.Secure.getInt(context.getContentResolver(),Settings.Secure.LOCATION_MODE);

                switch(locationmode)
                {
                    case 0: createdialog(context);
                            break;
                    case 1: createdialog(context);
                        break;

                    case 2:createdialog(context);
                        break;


                    case 3:
                        break;
                    default: break;

                }

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }



        }


    }

    private void createdialog(Context context) {

      try {
          AlertDialog.Builder adialog = new AlertDialog.Builder(MainActivity.mcon);
          adialog.setTitle("Enable GPS settings");
          adialog.setMessage("Location service of your phone needs to be enabled for this app and it's mode must be set to high accuracy. Do you want to enable it?");


          adialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

                  Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                  MainActivity.mcon.startActivity(i);
              }
          });

          adialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  dialog.cancel();
              }
          });

          adialog.show();
      }
      catch(Exception e)
      {
          e.printStackTrace();
          Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          MainActivity.mcon.startActivity(i);
          PendingIntent pendInt = PendingIntent.getService(MainActivity.mcon,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
          // if alertdialog box fails, try notification.
          NotificationCompat.Builder mBuilder =
                  new NotificationCompat.Builder(MainActivity.mcon)
                          .setSmallIcon(R.drawable.ic_launcher)
                          .setContentText("GPS needs to be enabled and set to high accuracy for Gtone to work.")
                          .setContentTitle("alarmreceive")
                          .setContentIntent(pendInt)
                          .setAutoCancel(true);
          NotificationManager mNotifyMgr =
                  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


          Notification note = mBuilder.build();

          note.flags |= note.DEFAULT_LIGHTS | note.FLAG_AUTO_CANCEL;


          // Builds the notification and issues it.
          mNotifyMgr.notify(0, note);

      }


    }

    //  private void checkIfmatch(Context context,double latitude, double longitude) {

    //

    //dbHelper = new LocationDBHelper(context);

    //gtone = dbHelper.getReadableDatabase();

    //Cursor c= gtone.rawQuery("SELECT PLACENAME,LAT,LONG FROM LOCATIONS WHERE LAT ='"+latitude+"' AND LONG ='"+longitude+"';",null);

    //

    //if(c.getCount()>0)

    //{

    //Log.i("Matched : "+c.moveToFirst()+c.getString(0)+c.getString(1)+c.getString(2)," good");

    //Toast.makeText(context,"We have a match at "+ longitude+" " +longitude,Toast.LENGTH_SHORT).show();

    //}

    //

    //}

    /* Request updates at startup */
    // @Override

    //protected void onResume() {

    //super.onResume();

    //locationManager.requestLocationUpdates(provider, 400, 10, this);

    //}

    // Remove the locationlistener updates when Activity is paused

    // protected void onPause() {

    //super.onPause();

    //locationManager.removeUpdates(this);

    //}

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();



    }




    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
       // Toast.makeText( ,"provider enabled",Toast.LENGTH_SHORT);
        Log.i("Provider enabled :" , provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Provider disabled :", provider);
    }
}

