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
    public void onReceive(Context context, Intent intent)// me to pou erthei kapoio ENTER h EXIT  apo ta geofences
    {
         //  This method is called when the BroadcastReceiver is receiving
        //add an notificationHelper for notifications
        NotificationHelper notificationHelper = new NotificationHelper(context);

        Toast.makeText(context, "Geofence triggered... ", Toast.LENGTH_SHORT).show();

        //geofencing event , gia na dw an ekane EXIT h ENTER kai ta cordinates
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        //se periptwsh sfalmatos
        if (geofencingEvent.hasError())
        {
            Log.d( "TAG" , "onReceive:Error geofence event");
            return;
        }

        //so we have no problems with geofencing event if we are here
        //Gurnaei thn lista  me ta geofence
        List <Geofence> geofenceList= geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence: geofenceList)
        {
            Log.d("TAG", "onReceive:"+geofence.getRequestId());
        }

        //edw xwrizw to event kai pairnw xwrista to transitionType
        int transitionType = geofencingEvent.getGeofenceTransition();

        //kai to location
        Location location = geofencingEvent.getTriggeringLocation();


        //edw ftiaxnw kainourgio object gia thn db
        geofenceDBController gdb = Room.databaseBuilder( //anoigw thn bash
                context,
                geofenceDBController.class,
                InitializationsDB.getTableName()).build();
        geofenceDAO geofenceDAO= gdb.geofenceDao();
        Cordinates cordinate = new Cordinates(); //pairnw to cordinate gia to insert

        cordinate.setLatitude(location.getLatitude());
        cordinate.setLongitude(location.getLongitude());
        cordinate.setTransition(transitionType);
        cordinate.setTimestamp(location.getTime());

        //ftiaxnw ena thread  gia na trexei ksexwrista
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
        thread.start(); //kai to ksekinaw


        //kathe fora pou tha mpainei h tha bgainei ston kuklo tote tha xtupaei katallhlo notification
        if(transitionType==Geofence.GEOFENCE_TRANSITION_ENTER){
            notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER","",MapsActivity.class);
        }else if(transitionType==Geofence.GEOFENCE_TRANSITION_EXIT){
            notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT","",MapsActivity.class);
        }

        //kleinw thn  db
        gdb.close();
    }
}