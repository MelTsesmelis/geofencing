package com.example.geofencing;

import androidx.room.Database;
import androidx.room.RoomDatabase;


//Kanw abstract thn geofenceDAO gia na dhmiourghsw kai thn  database
@Database(entities = {Cordinates.class},version = 1)
public abstract class geofenceDB extends RoomDatabase {
    public abstract geofenceDAO geofenceDao();

}
