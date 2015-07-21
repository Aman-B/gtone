package com.bewtechnologies.gtone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

/**
 * Created by Aman  on 7/21/2015.
 */

public class SettingsActivity extends MainActivity {

    LinearLayout setpage;
    ArrayAdapter<String> setadapter;
    ListView lv;
   static Button save;
    RadioButton rb;
    RadioGroup rg;
    public static String RINGER_MODE=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        ScrollView sv = (ScrollView) findViewById(R.id.scroll);
        sv.removeAllViews();


         setpage = (LinearLayout) findViewById(R.id.settings_page);

        if(setpage==null) {
            View contentView = getLayoutInflater().inflate(R.layout.settings_page, null, false);
            mDrawerLayout.addView(contentView);
        }
        rg=(RadioGroup) findViewById(R.id.rbg);
        save = (Button) findViewById(R.id.savesetting);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selection = rg.getCheckedRadioButtonId();
                Log.i("Got rb id ?", "Here:" + selection);

                rb=(RadioButton) findViewById(selection);
                Log.i("Got rb ?", "Here:" + rb);
                RINGER_MODE = rb.getText().toString();




            }
        });





        save.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {


                if (place_id != null && SettingsActivity.RINGER_MODE != null)
                {
                    InsertInDb(place_id, place_name, slatitude, slongitude,RINGER_MODE);
                }


            }
        });
        /*else
        {
            save.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("Got 2 ringer mode?", "Here:" + RINGER_MODE);
                    Toast.makeText(getApplicationContext(),"No radio btn selected", Toast.LENGTH_SHORT).show();
                }
            });

        }
*/







    }



}
