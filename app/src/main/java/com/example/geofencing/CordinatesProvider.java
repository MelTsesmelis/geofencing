package com.example.geofencing;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;


/*
  Ulopoiw enan provider gia na mporesw na dwsw ta dedomena sthn deuterh efarmogh
 */
public class CordinatesProvider extends ContentProvider
{

    private UriMatcher uriMatcher;
    private Context context; // orizw to context gia to map , kai na to xrhsimopoihsw kai sthn deuterh efarmogh endexomenws

    //crete uri matcher and add URI
    @Override
    public boolean onCreate() { // otan ksekina to programma
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); // ftiaxnw ena adeio URI
        uriMatcher.addURI( //kai to arxikopoiw meta me times
                InitializationsDB.getAUTHORITY(),
                InitializationsDB.getTableName(),
                InitializationsDB.getDbVersion());
        return false;
    }


    // Cursor in query
    @Nullable
    @Override
    public Cursor query
        (
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder
        )
    {
        context = MapsActivity.getContext(); // pernaw to context tou map activity
        geofenceDBController dDB = Room.databaseBuilder( // kai xtizw thn bash
                context,
                geofenceDBController.class,
                InitializationsDB.getTableName()).build();
        Cursor cursor = null;

        if (uriMatcher.match(uri)==1){
            //select all from database
            SimpleSQLiteQuery q = new SimpleSQLiteQuery("SELECT * FROM "+InitializationsDB.getTableName());
            cursor = dDB.query(q);
        }else{
            //this query does not exist
                throw new IllegalArgumentException("This URI does not exist: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
