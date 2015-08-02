package com.bewtechnologies.gtone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Aman  on 7/23/2015.
 */


public class AlarmReceiver extends BroadcastReceiver {

    public static double  elat; // returns latitude
    public static double elong;
    ShowLocationActivity locationListener;
    private PendingIntent restoreAudioState;
    public int ringstate;


    public double alarmlat;
    public double alarmlong;


    @Override
    public void onReceive(Context context, Intent intent) {



        TrackerService gps = new TrackerService(context);
        if(gps.canGetLocation())
        {
            elat = gps.getLatitude(); // returns latitude
            elong = gps.getLongitude();

            usersetting cm = new usersetting();



            locationListener = new ShowLocationActivity(context);
            AudioManager adm = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            ringstate = adm.getRingerMode();

            Intent i = new Intent(context.getApplicationContext(), restoreAudio.class);
            i.putExtra("com.bewtechnologies.gtone.restore", ringstate);
            restoreAudioState = PendingIntent.getService(context.getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);






            if(MainActivity.Notified==false) {
                if (cm.checkMatch(elat, elong, context)) {
                    MainActivity.Notified = true;
                    MainActivity.flat=elat;
                    MainActivity.flong=elong;
                    Log.i("Notified? ", " Let me do: " + MainActivity.Notified);

                    //Define sound URI
                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    //notification

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.batdroid)
                                    .addAction(R.drawable.next, "Undo?", restoreAudioState)
                                    .setContentTitle("alarmreceive")

                                    .setAutoCancel(true);




                    MainActivity ma = new MainActivity();
                    int savedRingerMode = ma.getSavedRingerMode(usersetting.dblatitude, usersetting.dblongitude, context);

                    if (savedRingerMode == 0) {
                        //Log.i("Inside main silent mode 1:", "yes");
                      //  adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        mBuilder.setContentTitle("Your phone is now silent.");
                    } else if (savedRingerMode == 1) {
                        // Log.i("Inside main vibrate mode 1:", "yes");
                       // adm.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        mBuilder.setContentTitle("Your phone is now on vibrate mode.");
                    } else {
                        // Log.i("Inside main normal  mode 1:", "yes");
                       // adm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        mBuilder.setContentTitle("Your phone is now on normal mode.");
                    }





                    NotificationManager mNotifyMgr =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                    Notification note = mBuilder.build();

                    note.flags |= note.DEFAULT_LIGHTS | note.FLAG_AUTO_CANCEL;


                    // Builds the notification and issues it.
                    mNotifyMgr.notify(0, note);

                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
                        r.play();

                        Thread.sleep(1000);
                        r.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (savedRingerMode == 0) {
                            //Log.i("Inside main silent mode 1:", "yes");
                            adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                        } else if (savedRingerMode == 1) {
                            // Log.i("Inside main vibrate mode 1:", "yes");
                            adm.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            //        mBuilder.setContentTitle("Your phone is now on vibrate mode.");
                        } else {
                            // Log.i("Inside main normal  mode 1:", "yes");
                            adm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            //  mBuilder.setContentTitle("Your phone is now on normal mode.");
                        }

                    }

                }
            }

            else{
                if(!(cm.checkMatch(elat,elong,MainActivity.flat,MainActivity.flong)))
                {
                    MainActivity.Notified=false;


                }
            }


        }
        Log.i("Notified? ", " Out; " + MainActivity.Notified);



    }


}
