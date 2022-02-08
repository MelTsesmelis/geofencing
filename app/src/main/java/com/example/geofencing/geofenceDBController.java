package com.example.geofencing;

import androidx.room.Database;
import androidx.room.RoomDatabase;


/*
  Kanw abstract thn geofenceDAO gia na dhmiourghsw kai to database access object (DAO)
  Epomenws mesw twn entities boitha sthn DB sthn ousia exei rolo enos controller
  pou mas deixnei thn antistoixia toy select me thn bash kanontas extend kai to room
  opws eixame deiksei
 */
@Database(entities = {Cordinates.class},version = 1) //Domh kai ekdosh ths bashs
public abstract class geofenceDBController extends RoomDatabase {
    public abstract geofenceDAO geofenceDao(); //

}
