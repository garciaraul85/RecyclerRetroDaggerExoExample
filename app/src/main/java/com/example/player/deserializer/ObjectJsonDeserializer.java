package com.example.player.deserializer;

import android.util.Log;

import com.example.player.model.BaseObject;
import com.example.player.model.Result;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by linke_000 on 17/08/2017.
 */
public class ObjectJsonDeserializer implements JsonDeserializer {
    private static String TAG = "JsonDeserializer";

    @Override
    public BaseObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || !json.isJsonObject()) {
            return null;
        }

        try {
            JsonObject jsonObject = json.getAsJsonObject();
            // return context.deserialize(jsonObject.get("data"), Example.class);
            return context.deserialize(jsonObject.get("Results"), Result.class);
        } catch (JsonParseException e) {
            Log.e(TAG, String.format("Could not deserialize: %s", json.toString()));
            return null;
        }
    }

}