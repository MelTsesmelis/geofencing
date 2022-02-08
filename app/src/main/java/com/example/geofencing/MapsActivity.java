package com.example.geofencing;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.geofencing.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener
{
    //arxikopoihseis
    private GoogleMap myGMap;
    private ActivityMapsBinding activityMapsBinding;
    private GeofencingClient geofencingClient;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private GeofenceHelper geofenceHelper;
    private float GEOFENCE_RADIUS = 200;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";
    private static final String TAG = " MapsActivity";
    private static Context context;


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        //orizw to pou tha ksekinhsei to map ... sthn Eleusina
        myGMap = googleMap;
        // Add a marker in eleysis and move the camera
        LatLng eleysis = new LatLng(38.041285, 23.541755);
        myGMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eleysis, 16));
        enableUserLocation(); // enable user location
        myGMap.setOnMapLongClickListener(MapsActivity.this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //kanw binding to activity map kai dhmiourgw client kai helper gia to geofencing
        super.onCreate(savedInstanceState);
        context=this;
        activityMapsBinding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(activityMapsBinding.getRoot());
        // Pairnei to  SupportMapFragment kai eidopoiei an to map einai etoimo.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
    }

    //ask for permissions
    private void enableUserLocation()
    {
        //Rwtaw ton xrhsth gia na mou dwsei adeia gia thn topothesia tou
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //an thn dwsei dinw kai exw access sto map mou na sunexisei
            myGMap.setMyLocationEnabled(true);
        } else {
            //an oxi tote ksanazhtaw to permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
    }


    //Edw ginontai oi elegxoi gia ta permissions LOCATION_ACCESS_REQUEST_CODE kai BACKROUND_LOCATION_ACCESS_REQUEST_CODE
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //se auto to shmeio exoume to permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the pe`rmission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                myGMap.setMyLocationEnabled(true);
            } else {
                // we do not have the permission
                Log.d(TAG, "onRequestPermissionsResult: We need the permission to continue...");
            }

            //Elegxw gia to BACKGROUND LOCATION access kai permissions ... gia na prostethoun ta geofences
            if (requestCode == BACKROUND_LOCATION_ACCESS_REQUEST_CODE)
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //an exoume to permission ola kala
                    Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show();
                } else {
                    //an den to exoume den mporoume na sunexisoume gia ta baloume ta geofences -triggers
                    Toast.makeText(this, "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    //O xrhsths prepei na pathsei paratetamena gia na tou emfanistei h koukida
    @Override
    public void onMapLongClick(LatLng latLng)
    {
        //logw twn diaforetikwn sumperiforwn ana ekdosh, ginetai elegxos gia to sdk
        if(Build.VERSION.SDK_INT>=29)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                tryAddingGeofence(latLng);
            }else{
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKROUND_LOCATION_ACCESS_REQUEST_CODE);
            }
        }else {
            tryAddingGeofence(latLng);
        }
    }
    //prosthetei to geofence, thn koukida me ton kuklo
    private void tryAddingGeofence(LatLng latLng)
    {
        myGMap.clear();
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
    }



    //boitha sthn prothiki geofence
    private void addGeofence(LatLng latLng, float radius)
    {
        //mesw tou helper stelnw to request kai xrhsimopoiw ta intents kai ton broadcast receiver gia to pending
        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //se periptwsh epituxias bazw enan listener gia na steilw katallhlo mhnuma gia thn prosthikh h mh tou geofence
        geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: Geofence Added...");
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String errorMessage= geofenceHelper.getErrorString(e);
                Log.d(TAG, "onFailure: " +errorMessage);
            }
        });
    }


    //ulopoiei thn prosthiki tou marker sto position pou pataei o xrhsths
    private void addMarker(LatLng latLing)
    {
        MarkerOptions markerOptions = new MarkerOptions().position(latLing);
        myGMap.addMarker(markerOptions);
    }

    //ulopoiei ton kuklo se kokkino xrwma gurw apo to marker pou phge to geofence
    private void addCircle(LatLng latLng, float radius)
    {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.fillColor(Color.argb(64,255,0,0));
        circleOptions.strokeWidth(4);
        myGMap.addCircle(circleOptions);
    }


    //ftiaxnw getter setters gia tis metablhtes na einai prospelasimes kai se alles klaseis
    public GoogleMap getmMap()
    {
        return myGMap;
    }

    public void setmMap(GoogleMap mMap)
    {
        this.myGMap = mMap;
    }

    public ActivityMapsBinding getActivityMapsBinding()
    {
        return activityMapsBinding;
    }

    public void setActivityMapsBinding(ActivityMapsBinding activityMapsBinding)
    {
        this.activityMapsBinding = activityMapsBinding;
    }

    public GeofencingClient getGeofencingClient()
    {
        return geofencingClient;
    }

    public void setGeofencingClient(GeofencingClient geofencingClient)
    {
        this.geofencingClient = geofencingClient;
    }

    public int getFINE_LOCATION_ACCESS_REQUEST_CODE()
    {
        return FINE_LOCATION_ACCESS_REQUEST_CODE;
    }

    public void setFINE_LOCATION_ACCESS_REQUEST_CODE(int FINE_LOCATION_ACCESS_REQUEST_CODE)
    {
        this.FINE_LOCATION_ACCESS_REQUEST_CODE = FINE_LOCATION_ACCESS_REQUEST_CODE;
    }

    public int getBACKROUND_LOCATION_ACCESS_REQUEST_CODE()
    {
        return BACKROUND_LOCATION_ACCESS_REQUEST_CODE;
    }

    public void setBACKROUND_LOCATION_ACCESS_REQUEST_CODE(int BACKROUND_LOCATION_ACCESS_REQUEST_CODE)
    {
        this.BACKROUND_LOCATION_ACCESS_REQUEST_CODE = BACKROUND_LOCATION_ACCESS_REQUEST_CODE;
    }

    public GeofenceHelper getGeofenceHelper()
    {
        return geofenceHelper;
    }

    public void setGeofenceHelper(GeofenceHelper geofenceHelper)
    {
        this.geofenceHelper = geofenceHelper;
    }

    public float getGEOFENCE_RADIUS()
    {
        return GEOFENCE_RADIUS;
    }

    public void setGEOFENCE_RADIUS(float GEOFENCE_RADIUS)
    {
        this.GEOFENCE_RADIUS = GEOFENCE_RADIUS;
    }

    public String getGEOFENCE_ID()
    {
        return GEOFENCE_ID;
    }

    public void setGEOFENCE_ID(String GEOFENCE_ID)
    {
        this.GEOFENCE_ID = GEOFENCE_ID;
    }

    public static String getTAG()
    {
        return TAG;
    }

    public static Context getContext()
    {
        return context;
    }

    public static void setContext(Context context)
    {
        MapsActivity.context = context;
    }
}