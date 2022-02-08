package com.example.geofencing;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;


@Dao
//Dhmiourgw to Dao gia na mh xreiazetai na kanw ta queries se SQL alla na xw mia pio aploustemenh morfh kai na ta pernaei se java
//ulopoiw to query ths bashs kai thn proshidi suntetagmenwn
public interface geofenceDAO
{

    @Query("SELECT * FROM CORDINATES")
    List<Cordinates> getAllCordinates();

    @Insert
    void insertCordinate(Cordinates... cordinates);
}
