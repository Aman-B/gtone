package com.bewtechnologies.gtone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Aman  on 8/27/2015.
 */
public class Bootreceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Br:", "running");
       // context.startService(new Intent(context,AlarmReceiver.class));

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval =1000*60*2;
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);


       PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

       /* Intent intr = new Intent(context,AlarmReceiver.class);
        context.startService(intr);*/

        if("com.bewtechnologies.gtone.Mate".equals(intent.getAction()))
        {
            Log.i("Br is :", "reading");
        }
    }
}
