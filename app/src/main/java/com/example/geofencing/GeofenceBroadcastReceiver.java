package com.example.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver
{
    private static final String TAG = "GeofenceBroadCastReceive";

    public static String getTAG()
    {
        return TAG;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
         //  This method is called when the BroadcastReceiver is receiving
        //add an notificationHelper for notifications
        NotificationHelper notificationHelper = new NotificationHelper(context);

        Toast.makeText(context, "Geofence triggered... ", Toast.LENGTH_SHORT).show();

        //geofencing event
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError())
        {
            Log.d( "TAG" , "onReceive:Error geofence event");
            return;
        }

        //so we have no problems with geofencing event if we are here

        List <Geofence> geofenceList= geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence: geofenceList)
        {
            Log.d("TAG", "onReceive:"+geofence.getRequestId());
        }

        int transitionType = geofencingEvent.getGeofenceTransition();

        Location location = geofencingEvent.getTriggeringLocation();


        //new object  to insert in DB
        geofenceDB gdb = Room.databaseBuilder(context,geofenceDB.class,InitializationsDB.getTableName()).build();
        geofenceDAO geofenceDAO= gdb.geofenceDao();
        Cordinates cordinate = new Cordinates();

        cordinate.setLatitude(location.getLatitude());
        cordinate.setLongitude(location.getLongitude());
        cordinate.setTransition(transitionType);
        cordinate.setTimestamp(location.getTime());

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                geofenceDAO.insertCordinate(cordinate);
                List<Cordinates> cordinatesList = geofenceDAO.getAllCordinates();
                for(Cordinates cordinates:cordinatesList){
                    Log.d(GeofenceBroadcastReceiver.getTAG(),"\tlat: "+cordinates.getLatitude());
                    Log.d(GeofenceBroadcastReceiver.getTAG(),"\tlon: "+cordinates.getLongitude());
                    Log.d(GeofenceBroadcastReceiver.getTAG(),"\ttransitionType: "+cordinates.getTransition());
                    Log.d(GeofenceBroadcastReceiver.getTAG(),"\ttimestamp: "+cordinates.getTimestamp());
                }
            }
        });
        thread.start();

        //kathe fora pou tha mpainei h tha bgainei ston kuklo tote tha xtupaei katallhlo notification
        switch (transitionType)
        {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                //Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER","",MapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                //Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT","",MapsActivity.class);
                break;
        }
    }
}