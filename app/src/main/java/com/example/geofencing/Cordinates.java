package com.example.geofencing;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
  Dhmiourgeitai ena arxeio gia thn bash,  to opoio menei kathe fora pou to trexw kai sbhnw thn efarmogh
  ... alla sbhnetai me thn apegkatastash
  tou emulator h ths efarmoghs oloklhrhs
  Epomenws kathe fora pou trexei tha exei o,ti exei treksei kai exei kataxwrhthei sthn bash kai apo prohgoumena runs
  Epishs gia na doulepsei thelei  impement kai sto gradle na baloume to room gia to database

 */
@Entity(tableName = "CORDINATES")
public class Cordinates {

    //Den hksera ti na balw san primary kai ebala ena autogenerated key gia na uparxei
    @PrimaryKey(autoGenerate = true)
    public int id;

    //timestamp
    @ColumnInfo(name = "TIMESTAMP")
    public Long timestamp;

    //longitude
    @ColumnInfo(name = "LONG")
    public Double longitude;

    //latitude
    @ColumnInfo(name = "LAT")
    public Double latitude;


    //transition
    @ColumnInfo(name = "TRANSITION")
    public int transition;




    //getter and setters gia na einai prospelasima
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getTransition() {
        return transition;
    }

    public void setTransition(int transition) {
        this.transition = transition;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
