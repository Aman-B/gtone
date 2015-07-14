package com.bewtechnologies.gtone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

       }

            selected_place.setText("Place name : " + place_name);


    }




}
