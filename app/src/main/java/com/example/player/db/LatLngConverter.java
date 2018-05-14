package com.example.player.db;

import android.arch.persistence.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;

public class LatLngConverter {

    @TypeConverter
    public static LatLng fromObject(String data) {

        if (data == null || data.equals("")) {
            return new LatLng(47.608013, -122.335167);
        }

        String[] coord = data.split(",");

        return new LatLng(Double.valueOf(coord[0]), Double.valueOf(coord[1]));
    }

    @TypeConverter
    public static String fromLatLngtToObject(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }
}