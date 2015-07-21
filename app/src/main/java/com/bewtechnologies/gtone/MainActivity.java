package com.bewtechnologies.gtone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;




public class MainActivity extends AppCompatActivity
      implements GoogleApiClient.OnConnectionFailedListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     /** Used to store the last screen title. For use in {link #restoreActionBar()}.*/

    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    //my variables
    private static final String TAG = "MY_TAG";
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    //for setting
    private Button goset;
    private TextView sel_place;
    static public String place_name;
    static public String place_id;

    //for current location
    double longitude;
    double latitude;


    //for selected location
    LatLng co_place;
   static double slongitude;
   static double slatitude;

    //checking sound
    Button set;

    /**
            * GoogleApiClient wraps our service connection to Google Play Services and provides access
            * to the user's sign in state as well as the Google's APIs.
            */
    protected GoogleApiClient mGoogleApiClient;

    private PlacesAutocompleteAdapter mpAdapter;

    private AutoCompleteTextView mAutocompleteView;

    private TextView mPlaceDetailsText;

    private TextView mPlaceDetailsAttribution;

    private  LatLngBounds BOUNDS ;


    //Database

    private LocationDBHelper dbHelper;
    private SQLiteDatabase  gtone;


    // Location updates

    LocationManager mlm;
    ShowLocationActivity locationListener;
    private PendingIntent restoreAudioState;
    public int ringstate;





    @Override
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerList = (ListView) findViewById(R.id.navList);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();


        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);




            //check match
            usersetting cm = new usersetting();
        locationListener =  new ShowLocationActivity(getApplicationContext());
        AudioManager adm = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ringstate = adm.getRingerMode();


        Intent i = new Intent(MainActivity.this, restoreAudio.class);
        i.putExtra("com.bewtechnologies.gtone.restore", ringstate);
        restoreAudioState = PendingIntent.getService(MainActivity.this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);



            if (cm.checkMatch(ShowLocationActivity.latitude, ShowLocationActivity.longitude, getApplicationContext())) {

                Toast.makeText(getApplicationContext(), "Inside onCreate", Toast.LENGTH_SHORT).show();
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.batdroid)
                                .addAction(R.drawable.next, "Turn of silent mode?", restoreAudioState)
                                .setContentTitle("gtone")
                                .setAutoCancel(true)
                                .setContentText("Your phone is now on silent.");
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


                Notification note = mBuilder.build();

                note.flags |= note.DEFAULT_LIGHTS | note.FLAG_AUTO_CANCEL;


                adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                // Builds the notification and issues it.
                mNotifyMgr.notify(0, note);
            }






        //checking location

        mlm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);






        if((mlm.isProviderEnabled(LocationManager.GPS_PROVIDER)))
            {
                Toast.makeText(getApplicationContext(),"Inside mlm gps",Toast.LENGTH_SHORT).show();

                mlm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 10, locationListener);

                if(cm.checkMatch(ShowLocationActivity.latitude,ShowLocationActivity.longitude,getApplicationContext()))
                {
                    Toast.makeText(getApplicationContext(),"latitude inside gps: "+ShowLocationActivity.latitude,Toast.LENGTH_SHORT).show();
                   // AudioManager adm = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                    Toast.makeText(getApplicationContext(),"Inside onCreate",Toast.LENGTH_SHORT).show();
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.batdroid)
                                    .addAction(R.drawable.next, "Turn of silent mode?", restoreAudioState)
                                    .setContentTitle("gtone")
                                    .setAutoCancel(true)
                                    .setContentText("Your phone is now on silent.");
                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);



                    Notification note = mBuilder.build();

                    note.flags|=note.DEFAULT_LIGHTS|note.FLAG_AUTO_CANCEL;


                    adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    // Builds the notification and issues it.
                    mNotifyMgr.notify(0, note);



                    Toast.makeText(getApplicationContext(),"Back from check m oncreate", Toast.LENGTH_SHORT).show();
                }


                mlm.removeUpdates(locationListener);
            }


            if((mlm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))&&(!(mlm.isProviderEnabled(LocationManager.GPS_PROVIDER))))
            {

                Toast.makeText(getApplicationContext(),"Inside mlm net pro",Toast.LENGTH_SHORT).show();

                mlm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 10, locationListener);
                if(cm.checkMatch(ShowLocationActivity.latitude,ShowLocationActivity.longitude,getApplicationContext()))
                {
                    Toast.makeText(getApplicationContext(),"Inside onCreate",Toast.LENGTH_SHORT).show();
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.batdroid)
                                    .addAction(R.drawable.next, "Turn of silent mode?", restoreAudioState)
                                    .setContentTitle("gtone")
                                    .setAutoCancel(true)
                                    .setContentText("Your phone is now on silent.");
                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    Notification note = mBuilder.build();

                            note.flags|=note.DEFAULT_LIGHTS|note.FLAG_AUTO_CANCEL;


                    adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    // Builds the notification and issues it.
                    mNotifyMgr.notify(0, note);


                }
                mlm.removeUpdates(locationListener);
            }



        BOUNDS = new LatLngBounds(
                new LatLng(latitude - 1, longitude - 1), new LatLng(latitude + 1, longitude + 1));


        //places


        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();


// Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mpAdapter = new PlacesAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS, null);
        mAutocompleteView.setAdapter(mpAdapter);

        // Set up the 'clear text' button that clears the text in the autocomplete view
        Button clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteView.setText("");
            }
        });

        //Create db, before going to setting page for the first time.

//               dbHelper.onCreate(gtone);


        //dbHelper.onCreate(gtone);


        //to the setting page
        goset = (Button) findViewById(R.id.go_set);





        goset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,usersetting.class);
                startActivity(i);
                sel_place = (TextView) findViewById(R.id.selection);
            }
        });
        //checking sound
        set=(Button) findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent set = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(set);
            }

        });



      /*  mlm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                30000,
                10,
                locationListener);

        mlm.removeUpdates(locationListener);*/
    } /* end of OnCreate. */





    @Override
    protected void onPause() {
        super.onPause();
        mlm.removeUpdates(locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        usersetting cm =new usersetting();
        locationListener = new ShowLocationActivity(getApplicationContext());
        mlm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if((mlm.isProviderEnabled(LocationManager.GPS_PROVIDER)))
        {
            Toast.makeText(getApplicationContext(),"Inside mlm gps resume",Toast.LENGTH_SHORT).show();
            mlm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 50, locationListener);
            Log.i("gps resume co: ", ShowLocationActivity.latitude+" "+ShowLocationActivity.longitude);
            if(cm.checkMatch(ShowLocationActivity.latitude,ShowLocationActivity.longitude,getApplicationContext()))
            {
                AudioManager adm = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Toast.makeText(getApplicationContext(),"Back from check m resume", Toast.LENGTH_SHORT).show();

            }
            mlm.removeUpdates(locationListener);
        }


        if((mlm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))&&!((mlm.isProviderEnabled(LocationManager.GPS_PROVIDER))))
        {
            Toast.makeText(getApplicationContext(),"Inside mlm net pro resume",Toast.LENGTH_SHORT).show();

            mlm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 50, locationListener);

            if(cm.checkMatch(ShowLocationActivity.latitude,ShowLocationActivity.longitude,getApplicationContext()))
            {
                AudioManager adm = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Toast.makeText(getApplicationContext(),"Back from check m resume", Toast.LENGTH_SHORT).show();

            }

            mlm.removeUpdates(locationListener);
        }


    }

      /**
        mNavigationDrawerFragment = (NavigationDrawerFragment)
        getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        Set up the drawer.
        mNavigationDrawerFragment.setUp(
        R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout));*/




   //to set items on list:

    private void addDrawerItems()
    {
               String[] osArray = { "About us" };
               mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
               mDrawerList.setAdapter(mAdapter);

        //setting on clicklistener

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupDrawer(){

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()


            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()


            }
        };


    }


           // On back pressed, if drawer is open, this closes it.
           @Override
           public void onBackPressed(){
               if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){ //replace this with actual function which returns if the drawer is open
                    mDrawerLayout.closeDrawer(Gravity.LEFT);     // replace this with actual function which closes drawer
               }
               else{
                   super.onBackPressed();
               }
           }


           @Override
           public boolean onOptionsItemSelected(MenuItem item) {
               // Handle action bar item clicks here. The action bar will
               // automatically handle clicks on the Home/Up button, so long
               // as you specify a parent activity in AndroidManifest.xml.
               int id = item.getItemId();

               //noinspection SimplifiableIfStatement
               if (id == R.id.action_settings) {
                   return true;
               }

               // Activate the navigation drawer toggle
               if (mDrawerToggle.onOptionsItemSelected(item)) {
                   return true;
               }



               return super.onOptionsItemSelected(item);
           }


           //keeping everything in sync when drawer is opened

           @Override
           protected void onPostCreate(Bundle savedInstanceState) {
               super.onPostCreate(savedInstanceState);
               mDrawerToggle.syncState();
           }



           //changes in config are handled here, like going form portrait to landscape

           @Override
           public void onConfigurationChanged(Configuration newConfig) {
               super.onConfigurationChanged(newConfig);
               mDrawerToggle.onConfigurationChanged(newConfig);
           }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();

                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
             place_name = place.getName().toString();
             place_id= place.getId();

            //Getting latitude and longitude

            co_place = place.getLatLng();
            slatitude = co_place.latitude;

            slongitude= co_place.longitude;
            String s = slatitude+ " "+slongitude;

            Log.i("My latlng : ",s);


            // Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
                //InsertInDb(place_id,place_name,slatitude,slongitude);
                usersetting cm = new usersetting();
                cm.checkMatch(latitude,longitude,getApplicationContext());

            }

            Log.i(TAG, "Place details received: " + place.getName());
            Log.i(TAG, "Place coordinates received: " + place.getLatLng());
            places.release();
        }
    };

    public  void InsertInDb(String place_id, String place_name, double slatitude, double slongitude, String RINGER_MODE) {

        //inserting values into db
        dbHelper = new LocationDBHelper(getApplicationContext());

        gtone = dbHelper.getWritableDatabase();

        String val= place_id+" "+place_name+" "+slatitude+" "+slongitude+" "+ RINGER_MODE;

        Log.i("Values : ", val);

        ContentValues values= new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_PLACE_ID,place_id);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_PLACE_NAME,place_name);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_PLACE_LAT,slatitude);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_PLACE_LONG,slongitude);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_SETTING,RINGER_MODE);

        long newrowid;

        newrowid= gtone.insert(LocationContract.LocationEntry.TABLE_NAME, null,values);

        dbHelper.close();

    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }





    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlacesAutocompleteAdapter.PlaceAutocomplete item = mpAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


            Toast.makeText(getApplicationContext(), "Clicked: " + item.description,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);



        }
    };


}

     /**
        * @Override
        * public void onNavigationDrawerItemSelected(int position) {
             update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        }

        public void onSectionAttached(int number) {
            switch (number) {
                case 1:
                    mTitle = getString(R.string.title_section1);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section2);
                    break;
                case 3:
                    mTitle = getString(R.string.title_section3);
                    break;
            }
        }

        public void restoreActionBar() {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
                // Only show items in the action bar relevant to this screen
                // if the drawer is not showing. Otherwise, let the drawer
                // decide what to show in the action bar.
                getMenuInflater().inflate(R.menu.main, menu);
                restoreActionBar();
                return true;
            }
            return super.onCreateOptionsMenu(menu);
        }*/




    /**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {
        *//**
         * The fragment argument representing the section number for this
         * fragment.
         *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        *//**
         * Returns a new instance of this fragment for the given section
         * number.
         *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
*/

