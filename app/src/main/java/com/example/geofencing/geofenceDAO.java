package com.example.geofencing;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;


@Dao
//ulopoiw to query ths bashs kai thn proshidi suntetagmenwn
public interface geofenceDAO
{

    @Query("SELECT * FROM CORDINATES")
    List<Cordinates> getAllCordinates();

    @Insert
    void insertCordinate(Cordinates... cordinates);
}
