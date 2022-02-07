package com.example.geofencing;

public class InitializationsDB {
    static private String DB_NAME = "CORDINATES_DB";
    static private int DB_VERSION = 1;
    static private String TABLE_NAME = "CORDINATES";
    static private String AUTHORITY = "com.example.geofencing";

    //kataskeuazw constructor getters gia na nai prosbasimes oi metablhtes
    public InitializationsDB() {
    }

    public static String getDbName() {
        return DB_NAME;
    }


    public static int getDbVersion() {
        return DB_VERSION;
    }


    public static String getTableName() {
        return TABLE_NAME;
    }


    public static String getAUTHORITY() {
        return AUTHORITY;
    }

}
