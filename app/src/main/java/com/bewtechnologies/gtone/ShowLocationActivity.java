package com.bewtechnologies.gtone;

/**
 * Created by Aman on 7/16/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ShowLocationActivity  implements LocationListener {
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
  // private Context context;


    //Database

    private LocationDBHelper dbHelper;
    private SQLiteDatabase gtone;

    public ShowLocationActivity(Context context){

        /*latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);*/

        // Get the location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);


        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            Toast.makeText(context,"Location  available"+location.getLatitude(),Toast.LENGTH_SHORT).show();
            checkIfmatch(context,location.getLatitude(),location.getLongitude());
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
            Toast.makeText(context,"Location not avaialable",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfmatch(Context context,double latitude, double longitude) {

        dbHelper = new LocationDBHelper(context);
        gtone = dbHelper.getReadableDatabase();
        Cursor c= gtone.rawQuery("SELECT PLACENAME,LAT,LONG FROM LOCATIONS WHERE LAT ='"+latitude+"' AND LONG ='"+longitude+"';",null);

        if(c.getCount()>0)
        {
            Log.i("Matched : "+c.moveToFirst()+c.getString(0)+c.getString(1)+c.getString(2)," good");
            Toast.makeText(context,"We have a match at "+ longitude+" " +longitude,Toast.LENGTH_SHORT).show();
        }

    }

    /* Request updates at startup */
   /* @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 10, this);
    }*/

    /* Remove the locationlistener updates when Activity is paused */

   /* protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }*/

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());


        //latituteField.setText(String.valueOf(lat));
        //longitudeField.setText(String.valueOf(lng));
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

