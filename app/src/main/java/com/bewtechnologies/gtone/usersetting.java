package com.bewtechnologies.gtone;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView sv = (ScrollView)findViewById(R.id.scroll);
        sv.removeAllViews();
        Intent intent = getIntent();
        if(intent.getStringExtra("place")!=null) {
            place_name = intent.getStringExtra("place");
        }
        else
        {
            place_name="Cannot retrieve place, some error occured.";
        }
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

        Cursor c = gtone.rawQuery("Select * from locations",null);

        int i=0;
        if(c!=null&&c.getCount()>0)

        {
            c.moveToFirst();
            do {


                 s =s+" " + c.getString(0)+" "+ c.getColumnName(1) + " " +
                         c.getString(1) +" " + c.getString(2)+ " " + c.getString(3)+ " " + c.getString(4)+"\n";



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
        dbHelper.close();
    }




}
