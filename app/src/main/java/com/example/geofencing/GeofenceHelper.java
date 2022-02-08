package com.example.geofencing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceHelper extends ContextWrapper {

    //arxikopoihseis
    private static final String TAG = "GeofenceHelper";
    PendingIntent pendingIntent;


    //make a constructor
    public GeofenceHelper(Context base)
    {
        super(base);
    }

    //tou dinw geofence kai an mpei to gps mesa ston kuklo tha steilei oti ekane ENTER kai antistoixa otan bgei tha steilei EXIT
    public GeofencingRequest getGeofencingRequest( Geofence geofence)
    {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER |GeofencingRequest.INITIAL_TRIGGER_EXIT)
                .build();
    }

    //sthn ousia dhmiourgei ton kuklo
    public Geofence getGeofence(String ID, LatLng latLng, float radius , int transitionTypes)
    {
        int delay = 2000; //to delay pou tha exei gia na deiksei oti eisai sthn perioxh
        return new Geofence.Builder()
                .setCircularRegion(
                            latLng.latitude,
                            latLng.longitude,
                            radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes) // ton endiaferoun mono ta transitions EXIT ENTER
                .setLoiteringDelay(delay)
                .setExpirationDuration(Geofence.NEVER_EXPIRE) // Epomenws meta to kathorismeno delay tha sunexisei na ufistantia o kuklos
                .build();
    }

    //xrhsimopoiei ta intents gia na sundesei ton broadcast receiver me authn thn klash
    public PendingIntent getPendingIntent()
    {
        if (pendingIntent != null)
        {
            return pendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        pendingIntent= PendingIntent.getBroadcast(
                this,
                2607,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    //Ulopoiw ektupwseis gia periptwseis lathous
    public String getErrorString(Exception e)
    {
        if (e instanceof ApiException)
        {
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode())
            {
                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
            }
        }
        return e.getLocalizedMessage();
    }
}
