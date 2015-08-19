package com.bewtechnologies.gtone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman  on 7/23/2015.
 */
public class SavedPlaces extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PlaceDetails> places;

    //for db
    private SQLiteDatabase gtone;
    private LocationDBHelper dbHelper;


    String[] myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_recycler_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);



        // specify an adapter (see also next example)


        initializeData(getApplicationContext());
        mAdapter = new CardAdapter(places);
        mRecyclerView.setAdapter(mAdapter);


    }



    private void initializeData(Context context){

        places= new ArrayList<>();
        //Getting data from db
        dbHelper = new LocationDBHelper(context);
        gtone= dbHelper.getReadableDatabase();

        if(!(gtone.isDbLockedByCurrentThread())) {

            Cursor c = gtone.rawQuery("Select placename,setting from location", null);

            try {


                int i = 0;
                if (c != null && c.getCount() > 0)

                {
                    c.moveToFirst();
                    do
                    {
                        places.add(new PlaceDetails(c.getString(0),c.getString(1)));

                    } while (c.moveToNext());
                }

            }

            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if((c!=null)&&(gtone!=null))
                {
                    gtone.close();
                    c.close();
                }
            }
        }






    }

}
